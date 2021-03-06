package org.recap.ils.protocol.rest.model.response;

import org.junit.Test;
import org.recap.BaseTestCaseUT;
import org.recap.ils.protocol.rest.model.DebugInfo;
import org.recap.ils.protocol.rest.model.response.CheckoutResponse;

import java.util.Arrays;

import static org.junit.Assert.assertNotNull;

public class CheckoutResponseUT extends BaseTestCaseUT {

    @Test
    public void testCheckinResponse() {
        CheckoutResponse checkoutResponse = new CheckoutResponse();
        checkoutResponse.setCount(1);
        checkoutResponse.setStatusCode(1);
        checkoutResponse.setDebugInfo(Arrays.asList(new DebugInfo()));
        assertNotNull(checkoutResponse.getCount());
        assertNotNull(checkoutResponse.getStatusCode());
        assertNotNull(checkoutResponse.getDebugInfo());
    }
}
