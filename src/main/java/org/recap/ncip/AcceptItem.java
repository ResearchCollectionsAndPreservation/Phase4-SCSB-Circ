package org.recap.ncip;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.extensiblecatalog.ncip.v2.service.AcceptItemInitiationData;
import org.extensiblecatalog.ncip.v2.service.AcceptItemResponseData;
import org.extensiblecatalog.ncip.v2.service.BibliographicDescription;
import org.extensiblecatalog.ncip.v2.service.InitiationHeader;
import org.extensiblecatalog.ncip.v2.service.ItemDescription;
import org.extensiblecatalog.ncip.v2.service.ItemId;
import org.extensiblecatalog.ncip.v2.service.ItemOptionalFields;
import org.extensiblecatalog.ncip.v2.service.PickupLocation;
import org.extensiblecatalog.ncip.v2.service.RequestId;
import org.extensiblecatalog.ncip.v2.service.RequestedActionType;
import org.extensiblecatalog.ncip.v2.service.UserId;
import org.json.JSONObject;
import org.recap.ScsbConstants;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

@Slf4j
@Getter
@Setter
public class AcceptItem extends ScsbNCIP {

    private String requestIdString;
    private String useridString;
    private String itemIdString;
    private String pickupLocationString;
    protected String toAgency;
    protected String fromAgency;
    private String requestedActionTypeString;
    private String applicationProfileTypeString;
    private HashMap<String, HashMap<String, String>> itemOptionalFields = new HashMap<>();

    public AcceptItem() {
        itemOptionalFields.put(ScsbConstants.BIBLIOGRAPHIC_DESCRIPTION, new HashMap<>());
        itemOptionalFields.put(ScsbConstants.ITEM_DESCRIPTION, new HashMap<>());
    }

    public AcceptItem setRequestActionType(String action) {
        requestedActionTypeString = action;
        return this;
    }

    public AcceptItem setApplicationProfileType(String profileType) {
        applicationProfileTypeString = profileType;
        return this;
    }

    public AcceptItem setItemId(String itemId) {
        itemIdString = itemId;
        return this;
    }

    public AcceptItem setRequestId(String requestId) {
        requestIdString = requestId;
        return this;
    }

    public AcceptItem setUserId(String userId) {
        useridString = userId;
        return this;
    }

    public AcceptItem setPickupLocation(String pickupLocation) {
        pickupLocationString = pickupLocation;
        return this;
    }

    public AcceptItem addBibliographicDescription(String bibliographicDescriptionType, String value) {
        itemOptionalFields.get(ScsbConstants.BIBLIOGRAPHIC_DESCRIPTION).put(bibliographicDescriptionType, value);
        return this;
    }

    public AcceptItem addItemDescription(String itemDescriptionType, String value) {
        itemOptionalFields.get(ScsbConstants.ITEM_DESCRIPTION).put(itemDescriptionType, value);
        return this;
    }

    public AcceptItem setToAgency(String toAgency) {
        this.toAgency = toAgency;
        return this;
    }

    public AcceptItem setFromAgency(String fromAgency) {
        this.fromAgency = fromAgency;
        return this;
    }

    public AcceptItem setRequestedActionTypeString(String actionType) {
        this.requestedActionTypeString = actionType;
        return this;
    }

    // Convenience methods
    public AcceptItem setTitle(String title) {
        return setInfo(ScsbConstants.TITLE, title);
    }

    public AcceptItem setAuthor(String author) {
        return setInfo(ScsbConstants.AUTHOR, author);
    }

    public AcceptItem setPublisher(String publisher) {
        return setInfo(ScsbConstants.PUBLISHER, publisher);
    }

    public AcceptItem setPublicationDate(String pubDate) {
        return setInfo(ScsbConstants.PUBLICATION_DATE, pubDate);
    }

    private AcceptItem setInfo(String var, String info) {
        this.itemOptionalFields.get(ScsbConstants.BIBLIOGRAPHIC_DESCRIPTION).put(var, info);
        return this;
    }

    public AcceptItem setIsbn(String isbn) {
        return setInfo(ScsbConstants.ISBN, isbn);
    }

