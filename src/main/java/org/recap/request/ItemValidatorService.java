package org.recap.request;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.recap.ScsbConstants;
import org.recap.ScsbCommonConstants;
import org.recap.controller.ItemController;
import org.recap.model.jpa.BibliographicEntity;
import org.recap.model.jpa.DeliveryCodeEntity;
import org.recap.model.jpa.DeliveryCodeTranslationEntity;
import org.recap.model.jpa.ImsLocationEntity;
import org.recap.model.jpa.InstitutionEntity;
import org.recap.model.jpa.ItemEntity;
import org.recap.model.jpa.ItemRequestInformation;
import org.recap.model.jpa.ItemStatusEntity;
import org.recap.model.jpa.OwnerCodeEntity;
import org.recap.model.jpa.RequestItemEntity;
import org.recap.repository.jpa.DeliveryCodeDetailsRepository;
import org.recap.repository.jpa.DeliveryCodeTranslationDetailsRepository;
import org.recap.repository.jpa.ImsLocationDetailsRepository;
import org.recap.repository.jpa.InstitutionDetailsRepository;
import org.recap.repository.jpa.ItemDetailsRepository;
import org.recap.repository.jpa.ItemStatusDetailsRepository;
import org.recap.repository.jpa.OwnerCodeDetailsRepository;
import org.recap.repository.jpa.RequestItemDetailsRepository;
import org.recap.util.PropertyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Map;

/**
 * Created by hemalathas on 11/11/16.
 */
@Component
public class ItemValidatorService {

    /**
     * The Scsb solr client url.
     */
    @Value("${scsb.solr.doc.url}")
    private String scsbSolrClientUrl;

    /**
     * The Item status details repository.
     */
    @Autowired
    private ItemStatusDetailsRepository itemStatusDetailsRepository;

    @Autowired
    private ImsLocationDetailsRepository imsLocationDetailsRepository;

    @Autowired
    private InstitutionDetailsRepository institutionDetailsRepository;

    @Autowired
    private DeliveryCodeTranslationDetailsRepository deliveryCodeTranslationDetailsRepository;


    /**
     * The Item details repository.
     */
    @Autowired
    private ItemDetailsRepository itemDetailsRepository;

    /**
     * The Item controller.
     */
    @Autowired
    private ItemController itemController;

    /**
     * The Owner code details repository.
     */
    @Autowired
    private OwnerCodeDetailsRepository onwerCodeDetailsRepository;

    @Autowired
    private DeliveryCodeDetailsRepository deliveryCodeDetailsRepository;

    @Autowired
    private RequestItemDetailsRepository requestItemDetailsRepository;

    @Autowired
    private PropertyUtil propertyUtil;

