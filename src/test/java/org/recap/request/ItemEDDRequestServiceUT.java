package org.recap.request;

import org.apache.camel.Exchange;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.recap.BaseTestCaseUT;
import org.recap.RecapCommonConstants;
import org.recap.RecapConstants;
import org.recap.controller.RequestItemValidatorController;
import org.recap.ils.model.response.ItemInformationResponse;
import org.recap.model.jpa.*;
import org.recap.repository.jpa.GenericPatronDetailsRepository;
import org.recap.repository.jpa.ItemDetailsRepository;
import org.recap.repository.jpa.RequestTypeDetailsRepository;
import org.recap.util.ItemRequestServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

/**
 * Created by hemalathas on 17/2/17.
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class ItemEDDRequestServiceUT extends BaseTestCaseUT {

    private static final Logger logger = LoggerFactory.getLogger(ItemEDDRequestServiceUT.class);

    @InjectMocks
    ItemEDDRequestService itemEDDRequestService;

    @Mock
    Exchange exchange;

    @Mock
    GenericPatronDetailsRepository genericPatronDetailsRepository;

    @Mock
    ItemDetailsRepository itemDetailsRepository;

    @Mock
    ItemRequestServiceUtil itemRequestServiceUtil;

    @Mock
    private RequestTypeDetailsRepository requestTypeDetailsRepository;

    @Mock
    private RequestItemValidatorController requestItemValidatorController;

    @Mock
    private ItemRequestService itemRequestService;

    @Mock
    private GFAService gfaService;

    @Test
    public void testEddRequestItem() throws Exception {
        SearchResultRow searchResultRow = new SearchResultRow();
        searchResultRow.setTitle("Title Of the Book");
        ResponseEntity res = new ResponseEntity(RecapConstants.WRONG_ITEM_BARCODE, HttpStatus.CONTINUE);
        ItemRequestInformation itemRequestInfo = getItemRequestInformation();

        ItemInformationResponse itemResponseInformation1 = new ItemInformationResponse();
        itemResponseInformation1.setSuccess(true);

        ItemInformationResponse itemResponseInformation = new ItemInformationResponse();

        BibliographicEntity bibliographicEntity = saveBibSingleHoldingsSingleItem();
        ItemEntity itemEntity = bibliographicEntity.getItemEntities().get(0);
            Mockito.when(itemRequestService.searchRecords(any())).thenReturn(searchResultRow);
            Mockito.when(itemRequestService.removeDiacritical(searchResultRow.getTitle().replaceAll("[^\\x00-\\x7F]", "?"))).thenReturn("Title Of the Book");
            Mockito.when(itemRequestService.updateRecapRequestItem(itemRequestInfo, itemEntity, RecapConstants.REQUEST_STATUS_PROCESSING)).thenReturn(1);
//        Mockito.when(itemRequestService.updateRecapRequestItem(itemRequestInfo, itemEntity, RecapCommonConstants.REQUEST_STATUS_EDD)).thenReturn(1);
            Mockito.when(itemRequestService.searchRecords(itemEntity)).thenReturn(getSearchResultRowList());
            Mockito.when(itemRequestService.updateItemAvailabilutyStatus(List.of(itemEntity), itemRequestInfo.getUsername())).thenReturn(true);
            Mockito.when(itemRequestService.getGfaService()).thenReturn(gfaService);
            Mockito.when(gfaService.isUseQueueLasCall()).thenReturn(true);

//        Mockito.when(itemRequestService.getTitle(itemRequestInfo.getTitleIdentifier(), itemEntity, getSearchResultRowList())).thenCallRealMethod();
            Mockito.when(itemDetailsRepository.findByBarcodeIn(itemRequestInfo.getItemBarcodes())).thenReturn(bibliographicEntity.getItemEntities());
//            Mockito.when(itemRequestServiceUtil.getPatronIdBorrowingInstitution(itemRequestInfo.getRequestingInstitution(), itemRequestInfo.getItemOwningInstitution(), RecapCommonConstants.REQUEST_TYPE_EDD)).thenReturn("432765");
            Mockito.when(itemRequestService.updateGFA(any(), any())).thenReturn(itemResponseInformation);
            Mockito.doNothing().when(itemRequestService).sendMessageToTopic(any(), any(), any(), any());
            ItemInformationResponse itemInfoResponse = itemEDDRequestService.eddRequestItem(itemRequestInfo, exchange);
            assertNotNull(itemInfoResponse);
    }
    @Test
    public void testEddRequestItemwWithoutStatusAvailability() throws Exception {
        SearchResultRow searchResultRow = new SearchResultRow();
        searchResultRow.setTitle("Title Of the Book");
        ResponseEntity res = new ResponseEntity(RecapConstants.WRONG_ITEM_BARCODE, HttpStatus.CONTINUE);
        ItemRequestInformation itemRequestInfo = getItemRequestInformation();
        ItemInformationResponse itemResponseInformation = new ItemInformationResponse();
        itemResponseInformation.setSuccess(true);

        BibliographicEntity bibliographicEntity = saveBibSingleHoldingsSingleItem();
        ItemEntity itemEntity = bibliographicEntity.getItemEntities().get(0);
        Mockito.when(itemRequestService.searchRecords(any())).thenReturn(searchResultRow);
        Mockito.when(itemRequestService.removeDiacritical(searchResultRow.getTitle().replaceAll("[^\\x00-\\x7F]", "?"))).thenReturn("Title Of the Book");
//        Mockito.when(gfaService.isUseQueueLasCall()).thenReturn(false);
        Mockito.when(itemRequestService.updateRecapRequestItem(itemRequestInfo, itemEntity,RecapConstants.REQUEST_STATUS_PROCESSING)).thenReturn(1);
//        Mockito.when(itemRequestService.updateRecapRequestItem(itemRequestInfo, itemEntity, RecapCommonConstants.REQUEST_STATUS_EDD)).thenReturn(1);
        Mockito.when(itemRequestService.searchRecords(itemEntity)).thenReturn(getSearchResultRowList());
        Mockito.when(itemRequestService.updateItemAvailabilutyStatus(List.of(itemEntity), itemRequestInfo.getUsername())).thenReturn(false);
//        Mockito.when(itemRequestService.getGfaService()).thenReturn(gfaService);
//        Mockito.when(gfaService.isUseQueueLasCall()).thenReturn(true);
//        Mockito.when(itemRequestService.getTitle(itemRequestInfo.getTitleIdentifier(), itemEntity, getSearchResultRowList())).thenCallRealMethod();
        Mockito.when(itemDetailsRepository.findByBarcodeIn(itemRequestInfo.getItemBarcodes())).thenReturn(bibliographicEntity.getItemEntities());
//        Mockito.when(itemRequestService.updateGFA(itemRequestInfo, itemResponseInformation)).thenReturn(itemResponseInformation);
        ItemInformationResponse itemInfoResponse = itemEDDRequestService.eddRequestItem(itemRequestInfo, exchange);
        assertNotNull(itemInfoResponse);
        assertEquals(itemInfoResponse.getTitleIdentifier(), "Title Of the Book");
    }
    @Test
    public void testEddRequestItemWithoutItemEntity() throws Exception {
        SearchResultRow searchResultRow = new SearchResultRow();
        searchResultRow.setTitle("Title Of the Book");
        ResponseEntity res = new ResponseEntity(RecapConstants.WRONG_ITEM_BARCODE, HttpStatus.CONTINUE);
        ItemRequestInformation itemRequestInfo = getItemRequestInformation();
        ItemInformationResponse itemResponseInformation = new ItemInformationResponse();
        itemResponseInformation.setSuccess(true);
        Mockito.when(itemDetailsRepository.findByBarcodeIn(itemRequestInfo.getItemBarcodes())).thenReturn(null);
        Mockito.when(itemEDDRequestService.eddRequestItem(itemRequestInfo, exchange)).thenCallRealMethod();
        ItemInformationResponse itemInfoResponse = itemEDDRequestService.eddRequestItem(itemRequestInfo, exchange);
        assertNotNull(itemInfoResponse);
    }

    @Test
    public void testEddRequestItemWithoutRequestId() throws Exception {
        SearchResultRow searchResultRow = new SearchResultRow();
        searchResultRow.setTitle("Title Of the Book");
        ResponseEntity res = new ResponseEntity(RecapConstants.WRONG_ITEM_BARCODE, HttpStatus.CONTINUE);
        ItemRequestInformation itemRequestInfo = getItemRequestInformation();
        ItemInformationResponse itemResponseInformation = new ItemInformationResponse();
        itemResponseInformation.setSuccess(true);
        BibliographicEntity bibliographicEntity = saveBibSingleHoldingsSingleItem();
        ItemEntity itemEntity = bibliographicEntity.getItemEntities().get(0);
        Mockito.when(itemRequestService.searchRecords(any())).thenReturn(searchResultRow);
        Mockito.when(itemRequestService.removeDiacritical(searchResultRow.getTitle().replaceAll("[^\\x00-\\x7F]", "?"))).thenReturn("Title Of the Book");
//        Mockito.when(itemRequestService.getGfaService()).thenReturn(gfaService);
//        Mockito.when(gfaService.isUseQueueLasCall()).thenReturn(false);
//        Mockito.when(itemRequestService.updateRecapRequestItem(itemRequestInfo, itemEntity, RecapCommonConstants.REQUEST_STATUS_EDD)).thenReturn(1);
        Mockito.when(itemRequestService.searchRecords(itemEntity)).thenReturn(getSearchResultRowList());
//        Mockito.when(itemRequestService.getTitle(itemRequestInfo.getTitleIdentifier(), itemEntity, getSearchResultRowList())).thenCallRealMethod();
        Mockito.when(itemDetailsRepository.findByBarcodeIn(itemRequestInfo.getItemBarcodes())).thenReturn(bibliographicEntity.getItemEntities());
//        Mockito.when(itemRequestService.updateGFA(itemRequestInfo, itemResponseInformation)).thenReturn(itemResponseInformation);
        ItemInformationResponse itemInfoResponse = itemEDDRequestService.eddRequestItem(itemRequestInfo, exchange);
        assertNotNull(itemInfoResponse);
        assertEquals(itemInfoResponse.getTitleIdentifier(), "Title Of the Book");
    }

    private ItemRequestInformation getItemRequestInformation() {
        ItemRequestInformation itemRequestInfo = new ItemRequestInformation();
        itemRequestInfo.setItemBarcodes(Arrays.asList("23"));
        itemRequestInfo.setItemOwningInstitution("PUL");
        itemRequestInfo.setExpirationDate(new Date().toString());
        itemRequestInfo.setRequestingInstitution("PUL");
        itemRequestInfo.setTitleIdentifier("titleIdentifier");
        itemRequestInfo.setPatronBarcode("4356234");
        itemRequestInfo.setBibId("1");
        itemRequestInfo.setDeliveryLocation("PB");
        itemRequestInfo.setEmailAddress("hemalatha.s@htcindia.com");
        itemRequestInfo.setRequestType("Recall");
        itemRequestInfo.setRequestNotes("Test");
        return itemRequestInfo;
    }

    @Test
    public void getPatronIdForOwningInstitutionOnEdd(){
        GenericPatronEntity genericPatronEntity = getGenericPatronEntity();
        Mockito.when(genericPatronDetailsRepository.findByItemOwningInstitutionCode(any())).thenReturn(genericPatronEntity);
        itemEDDRequestService.getPatronIdForOwningInstitutionOnEdd(RecapCommonConstants.PRINCETON);
        itemEDDRequestService.getPatronIdForOwningInstitutionOnEdd(RecapCommonConstants.COLUMBIA);
        itemEDDRequestService.getPatronIdForOwningInstitutionOnEdd(RecapCommonConstants.NYPL);
    }

    private GenericPatronEntity getGenericPatronEntity() {
        GenericPatronEntity genericPatronEntity = new GenericPatronEntity();
        genericPatronEntity.setGenericPatronId(1);
        genericPatronEntity.setEddGenericPatron("edd");
        genericPatronEntity.setCreatedBy("test");
        genericPatronEntity.setCreatedDate(new Date());
        genericPatronEntity.setRetrievalGenericPatron("retrieve");
        genericPatronEntity.setItemOwningInstitutionId(1);
        genericPatronEntity.setOwningInstitutionEntity(new InstitutionEntity());
        genericPatronEntity.setRequestingInstitutionEntity(new InstitutionEntity());
        genericPatronEntity.setRequestingInstitutionId(1);
        genericPatronEntity.setUpdatedBy("test");
        genericPatronEntity.setUpdatedDate(new Date());
        return genericPatronEntity;
    }
    public BibliographicEntity saveBibSingleHoldingsSingleItem() throws Exception {

        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setId(1);
        institutionEntity.setInstitutionCode("UC");
        institutionEntity.setInstitutionName("University of Chicago");
        assertNotNull(institutionEntity);

        Random random = new Random();
        BibliographicEntity bibliographicEntity = new BibliographicEntity();
        bibliographicEntity.setContent("mock Content".getBytes());
        bibliographicEntity.setCreatedDate(new Date());
        bibliographicEntity.setLastUpdatedDate(new Date());
        bibliographicEntity.setCreatedBy("tst");
        bibliographicEntity.setLastUpdatedBy("tst");
        bibliographicEntity.setOwningInstitutionId(institutionEntity.getId());
        bibliographicEntity.setOwningInstitutionBibId("");
        HoldingsEntity holdingsEntity = new HoldingsEntity();
        holdingsEntity.setContent("mock holdings".getBytes());
        holdingsEntity.setCreatedDate(new Date());
        holdingsEntity.setLastUpdatedDate(new Date());
        holdingsEntity.setCreatedBy("tst");
        holdingsEntity.setLastUpdatedBy("tst");
        holdingsEntity.setOwningInstitutionId(1);
        holdingsEntity.setOwningInstitutionHoldingsId(String.valueOf(random.nextInt()));

        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setLastUpdatedDate(new Date());
        itemEntity.setOwningInstitutionItemId(String.valueOf(random.nextInt()));
        itemEntity.setOwningInstitutionId(1);
        itemEntity.setBarcode("0235");
        itemEntity.setCallNumber("x.12321");
        itemEntity.setCollectionGroupId(1);
        itemEntity.setCallNumberType("1");
        itemEntity.setCustomerCode("123");
        itemEntity.setCreatedDate(new Date());
        itemEntity.setCreatedBy("tst");
        itemEntity.setLastUpdatedBy("tst");
        itemEntity.setItemAvailabilityStatusId(1);
        itemEntity.setHoldingsEntities(Arrays.asList(holdingsEntity));
        itemEntity.setCatalogingStatus("Completed");
        itemEntity.setBibliographicEntities(Arrays.asList(bibliographicEntity));
        itemEntity.setInstitutionEntity(institutionEntity);

        bibliographicEntity.setHoldingsEntities(Arrays.asList(holdingsEntity));
        bibliographicEntity.setItemEntities(Arrays.asList(itemEntity));

        return bibliographicEntity;

    }

    public SearchResultRow getSearchResultRowList() {
        SearchResultRow searchItemResultRow = new SearchResultRow();
        searchItemResultRow.setTitle("Title Of the Book");
        searchItemResultRow.setAuthor("AuthorBook");
        searchItemResultRow.setCustomerCode("PB");
        searchItemResultRow.setBarcode("123");
        searchItemResultRow.setAvailability("Available");
        searchItemResultRow.setCollectionGroupDesignation("Shared");
        searchItemResultRow.setItemId(1);
        return searchItemResultRow;
    }

}