package org.recap.gfa.model;

import org.junit.Test;
import org.recap.BaseTestCaseUT;

import java.util.Arrays;

public class RetrieveItemRequestUT extends BaseTestCaseUT {
    @Test
    public void getRetrieveItemRequest(){
        RetrieveItemRequest retrieveItemRequest = new RetrieveItemRequest();
        RetrieveItemRequest retrieveItemRequest1 = new RetrieveItemRequest();
        retrieveItemRequest.setTtitem(Arrays.asList(new TtitemRequest()));
        retrieveItemRequest.equals(retrieveItemRequest);
        retrieveItemRequest1.equals(retrieveItemRequest);
        retrieveItemRequest.equals(retrieveItemRequest1);
        retrieveItemRequest.hashCode();
        retrieveItemRequest1.hashCode();
        retrieveItemRequest.canEqual(retrieveItemRequest);
        retrieveItemRequest.canEqual(retrieveItemRequest1);
        retrieveItemRequest.toString();
    }

}
