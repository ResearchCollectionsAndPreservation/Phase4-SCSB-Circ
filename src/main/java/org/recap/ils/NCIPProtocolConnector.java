package org.recap.ils;

import lombok.extern.slf4j.Slf4j;
import org.recap.ils.model.ILSConfigProperties;
import org.recap.model.AbstractResponseItem;

@Slf4j
public class NCIPProtocolConnector extends AbstractProtocolConnector {

    public NCIPProtocolConnector(ILSConfigProperties ilsConfigProperties) {
        super(ilsConfigProperties);
    }

    @Override
    public AbstractResponseItem lookupItem(String itemIdentifier) {
        log.info("NCIP Connector Host: {}", getHost());
        log.info("NCIP Connector Port: {}", getPort());
        log.info("NCIP Connector Location: {}", getOperatorLocation());
        return null;
    }

    @Override
    public Object checkOutItem(String itemIdentifier, String patronIdentifier) {
        return null;
    }

    @Override
    public Object checkInItem(String itemIdentifier, String patronIdentifier) {
        return null;
    }

    @Override
    public Object placeHold(String itemIdentifier, String patronIdentifier, String callInstitutionId, String itemInstitutionId, String expirationDate, String bibId, String pickupLocation, String trackingId, String title, String author, String callNumber) {
        return null;
    }

    @Override
    public Object cancelHold(String itemIdentifier, String patronIdentifier, String institutionId, String expirationDate, String bibId, String pickupLocation, String trackingId) {
        return null;
    }

    @Override
    public Object createBib(String itemIdentifier, String patronIdentifier, String institutionId, String titleIdentifier) {
        return null;
    }

    @Override
    public boolean patronValidation(String institutionId, String patronIdentifier) {
        return false;
    }

    @Override
    public AbstractResponseItem lookupPatron(String patronIdentifier) {
        return null;
    }

    @Override
    public Object recallItem(String itemIdentifier, String patronIdentifier, String institutionId, String expirationDate, String bibId, String pickupLocation) {
        return null;
    }

    @Override
    public Object refileItem(String itemIdentifier) {
        return null;
    }
}
