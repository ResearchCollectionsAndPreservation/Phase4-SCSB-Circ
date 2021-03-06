package org.recap.ils.protocol.rest.model.response;

import org.junit.Test;
import org.recap.ils.protocol.rest.model.DebugInfo;
import org.recap.ils.protocol.rest.model.ItemData;
import org.recap.ils.protocol.rest.model.response.ItemResponse;

import java.util.Arrays;

import static org.junit.Assert.assertNotNull;

public class ItemResponseUT {

    @Test
    public void getItemResponse(){
        ItemResponse itemResponse = new ItemResponse();
        itemResponse.setCount(1);
        itemResponse.setDebugInfo(Arrays.asList(new DebugInfo()));
        itemResponse.setItemData(new ItemData());
        itemResponse.setStatusCode(1);

        assertNotNull(itemResponse.getCount());
        assertNotNull(itemResponse.getDebugInfo());
        assertNotNull(itemResponse.getItemData());
        assertNotNull(itemResponse.getStatusCode());
    }
}