    public AcceptItem setIssn(String issn) {
        return setInfo(ScsbConstants.ISSN, issn);
    }

    public AcceptItem setCallNumber(String callNumber) {
        this.itemOptionalFields.get(ScsbConstants.ITEM_DESCRIPTION).put(ScsbConstants.CALL_NUMBER, callNumber);
        return this;
    }

    public String getAuthor() {
        return this.itemOptionalFields.get(ScsbConstants.BIBLIOGRAPHIC_DESCRIPTION).get(ScsbConstants.AUTHOR);
    }

    public String getTitle() {
        return this.itemOptionalFields.get(ScsbConstants.BIBLIOGRAPHIC_DESCRIPTION).get(ScsbConstants.TITLE);
    }

    public String getCallNo() {
        return this.itemOptionalFields.get(ScsbConstants.ITEM_DESCRIPTION).get(ScsbConstants.CALL_NUMBER);
    }

    public AcceptItemInitiationData getAcceptItemInitiationData(String itemIdentifier, Integer requestId, String patronIdentifier, String title, String author, String itemPickupLocation, String callNumber, String ncipAgencyId, String ncipScheme)  {
        AcceptItemInitiationData acceptItemInitationData = new AcceptItemInitiationData();
        InitiationHeader initiationHeader = new InitiationHeader();
        initiationHeader = getInitiationHeaderwithScheme(initiationHeader, ncipScheme, ScsbConstants.AGENCY_ID_SCSB, ncipAgencyId);
        acceptItemInitationData.setInitiationHeader(initiationHeader);
        RequestId requestIdentifier = new RequestId();
        if(requestId != null) {
            requestIdentifier.setRequestIdentifierValue(requestId.toString());
        }
        else {
            requestIdentifier.setRequestIdentifierValue(Integer.toString(RandomUtils.nextInt(100000,100000000)));
        }
        RequestedActionType requestActionType = new RequestedActionType(null, "Hold For Pickup");
        UserId userid = new UserId();
        userid.setUserIdentifierValue(patronIdentifier);
        ItemId itemId = new ItemId();
        itemId.setItemIdentifierValue(itemIdentifier);
        ItemOptionalFields itemOptFields = new ItemOptionalFields();
        BibliographicDescription bibliographicDescription = new BibliographicDescription();
        bibliographicDescription.setAuthor(author);
        bibliographicDescription.setTitle(title);

        itemOptFields.setBibliographicDescription(bibliographicDescription);
        ItemDescription itemDescription = new ItemDescription();
        itemDescription.setCallNumber(callNumber);
        itemOptFields.setItemDescription(itemDescription);
        PickupLocation pickupLocation = new PickupLocation(itemPickupLocation);

        Calendar cal = new GregorianCalendar();
        Date dueDate = DateUtils.addYears(new Date(), 1);
        cal.setTime(dueDate);

        acceptItemInitationData.setItemId(itemId);
        acceptItemInitationData.setPickupLocation(pickupLocation);
        acceptItemInitationData.setUserId(userid);
        acceptItemInitationData.setInitiationHeader(initiationHeader);
        acceptItemInitationData.setRequestId(requestIdentifier);
        acceptItemInitationData.setRequestedActionType(requestActionType);
        acceptItemInitationData.setItemOptionalFields(itemOptFields);
        acceptItemInitationData.setDateForReturn((GregorianCalendar) cal);

        return acceptItemInitationData;

    }

    public JSONObject getAcceptItemResponse(AcceptItemResponseData acceptItem) {
        JSONObject returnJson = new JSONObject();

        // DEAL W/PROBLEMS IN THE RESPONSE
        if (!acceptItem.getProblems().isEmpty()) {
            return generateNcipProblems(acceptItem);
        }

        String itemId = acceptItem.getItemId().getItemIdentifierValue();
        String requestId = acceptItem.getRequestId().getRequestIdentifierValue();

        returnJson.put("itemId", itemId);
        returnJson.put("requestId", requestId);
        return returnJson;
    }


}