    /**
     * Item validation response entity.
     *
     * @param itemRequestInformation the item request information
     * @return the response entity
     */
    public ResponseEntity itemValidation(ItemRequestInformation itemRequestInformation) {
        List<ItemEntity> itemEntityList = getItemEntities(itemRequestInformation.getItemBarcodes());
        Map<String, String> frozenInstitutionPropertyMap = propertyUtil.getPropertyByKeyForAllInstitutions(ScsbCommonConstants.KEY_ILS_ENABLE_CIRCULATION_FREEZE);
        if (itemRequestInformation.getItemBarcodes().size() == 1) {
            if (itemEntityList != null && !itemEntityList.isEmpty()) {
                for (ItemEntity itemEntity1 : itemEntityList) {
                    if (!checkRequestItemStatus(itemEntity1.getBarcode(), ScsbCommonConstants.REQUEST_STATUS_INITIAL_LOAD)) {
                        return new ResponseEntity<>(ScsbConstants.INITIAL_LOAD_ITEM_EXISTS, getHttpHeaders(), HttpStatus.BAD_REQUEST);
                    }
                    if (Boolean.parseBoolean(frozenInstitutionPropertyMap.get(itemEntity1.getInstitutionEntity().getInstitutionCode()))) {
                        return new ResponseEntity<>(ScsbConstants.CIRCULATION_FREEZE_UNAVAILABLE_ITEM, getHttpHeaders(), HttpStatus.BAD_REQUEST);
                    }
                    String availabilityStatus = getItemStatus(itemEntity1.getItemAvailabilityStatusId());
                    if (availabilityStatus.equalsIgnoreCase(ScsbCommonConstants.NOT_AVAILABLE) && (itemRequestInformation.getRequestType().equalsIgnoreCase(ScsbCommonConstants.RETRIEVAL)
                            || itemRequestInformation.getRequestType().equalsIgnoreCase(ScsbCommonConstants.REQUEST_TYPE_EDD)
                            || itemRequestInformation.getRequestType().equalsIgnoreCase(ScsbCommonConstants.BORROW_DIRECT))) {
                        return new ResponseEntity<>(ScsbConstants.RETRIEVAL_NOT_FOR_UNAVAILABLE_ITEM, getHttpHeaders(), HttpStatus.BAD_REQUEST);
                    } else if (availabilityStatus.equalsIgnoreCase(ScsbCommonConstants.AVAILABLE) && itemRequestInformation.getRequestType().equalsIgnoreCase(ScsbCommonConstants.REQUEST_TYPE_RECALL)) {
                        return new ResponseEntity<>(ScsbConstants.RECALL_NOT_FOR_AVAILABLE_ITEM, getHttpHeaders(), HttpStatus.BAD_REQUEST);
                    }

                    String imsLocationCode = getImsLocation(itemEntity1.getImsLocationEntity().getId());
                    if (StringUtils.isBlank(imsLocationCode)) {
                        return new ResponseEntity<>(ScsbConstants.IMS_LOCATION_DOES_NOT_EXIST_ITEM, getHttpHeaders(), HttpStatus.BAD_REQUEST);
                    }

                    if(itemRequestInformation.getRequestType().equalsIgnoreCase(ScsbCommonConstants.REQUEST_TYPE_RECALL)) {
                        if (!checkRequestItemStatus(itemEntity1.getBarcode(), ScsbCommonConstants.REQUEST_STATUS_EDD)) {
                            return new ResponseEntity<>(ScsbConstants.RECALL_FOR_EDD_ITEM, getHttpHeaders(), HttpStatus.BAD_REQUEST);
                        }else if (!checkRequestItemStatus(itemEntity1.getBarcode(), ScsbCommonConstants.REQUEST_STATUS_CANCELED)) {
                            return new ResponseEntity<>(ScsbConstants.RECALL_FOR_CANCELLED_ITEM, getHttpHeaders(), HttpStatus.BAD_REQUEST);
                        }
                    }

                    if (!checkRequestItemStatus(itemEntity1.getBarcode(), ScsbCommonConstants.REQUEST_STATUS_RECALLED)) {
                        return new ResponseEntity<>(ScsbConstants.RECALL_FOR_ITEM_EXISTS, getHttpHeaders(), HttpStatus.BAD_REQUEST);
                    }

                    if (itemRequestInformation.getRequestType().equalsIgnoreCase(ScsbCommonConstants.REQUEST_TYPE_EDD)) {
                        OwnerCodeEntity onwerCodeEntity = onwerCodeDetailsRepository.findByOwnerCodeAndRecapDeliveryRestrictionLikeEDD(itemEntity1.getCustomerCode());
                        if (onwerCodeEntity == null) {
                            return new ResponseEntity<>(ScsbConstants.EDD_REQUEST_NOT_ALLOWED, getHttpHeaders(), HttpStatus.BAD_REQUEST);
                        }
                    }
                }
                ItemEntity itemEntity = itemEntityList.get(0);
                ResponseEntity responseEntity1 = null;
                if (itemRequestInformation.getRequestType().equalsIgnoreCase(ScsbCommonConstants.REQUEST_TYPE_RETRIEVAL) || itemRequestInformation.getRequestType().equalsIgnoreCase(ScsbCommonConstants.REQUEST_TYPE_RECALL)) {
                    int validateCustomerCode = checkDeliveryLocation(itemEntity.getCustomerCode(), itemRequestInformation);
                    if (validateCustomerCode == 1) {
                        int validateDeliveryTranslationCode = checkDeliveryLocationTranslationCode(itemEntity, itemRequestInformation);
                        if (validateDeliveryTranslationCode == 1) {
                            responseEntity1 = new ResponseEntity<>(ScsbCommonConstants.VALID_REQUEST, getHttpHeaders(), HttpStatus.OK);
                        } else if (validateDeliveryTranslationCode == 0) {
                            responseEntity1 = new ResponseEntity<>(ScsbConstants.INVALID_CUSTOMER_CODE, getHttpHeaders(), HttpStatus.BAD_REQUEST);
                        } else if (validateDeliveryTranslationCode == -1) {
                            responseEntity1 = new ResponseEntity<>(ScsbConstants.INVALID_TRANSLATED_CODE, getHttpHeaders(), HttpStatus.BAD_REQUEST);
                        }
                    } else if (validateCustomerCode == 0) {
                        responseEntity1 = new ResponseEntity<>(ScsbConstants.INVALID_CUSTOMER_CODE, getHttpHeaders(), HttpStatus.BAD_REQUEST);
                    } else if (validateCustomerCode == -1) {
                        responseEntity1 = new ResponseEntity<>(ScsbConstants.INVALID_DELIVERY_CODE, getHttpHeaders(), HttpStatus.BAD_REQUEST);
                    }
                } else {
                    responseEntity1 = new ResponseEntity<>(ScsbCommonConstants.VALID_REQUEST, getHttpHeaders(), HttpStatus.OK);
                }
                return responseEntity1;
            } else {
                return new ResponseEntity<>(ScsbConstants.WRONG_ITEM_BARCODE, getHttpHeaders(), HttpStatus.BAD_REQUEST);
            }
        } else if (itemRequestInformation.getItemBarcodes().size() > 1) {
            Set<Integer> bibliographicIds = new HashSet<>();
            for (ItemEntity itemEntity : itemEntityList) {
                List<BibliographicEntity> bibliographicList = itemEntity.getBibliographicEntities();
                for (BibliographicEntity bibliographicEntityDetails : bibliographicList) {
                    bibliographicIds.add(bibliographicEntityDetails.getId());
                }
            }
            return multipleRequestItemValidation(itemEntityList, bibliographicIds, itemRequestInformation, frozenInstitutionPropertyMap);
        }
        return new ResponseEntity<>(ScsbCommonConstants.VALID_REQUEST, getHttpHeaders(), HttpStatus.OK);
    }

