package org.recap.las.model;

import org.junit.Test;
import org.recap.BaseTestCaseUT;
import org.recap.las.model.TtitemRequest;

import static org.junit.Assert.assertNotNull;


public class TtitemRequestUT extends BaseTestCaseUT {

    @Test
    public void testTtitemRequest() {
        TtitemRequest ititemRequest = new TtitemRequest();
        TtitemRequest ititemRequest1 = new TtitemRequest();
        ititemRequest.setRequestId("test");
        ititemRequest.setRequestor("test");
        ititemRequest.setCustomerCode("2356");
        ititemRequest.setItemBarcode("245778");
        ititemRequest.setItemStatus("Complete");
        ititemRequest.setDestination("PA");
        ititemRequest.equals(ititemRequest);
        ititemRequest.equals(ititemRequest1);
        ititemRequest1.equals(ititemRequest);
        ititemRequest.hashCode();
        ititemRequest1.hashCode();


        assertNotNull(ititemRequest.getRequestId());
        assertNotNull(ititemRequest.getRequestor());
        assertNotNull(ititemRequest.getCustomerCode());
        assertNotNull(ititemRequest.getDestination());
        assertNotNull(ititemRequest.getItemBarcode());
        assertNotNull(ititemRequest.getItemStatus());
    }
}
