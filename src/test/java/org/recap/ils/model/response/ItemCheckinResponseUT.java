package org.recap.ils.model.response;

import org.junit.Test;
import org.recap.BaseTestCaseUT;

import java.util.Date;

import static org.junit.Assert.assertNotNull;

/**
 * Created by hemalathas on 3/4/17.
 */
public class ItemCheckinResponseUT extends BaseTestCaseUT {

    @Test
    public void testItemCheckinResponse(){
        ItemCheckinResponse itemCheckinResponse = new ItemCheckinResponse();
        itemCheckinResponse.setAlert(false);
        itemCheckinResponse.setMagneticMedia(true);
        itemCheckinResponse.setResensitize(true);
        itemCheckinResponse.setTransactionDate(new Date().toString());
        itemCheckinResponse.setInstitutionID("1");
        itemCheckinResponse.setPatronIdentifier("45213436588");
        itemCheckinResponse.setTitleIdentifier("test");
        itemCheckinResponse.setDueDate(new Date().toString());
        itemCheckinResponse.setFeeAmount("156");
        itemCheckinResponse.setMediaType("test");
        itemCheckinResponse.setBibId("1");
        itemCheckinResponse.setIsbn("145345");
        itemCheckinResponse.setLccn("454558");
        itemCheckinResponse.setPermanentLocation("test");
        itemCheckinResponse.setSortBin("test");
        itemCheckinResponse.setCollectionCode("test");
        itemCheckinResponse.setCallNumber("X");
        itemCheckinResponse.setDestinationLocation("CUL");
        itemCheckinResponse.setAlertType("test");
        itemCheckinResponse.setHoldPatronId("1");
        itemCheckinResponse.setHoldPatronName("test");
        itemCheckinResponse.setJobId("1");
        itemCheckinResponse.setFeeType("test");
        itemCheckinResponse.setProcessed(true);
        itemCheckinResponse.setUpdatedDate(new Date().toString());
        itemCheckinResponse.setCreatedDate(new Date().toString());
        itemCheckinResponse.setSecurityInhibit("test");
        itemCheckinResponse.setCurrencyType("test");

        assertNotNull(itemCheckinResponse.getAlert());
        assertNotNull(itemCheckinResponse.getPermanentLocation());
        assertNotNull(itemCheckinResponse.getSortBin());
        assertNotNull(itemCheckinResponse.getCollectionCode());
        assertNotNull(itemCheckinResponse.getCallNumber());
        assertNotNull(itemCheckinResponse.getDestinationLocation());
        assertNotNull(itemCheckinResponse.getAlertType());
        assertNotNull(itemCheckinResponse.getHoldPatronId());
        assertNotNull(itemCheckinResponse.getHoldPatronName());
        assertNotNull(itemCheckinResponse.getMagneticMedia());
        assertNotNull(itemCheckinResponse.getResensitize());
        assertNotNull(itemCheckinResponse.getTransactionDate());
        assertNotNull(itemCheckinResponse.getInstitutionID());
        assertNotNull(itemCheckinResponse.getPatronIdentifier());
        assertNotNull(itemCheckinResponse.getTitleIdentifier());
        assertNotNull(itemCheckinResponse.getDueDate());
        assertNotNull(itemCheckinResponse.getFeeType());
        assertNotNull(itemCheckinResponse.getSecurityInhibit());
        assertNotNull(itemCheckinResponse.getCurrencyType());
        assertNotNull(itemCheckinResponse.getFeeAmount());
        assertNotNull(itemCheckinResponse.getMediaType());
        assertNotNull(itemCheckinResponse.getBibId());
        assertNotNull(itemCheckinResponse.getIsbn());
        assertNotNull(itemCheckinResponse.getLccn());
        assertNotNull(itemCheckinResponse.getJobId());
        assertNotNull(itemCheckinResponse.isProcessed());
        assertNotNull(itemCheckinResponse.getUpdatedDate());
        assertNotNull(itemCheckinResponse.getCreatedDate());
    }

}