    private List<ItemEntity> getItemEntities(List<String> itemBarcodes) {
        List<ItemEntity> itemEntityList = null;
        if (CollectionUtils.isNotEmpty(itemBarcodes)) {
            itemEntityList = itemController.findByBarcodeIn(itemBarcodes.toString());
        }
        return itemEntityList;
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(ScsbCommonConstants.RESPONSE_DATE, new Date().toString());
        return responseHeaders;
    }

    /**
     * Gets item status.
     *
     * @param itemAvailabilityStatusId the item availability status id
     * @return the item status
     */
    public String getItemStatus(Integer itemAvailabilityStatusId) {
        String status = "";
        Optional<ItemStatusEntity> itemStatusEntity = itemStatusDetailsRepository.findById(itemAvailabilityStatusId);
        if (itemStatusEntity.isPresent()) {
            status = itemStatusEntity.get().getStatusCode();
        }
        return status;
    }

    /**
     * Gets item status.
     *
     * @param imsLocationId the item availability status id
     * @return the item status
     */
    public String getImsLocation(Integer imsLocationId) {
        String imsLocationCode = "";
        Optional<ImsLocationEntity> imsLocationEntity = imsLocationDetailsRepository.findById(imsLocationId);
        if (imsLocationEntity.isPresent()) {
            imsLocationCode = imsLocationEntity.get().getImsLocationCode();
        }
        return imsLocationCode;
    }

