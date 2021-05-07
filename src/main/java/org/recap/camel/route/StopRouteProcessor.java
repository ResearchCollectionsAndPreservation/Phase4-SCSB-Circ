package org.recap.camel.route;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.recap.ScsbConstants;
import org.recap.ScsbCommonConstants;
import org.slf4j.LoggerFactory;

/**
 * The type Stop route processor.
 */
public class StopRouteProcessor implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(StopRouteProcessor.class);
    private String routeId;

    /**
     * Instantiates a new Stop route processor.
     *
     * @param routeId the route id
     */
    public StopRouteProcessor(String routeId) {
        this.routeId = routeId;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Thread stopThread;
        stopThread = new Thread() {
            @Override
            public void run() {
                try {
                    if (routeId.contains(ScsbConstants.REQUEST_INITIAL_LOAD_FTP_ROUTE)) {
                        stopRouteWithTimeOutOption();
                    } else {
                        exchange.getContext().getRouteController().stopRoute(routeId);
                    }
                    logger.info("Stop Route {}" , routeId);
                } catch (Exception e) {
                    logger.error("Exception while stop route : {}" , routeId);
                    logger.error(ScsbCommonConstants.LOG_ERROR , e);

                }
            }

            private void stopRouteWithTimeOutOption() throws Exception {
                exchange.getContext().getShutdownStrategy().setTimeout(1);
                exchange.getContext().getRouteController().stopRoute(routeId);
            }
        };
        stopThread.start();
    }
}

