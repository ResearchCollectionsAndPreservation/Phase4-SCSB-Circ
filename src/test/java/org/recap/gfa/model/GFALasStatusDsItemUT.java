package org.recap.gfa.model;

import org.junit.Test;
import org.recap.BaseTestCaseUT;

import java.util.Arrays;

public class GFALasStatusDsItemUT extends BaseTestCaseUT {

    @Test
    public void getGFALasStatusDsItem(){
        GFALasStatusDsItem gfaLasStatusDsItem = new GFALasStatusDsItem();
        GFALasStatusDsItem gfaLasStatusDsItem1 = new GFALasStatusDsItem();
        gfaLasStatusDsItem.setTtitem(Arrays.asList(new GFALasStatusTtItem()));
        gfaLasStatusDsItem.toString();
        gfaLasStatusDsItem.canEqual(gfaLasStatusDsItem1);
        gfaLasStatusDsItem.canEqual(gfaLasStatusDsItem);
        gfaLasStatusDsItem.equals(gfaLasStatusDsItem1);
        gfaLasStatusDsItem.equals(gfaLasStatusDsItem);
        gfaLasStatusDsItem1.equals(gfaLasStatusDsItem);
        gfaLasStatusDsItem.hashCode();
        gfaLasStatusDsItem1.hashCode();
    }
}