    private ResponseEntity multipleRequestItemValidation(List<ItemEntity> itemEntityList, Set<Integer> bibliographicIds, ItemRequestInformation itemRequestInformation, Map<String, String> frozenInstitutionPropertyMap) {
        String status = "";
        List<BibliographicEntity> bibliographicList;

        for (ItemEntity itemEntity : itemEntityList) {
            if (!checkRequestItemStatus(itemEntity.getBarcode(), ScsbCommonConstants.REQUEST_STATUS_INITIAL_LOAD)) {
                return new ResponseEntity<>(ScsbConstants.INITIAL_LOAD_ITEM_EXISTS, getHttpHeaders(), HttpStatus.BAD_REQUEST);
            }
            if (Boolean.parseBoolean(frozenInstitutionPropertyMap.get(itemEntity.getInstitutionEntity().getInstitutionCode()))) {
                return new ResponseEntity<>(ScsbConstants.CIRCULATION_FREEZE_UNAVAILABLE_ITEM, getHttpHeaders(), HttpStatus.BAD_REQUEST);
            }
            if (itemEntity.getItemAvailabilityStatusId() == 2 && (itemRequestInformation.getRequestType().equalsIgnoreCase(ScsbCommonConstants.RETRIEVAL)
                    || itemRequestInformation.getRequestType().equalsIgnoreCase(ScsbCommonConstants.REQUEST_TYPE_EDD))) {
                return new ResponseEntity<>(ScsbConstants.INVALID_ITEM_BARCODE, getHttpHeaders(), HttpStatus.BAD_REQUEST);
            } else if (itemEntity.getItemAvailabilityStatusId() == 1 && itemRequestInformation.getRequestType().equalsIgnoreCase(ScsbCommonConstants.REQUEST_TYPE_RECALL)) {
                return new ResponseEntity<>(ScsbConstants.RECALL_NOT_FOR_AVAILABLE_ITEM, getHttpHeaders(), HttpStatus.BAD_REQUEST);
            }
            if (!checkRequestItemStatus(itemEntity.getBarcode(), ScsbCommonConstants.REQUEST_STATUS_RECALLED)) {
                return new ResponseEntity<>(ScsbConstants.RECALL_FOR_ITEM_EXISTS, getHttpHeaders(), HttpStatus.BAD_REQUEST);
            }

            if (!(itemRequestInformation.getRequestType().equalsIgnoreCase(ScsbCommonConstants.REQUEST_TYPE_EDD))) {
                int validateCustomerCode = checkDeliveryLocation(itemEntity.getCustomerCode(), itemRequestInformation);
                if (validateCustomerCode == 1) {
                 int validateDeliveryTranslationCode = checkDeliveryLocationTranslationCode(itemEntity, itemRequestInformation);
                    if (validateDeliveryTranslationCode == 1) {
                        if (itemEntity.getBibliographicEntities().size() == bibliographicIds.size()) {
                            bibliographicList = itemEntity.getBibliographicEntities();
                            for (BibliographicEntity bibliographicEntity : bibliographicList) {
                                Integer bibliographicId = bibliographicEntity.getId();
                                if (!bibliographicIds.contains(bibliographicId)) {
                                    return new ResponseEntity<>(ScsbConstants.ITEMBARCODE_WITH_DIFFERENT_BIB, getHttpHeaders(), HttpStatus.BAD_REQUEST);
                                } else {
                                    status = ScsbCommonConstants.VALID_REQUEST;
                                }
                            }
                        } else {
                            return new ResponseEntity<>(ScsbConstants.ITEMBARCODE_WITH_DIFFERENT_BIB, getHttpHeaders(), HttpStatus.BAD_REQUEST);
                        }
                    }
                    else {
                        if (validateDeliveryTranslationCode == 0) {
                            return new ResponseEntity<>(ScsbConstants.INVALID_CUSTOMER_CODE, getHttpHeaders(), HttpStatus.BAD_REQUEST);
                        } else if (validateDeliveryTranslationCode == -1) {
                            return new ResponseEntity<>(ScsbConstants.INVALID_TRANSLATED_CODE, getHttpHeaders(), HttpStatus.BAD_REQUEST);
                        }
                    }
                } else {
                    if (validateCustomerCode == 0) {
                        return new ResponseEntity<>(ScsbConstants.INVALID_CUSTOMER_CODE, getHttpHeaders(), HttpStatus.BAD_REQUEST);
                    } else if (validateCustomerCode == -1) {
                        return new ResponseEntity<>(ScsbConstants.INVALID_DELIVERY_CODE, getHttpHeaders(), HttpStatus.BAD_REQUEST);
                    }
                }
            }
        }

        /*

         */
        return new ResponseEntity<>(status, getHttpHeaders(), HttpStatus.OK);
    }

