package org.recap.ncip;

import org.extensiblecatalog.ncip.v2.service.*;
import org.json.JSONObject;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.recap.BaseTestCaseUT;

import java.util.Arrays;
import java.util.Collections;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertNotNull;

public class CheckoutItemUT extends BaseTestCaseUT {

    @InjectMocks
    CheckoutItem checkoutItem;

    @Test
    public void getCheckoutResponse() {
        CheckOutItemResponseData checkOutItemResponseData = getCheckOutItemResponseData();
        JSONObject returnJson = checkoutItem.getCheckoutResponse(checkOutItemResponseData);
        assertNotNull(returnJson);
    }

    @Test
    public void getCheckoutResponseWithoutProblems() {
        CheckOutItemResponseData checkOutItemResponseData = getCheckOutItemResponseData();
        checkOutItemResponseData.setProblems(Collections.EMPTY_LIST);
        JSONObject returnJson = checkoutItem.getCheckoutResponse(checkOutItemResponseData);
        assertNotNull(returnJson);
    }

    @Test
    public void getCheckOutItemInitiationDataWithoutRequestId() {
        CheckOutItemInitiationData data = checkoutItem.getCheckOutItemInitiationData(null,null,null,null);
        assertNotNull(data);
    }

    private CheckOutItemResponseData getCheckOutItemResponseData() {
        CheckOutItemResponseData checkOutItemResponseData = new CheckOutItemResponseData();
        ItemId itemId = new ItemId();
        itemId.setItemIdentifierValue("24365");
        Problem problem = getProblem();
        UserId userId = new UserId();
        userId.setUserIdentifierValue("234556");
        checkOutItemResponseData.setItemId(itemId);
        checkOutItemResponseData.setProblems(Arrays.asList(problem));
        checkOutItemResponseData.setUserId(userId);
        checkOutItemResponseData.setDateDue(new GregorianCalendar());
        return checkOutItemResponseData;
    }

    private Problem getProblem() {
        Problem problem = new Problem();
        ProblemType problemType = new ProblemType("43656");
        problem.setProblemType(problemType);
        problem.setProblemDetail("Bad Request");
        problem.setProblemValue("43656");
        problem.setProblemElement("Error");
        return problem;
    }
}
