package org.recap.request;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.recap.RecapCommonConstants;
import org.recap.RecapConstants;
import org.recap.controller.ItemController;
import org.recap.model.jpa.ItemRequestInformation;
import org.recap.repository.jpa.InstitutionDetailsRepository;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by hemalathas on 3/11/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class RequestParamaterValidatorServiceUT{

    @InjectMocks
    RequestParamaterValidatorService requestParamaterValidatorService;

    @Mock
    ItemController itemController;

    @Mock
    InstitutionDetailsRepository institutionDetailsRepository;

    @Test
    public void testForValidatingInvalidRequestingInstitution(){
        ItemRequestInformation itemRequestInformation = getItemRequestInformation();
        itemRequestInformation.setEmailAddress("hemalatha.s@htcindia.com");
        Mockito.when(institutionDetailsRepository.existsByInstitutionCode(itemRequestInformation.getRequestingInstitution())).thenReturn(true);
        ResponseEntity responseEntity = requestParamaterValidatorService.validateItemRequestParameters(itemRequestInformation);
        assertNotNull(responseEntity);
    }
    @Test
    public void testForValidatingInvalidRequestingInstitutionWithBarcode(){
        ItemRequestInformation itemRequestInformation = getItemRequestInformation();
        itemRequestInformation.setRequestType(RecapConstants.EDD_REQUEST);
        ResponseEntity responseEntity = requestParamaterValidatorService.validateItemRequestParameters(itemRequestInformation);
        assertNotNull(responseEntity);
        itemRequestInformation.setEmailAddress("");
        itemRequestInformation.setRequestType(RecapCommonConstants.REQUEST_TYPE_RECALL);
        Mockito.when(institutionDetailsRepository.existsByInstitutionCode(itemRequestInformation.getRequestingInstitution())).thenReturn(true);
        ResponseEntity responseEntity1 = requestParamaterValidatorService.validateItemRequestParameters(itemRequestInformation);
        assertNotNull(responseEntity1);
    }
    private ItemRequestInformation getItemRequestInformation() {

        ItemRequestInformation itemRequestInformation = new ItemRequestInformation();
        itemRequestInformation.setPatronBarcode("45678915");
        itemRequestInformation.setRequestType(RecapCommonConstants.REQUEST_TYPE_BORROW_DIRECT);
        itemRequestInformation.setRequestingInstitution("PUL");
        return itemRequestInformation;
    }

    @Test
    public void testForValidatingInvalidEmailAddress(){
        ItemRequestInformation itemRequestInformation = new ItemRequestInformation();
        List<String> itemBarcodeList = new ArrayList<>();
        itemBarcodeList.add("33433014514719");
        itemBarcodeList.add("33433012968222");
        itemRequestInformation.setItemBarcodes(itemBarcodeList);
        itemRequestInformation.setPatronBarcode("45678915");
        itemRequestInformation.setRequestType(RecapCommonConstants.REQUEST_TYPE_BORROW_DIRECT);
        itemRequestInformation.setRequestingInstitution("PUL");
        itemRequestInformation.setEmailAddress("hemalatha.shtcindia.com");
        Mockito.when(institutionDetailsRepository.existsByInstitutionCode(itemRequestInformation.getRequestingInstitution())).thenReturn(true);
        ResponseEntity responseEntity = requestParamaterValidatorService.validateItemRequestParameters(itemRequestInformation);
        assertNotNull(responseEntity);
        assertEquals(responseEntity.getBody(), RecapConstants.INVALID_EMAIL_ADDRESS+"\n");
    }

    @Test
    public void testForValidatingInvalidRequestType(){
        ItemRequestInformation itemRequestInformation = new ItemRequestInformation();
        List<String> itemBarcodeList = new ArrayList<>();
        itemBarcodeList.add("33433014514719");
        itemBarcodeList.add("33433012968222");
        itemRequestInformation.setItemBarcodes(itemBarcodeList);
        itemRequestInformation.setPatronBarcode("45678915");
        itemRequestInformation.setRequestType(null);
        itemRequestInformation.setRequestingInstitution("PUL");
        itemRequestInformation.setEmailAddress("hemalatha.s@htcindia.com");
        Mockito.when(institutionDetailsRepository.existsByInstitutionCode(itemRequestInformation.getRequestingInstitution())).thenReturn(true);
        ResponseEntity responseEntity = requestParamaterValidatorService.validateItemRequestParameters(itemRequestInformation);
        assertNotNull(responseEntity);
        assertEquals(responseEntity.getBody(), RecapConstants.INVALID_REQUEST_TYPE+"\n");
    }

    @Test
    public void testForValidatingEDDRequestType(){
        ItemRequestInformation itemRequestInformation = new ItemRequestInformation();
        List<String> itemBarcodeList = new ArrayList<>();
        itemBarcodeList.add("33433014514719");
        itemRequestInformation.setItemBarcodes(itemBarcodeList);
        itemRequestInformation.setPatronBarcode("45678915");
        itemRequestInformation.setChapterTitle("title");
        itemRequestInformation.setRequestType(RecapConstants.EDD_REQUEST);
        itemRequestInformation.setDeliveryLocation("AC");
        itemRequestInformation.setRequestingInstitution("PUL");
        itemRequestInformation.setEmailAddress("hemalatha.s@htcindia.com");
        itemRequestInformation.setStartPage("0");
        itemRequestInformation.setEndPage("0");
        Mockito.when(institutionDetailsRepository.existsByInstitutionCode(itemRequestInformation.getRequestingInstitution())).thenReturn(true);
        ResponseEntity responseEntity = requestParamaterValidatorService.validateItemRequestParameters(itemRequestInformation);
        assertNull(responseEntity);
    }







}