    /**
     * Check delivery location int.
     *
     * @param onwerCode           the ower code
     * @param itemRequestInformation the item request information
     * @return the int
     */
    public int checkDeliveryLocation(String onwerCode, ItemRequestInformation itemRequestInformation) {
        int bSuccess = 0;
        DeliveryCodeEntity deliveryCodeEntity = deliveryCodeDetailsRepository.findByDeliveryCode(itemRequestInformation.getDeliveryLocation());
        if (deliveryCodeEntity != null && deliveryCodeEntity.getDeliveryCode().equalsIgnoreCase(itemRequestInformation.getDeliveryLocation())) {
                OwnerCodeEntity onwerCodeEntity = onwerCodeDetailsRepository.findByOwnerCode(onwerCode);
                String requestingInstitution = itemRequestInformation.getRequestingInstitution();
                institutionDetailsRepository.findByInstitutionCode(requestingInstitution);

                List<Object[]> deliveryCodeEntityList = onwerCodeDetailsRepository.findByOwnerCodeAndRequestingInstitution(onwerCodeEntity.getId(), institutionDetailsRepository.findByInstitutionCode(requestingInstitution).getId(), itemRequestInformation.getDeliveryLocation());
                if (!deliveryCodeEntityList.isEmpty()) {
                            bSuccess = 1;
                        } else {
                            bSuccess = -1;
                        }

        }
        return bSuccess;
    }

    public int checkDeliveryLocationTranslationCode(ItemEntity itemEntity, ItemRequestInformation itemRequestInformation) {
        int bSuccess = -1;
        DeliveryCodeEntity deliveryCodeEntity = deliveryCodeDetailsRepository.findByDeliveryCode(itemRequestInformation.getDeliveryLocation());
        InstitutionEntity institutionEntity = institutionDetailsRepository.findByInstitutionCode(itemRequestInformation.getRequestingInstitution());
            DeliveryCodeTranslationEntity deliveryCodeTranslationEntity = deliveryCodeTranslationDetailsRepository.findByRequestingInstitutionandImsLocation(institutionEntity.getId(), deliveryCodeEntity.getId(), itemEntity.getImsLocationId());
            if (deliveryCodeTranslationEntity != null) {
                bSuccess = 1;
            } else {
                bSuccess = -1;
            }
        return bSuccess;
    }

    private boolean checkRequestItemStatus(String barcode, String requestItemStatus) {
        RequestItemEntity requestItemList = requestItemDetailsRepository.findByItemBarcodeAndRequestStaCode(barcode, requestItemStatus);
        return !(requestItemList != null && requestItemList.getId() > 0);
    }
}
