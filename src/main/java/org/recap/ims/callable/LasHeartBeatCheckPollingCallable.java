package org.recap.ims.callable;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.recap.ims.connector.factory.LASImsLocationConnectorFactory;
import org.recap.ims.model.GFALasStatus;
import org.recap.ims.model.GFALasStatusCheckRequest;
import org.recap.ims.model.GFALasStatusCheckResponse;
import java.util.Collections;
import java.util.concurrent.Callable;

/**
 * Created by rajeshbabuk on 25/Nov/2020
 */
@Slf4j
public class LasHeartBeatCheckPollingCallable implements Callable {

    private String imsLocationCode;
    private LASImsLocationConnectorFactory lasImsLocationConnectorFactory;
    private Integer pollingTimeInterval;

    public LasHeartBeatCheckPollingCallable(Integer pollingTimeInterval, LASImsLocationConnectorFactory lasImsLocationConnectorFactory, String imsLocationCode) {
        this.imsLocationCode = imsLocationCode;
        this.lasImsLocationConnectorFactory = lasImsLocationConnectorFactory;
        this.pollingTimeInterval = pollingTimeInterval;
    }

    @Override
    public GFALasStatusCheckResponse call() throws Exception {
        return poll();
    }

    private GFALasStatusCheckResponse poll() throws Exception {
        GFALasStatusCheckResponse gfaLasStatusCheckResponse = null;
        GFALasStatusCheckRequest gfaLasStatusCheckRequest = new GFALasStatusCheckRequest();
        GFALasStatus lasStatus = new GFALasStatus();
        lasStatus.setImsLocationCode(imsLocationCode);
        gfaLasStatusCheckRequest.setLasStatus(Collections.singletonList(lasStatus));
        try {
            gfaLasStatusCheckResponse = lasImsLocationConnectorFactory.getLasImsLocationConnector(imsLocationCode).heartBeatCheck(gfaLasStatusCheckRequest);
            if (null == gfaLasStatusCheckResponse
                    || null == gfaLasStatusCheckResponse.getDsitem()
                    || null == gfaLasStatusCheckResponse.getDsitem().getTtitem()
                    || gfaLasStatusCheckResponse.getDsitem().getTtitem().isEmpty()
                    || !BooleanUtils.toBoolean(gfaLasStatusCheckResponse.getDsitem().getTtitem().get(0).getSuccess())) {
                Thread.sleep(pollingTimeInterval);
                gfaLasStatusCheckResponse = poll();
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return gfaLasStatusCheckResponse;
    }
}
