package org.recap.mqconsumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.recap.BaseTestCase;
import org.recap.RecapCommonConstants;
import org.recap.ils.model.response.ItemInformationResponse;
import org.recap.model.jpa.ItemRequestInformation;
import org.recap.model.jpa.RequestInformation;
import org.recap.request.BulkItemRequestProcessService;
import org.recap.request.BulkItemRequestService;
import org.recap.request.ItemEDDRequestService;
import org.recap.request.ItemRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by hemalathas on 14/3/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class RequestItemQueueConsumerUT{

    private static final Logger logger = LoggerFactory.getLogger(RequestItemQueueConsumer.class);

    @Mock
    RequestItemQueueConsumer requestItemQueueConsumer;

    @InjectMocks
    RequestItemQueueConsumer injectMockedRequestItemQueueConsumer;

    @Mock
    BulkItemRequestProcessService bulkItemRequestProcessService;

    @Mock
    Exchange exchange;

    @Mock
    Message message;

    @Mock
    BulkItemRequestService bulkItemRequestService;

    @Mock
    ItemRequestService itemRequestService;

    @Mock
    ItemEDDRequestService itemEDDRequestService;

    @Mock
    ObjectMapper om;

    @Before
    public  void setup(){
        MockitoAnnotations.initMocks(this);
        Mockito.when(requestItemQueueConsumer.getBulkItemRequestProcessService()).thenReturn(bulkItemRequestProcessService);
        Mockito.when(requestItemQueueConsumer.getBulkItemRequestService()).thenReturn(bulkItemRequestService);
        Mockito.when(requestItemQueueConsumer.getItemEDDRequestService()).thenReturn(itemEDDRequestService);
        Mockito.when(requestItemQueueConsumer.getItemRequestService()).thenReturn(itemRequestService);
        Mockito.when(requestItemQueueConsumer.getLogger()).thenReturn(logger);
        Mockito.when(requestItemQueueConsumer.getObjectMapper()).thenReturn(om);
    }

    @Test
    public void testRequestItemOnMessage() throws IOException {
        ItemRequestInformation itemRequestInformation = new ItemRequestInformation();
        itemRequestInformation.setItemBarcodes(Arrays.asList("123"));
        String body = itemRequestInformation.toString();
        Mockito.when(requestItemQueueConsumer.getObjectMapper()).thenReturn(om);
        Mockito.when(requestItemQueueConsumer.getLogger()).thenReturn(logger);
        Mockito.when(requestItemQueueConsumer.getItemRequestService()).thenReturn(itemRequestService);
        Mockito.when(requestItemQueueConsumer.getObjectMapper().readValue(body, ItemRequestInformation.class)).thenReturn(itemRequestInformation);
        Mockito.doCallRealMethod().when(requestItemQueueConsumer).requestItemOnMessage(body,exchange);
        requestItemQueueConsumer.requestItemOnMessage(body,exchange);
    }

    @Test
    public void testRequestItemEDDOnMessage() throws IOException {
        ItemRequestInformation itemRequestInformation = new ItemRequestInformation();
        itemRequestInformation.setItemBarcodes(Arrays.asList("123"));
        String body = itemRequestInformation.toString();
        Mockito.when(requestItemQueueConsumer.getObjectMapper()).thenReturn(om);
        Mockito.when(requestItemQueueConsumer.getLogger()).thenReturn(logger);
        Mockito.when(requestItemQueueConsumer.getItemEDDRequestService()).thenReturn(itemEDDRequestService);
        Mockito.when(requestItemQueueConsumer.getObjectMapper().readValue(body, ItemRequestInformation.class)).thenReturn(itemRequestInformation);
        Mockito.doCallRealMethod().when(requestItemQueueConsumer).requestItemEDDOnMessage(body,exchange);
        requestItemQueueConsumer.requestItemEDDOnMessage(body,exchange);
    }

    @Test
    public void testRequestItemBorrowDirectOnMessage() throws IOException {
        ItemRequestInformation itemRequestInformation = new ItemRequestInformation();
        itemRequestInformation.setItemBarcodes(Arrays.asList("123"));
        String body = itemRequestInformation.toString();
        Mockito.when(requestItemQueueConsumer.getObjectMapper()).thenReturn(om);
        Mockito.when(requestItemQueueConsumer.getLogger()).thenReturn(logger);
        Mockito.when(requestItemQueueConsumer.getItemRequestService()).thenReturn(itemRequestService);
        Mockito.when(requestItemQueueConsumer.getObjectMapper().readValue(body, ItemRequestInformation.class)).thenReturn(itemRequestInformation);
        Mockito.doCallRealMethod().when(requestItemQueueConsumer).requestItemBorrowDirectOnMessage(body,exchange);
        requestItemQueueConsumer.requestItemBorrowDirectOnMessage(body,exchange);
    }

    @Test
    public void testRequestItemRecallOnMessage() throws IOException {
        ItemRequestInformation itemRequestInformation = new ItemRequestInformation();
        itemRequestInformation.setItemBarcodes(Arrays.asList("123"));
        String body = itemRequestInformation.toString();
        Mockito.when(requestItemQueueConsumer.getObjectMapper()).thenReturn(om);
        Mockito.when(requestItemQueueConsumer.getLogger()).thenReturn(logger);
        Mockito.when(requestItemQueueConsumer.getItemRequestService()).thenReturn(itemRequestService);
        Mockito.when(requestItemQueueConsumer.getObjectMapper().readValue(body, ItemRequestInformation.class)).thenReturn(itemRequestInformation);
        Mockito.doCallRealMethod().when(requestItemQueueConsumer).requestItemRecallOnMessage(body,exchange);
        requestItemQueueConsumer.requestItemRecallOnMessage(body,exchange);
    }

    @Test
    public void testPulRequestTopicOnMessage() throws Exception{
        ItemRequestInformation itemRequestInformation = new ItemRequestInformation();
        itemRequestInformation.setItemBarcodes(Arrays.asList("123"));
        JSONObject jsonObject = new JSONObject();
        String body = jsonObject.toString();
        Mockito.when(requestItemQueueConsumer.getLogger()).thenReturn(logger);
        Mockito.when(requestItemQueueConsumer.getItemRequestService()).thenReturn(itemRequestService);
        Mockito.doCallRealMethod().when(requestItemQueueConsumer).pulRequestTopicOnMessage(body);
        requestItemQueueConsumer.pulRequestTopicOnMessage(body);
    }

    @Test
    public void testPulEDDTopicOnMessage() throws Exception{
        ItemRequestInformation itemRequestInformation = new ItemRequestInformation();
        itemRequestInformation.setItemBarcodes(Arrays.asList("123"));
        String body = itemRequestInformation.toString();
        Mockito.when(requestItemQueueConsumer.getLogger()).thenReturn(logger);
        Mockito.when(requestItemQueueConsumer.getItemRequestService()).thenReturn(itemRequestService);
        Mockito.doCallRealMethod().when(requestItemQueueConsumer).pulEDDTopicOnMessage(body);
        requestItemQueueConsumer.pulEDDTopicOnMessage(body);
    }

    @Test
    public void testPulRecalTopicOnMessage() throws Exception{
        ItemRequestInformation itemRequestInformation = new ItemRequestInformation();
        itemRequestInformation.setItemBarcodes(Arrays.asList("123"));
        String body = itemRequestInformation.toString();
        Mockito.when(requestItemQueueConsumer.getLogger()).thenReturn(logger);
        Mockito.when(requestItemQueueConsumer.getItemRequestService()).thenReturn(itemRequestService);
        Mockito.doCallRealMethod().when(requestItemQueueConsumer).pulRecalTopicOnMessage(body);
        requestItemQueueConsumer.pulRecalTopicOnMessage(body);
    }

    @Test
    public void testPulBorrowDirectTopicOnMessage() throws Exception{
        ItemRequestInformation itemRequestInformation = new ItemRequestInformation();
        itemRequestInformation.setItemBarcodes(Arrays.asList("123"));
        String body = itemRequestInformation.toString();
        Mockito.when(requestItemQueueConsumer.getLogger()).thenReturn(logger);
        Mockito.when(requestItemQueueConsumer.getItemRequestService()).thenReturn(itemRequestService);
        Mockito.doCallRealMethod().when(requestItemQueueConsumer).pulBorrowDirectTopicOnMessage(body);
        requestItemQueueConsumer.pulBorrowDirectTopicOnMessage(body);
    }

    @Test
    public void testCulRequestTopicOnMessage() throws Exception{
        ItemRequestInformation itemRequestInformation = new ItemRequestInformation();
        itemRequestInformation.setItemBarcodes(Arrays.asList("123"));
        String body = itemRequestInformation.toString();
        Mockito.when(requestItemQueueConsumer.getLogger()).thenReturn(logger);
        Mockito.when(requestItemQueueConsumer.getItemRequestService()).thenReturn(itemRequestService);
        Mockito.doCallRealMethod().when(requestItemQueueConsumer).culRequestTopicOnMessage(body);
        requestItemQueueConsumer.culRequestTopicOnMessage(body);
    }

    @Test
    public void testCulEDDTopicOnMessage() throws Exception{
        ItemRequestInformation itemRequestInformation = new ItemRequestInformation();
        itemRequestInformation.setItemBarcodes(Arrays.asList("123"));
        String body = itemRequestInformation.toString();
        Mockito.when(requestItemQueueConsumer.getLogger()).thenReturn(logger);
        Mockito.when(requestItemQueueConsumer.getItemRequestService()).thenReturn(itemRequestService);
        Mockito.doCallRealMethod().when(requestItemQueueConsumer).culEDDTopicOnMessage(body);
        requestItemQueueConsumer.culEDDTopicOnMessage(body);
    }

    @Test
    public void testCulRecalTopicOnMessage(){
        ItemRequestInformation itemRequestInformation = new ItemRequestInformation();
        itemRequestInformation.setItemBarcodes(Arrays.asList("123"));
        String body = itemRequestInformation.toString();
        Mockito.when(requestItemQueueConsumer.getLogger()).thenReturn(logger);
        Mockito.when(requestItemQueueConsumer.getItemRequestService()).thenReturn(itemRequestService);
        Mockito.doCallRealMethod().when(requestItemQueueConsumer).culRecalTopicOnMessage(body);
        requestItemQueueConsumer.culRecalTopicOnMessage(body);
    }

    @Test
    public void testCulBorrowDirectTopicOnMessage(){
        ItemRequestInformation itemRequestInformation = new ItemRequestInformation();
        itemRequestInformation.setItemBarcodes(Arrays.asList("123"));
        String body = itemRequestInformation.toString();
        Mockito.when(requestItemQueueConsumer.getLogger()).thenReturn(logger);
        Mockito.when(requestItemQueueConsumer.getItemRequestService()).thenReturn(itemRequestService);
        Mockito.doCallRealMethod().when(requestItemQueueConsumer).culBorrowDirectTopicOnMessage(body);
        requestItemQueueConsumer.culBorrowDirectTopicOnMessage(body);
    }

    @Test
    public void testNyplRequestTopicOnMessage(){
        ItemRequestInformation itemRequestInformation = new ItemRequestInformation();
        itemRequestInformation.setItemBarcodes(Arrays.asList("123"));
        String body = itemRequestInformation.toString();
        Mockito.when(requestItemQueueConsumer.getLogger()).thenReturn(logger);
        Mockito.when(requestItemQueueConsumer.getItemRequestService()).thenReturn(itemRequestService);
        Mockito.doCallRealMethod().when(requestItemQueueConsumer).nyplRequestTopicOnMessage(body);
        requestItemQueueConsumer.nyplRequestTopicOnMessage(body);
    }

    @Test
    public void testNYPLEDDTopicOnMessage(){
        ItemRequestInformation itemRequestInformation = new ItemRequestInformation();
        itemRequestInformation.setItemBarcodes(Arrays.asList("123"));
        String body = itemRequestInformation.toString();
        Mockito.when(requestItemQueueConsumer.getLogger()).thenReturn(logger);
        Mockito.when(requestItemQueueConsumer.getItemRequestService()).thenReturn(itemRequestService);
        Mockito.doCallRealMethod().when(requestItemQueueConsumer).nyplEDDTopicOnMessage(body);
        requestItemQueueConsumer.nyplEDDTopicOnMessage(body);
    }

    @Test
    public void testNYPLRecalTopicOnMessage(){
        ItemRequestInformation itemRequestInformation = new ItemRequestInformation();
        itemRequestInformation.setItemBarcodes(Arrays.asList("123"));
        String body = itemRequestInformation.toString();
        Mockito.when(requestItemQueueConsumer.getLogger()).thenReturn(logger);
        Mockito.when(requestItemQueueConsumer.getItemRequestService()).thenReturn(itemRequestService);
        Mockito.doCallRealMethod().when(requestItemQueueConsumer).nyplRecalTopicOnMessage(body);
        requestItemQueueConsumer.nyplRecalTopicOnMessage(body);
    }

    @Test
    public void testNYPLBorrowDirectTopicOnMessage(){
        ItemRequestInformation itemRequestInformation = new ItemRequestInformation();
        itemRequestInformation.setItemBarcodes(Arrays.asList("123"));
        String body = itemRequestInformation.toString();
        Mockito.when(requestItemQueueConsumer.getLogger()).thenReturn(logger);
        Mockito.when(requestItemQueueConsumer.getItemRequestService()).thenReturn(itemRequestService);
        Mockito.doCallRealMethod().when(requestItemQueueConsumer).nyplBorrowDirectTopicOnMessage(body);
        requestItemQueueConsumer.nyplBorrowDirectTopicOnMessage(body);
    }

    @Test
    public void testLasOutgoingQOnCompletion(){
        ItemRequestInformation itemRequestInformation = new ItemRequestInformation();
        itemRequestInformation.setItemBarcodes(Arrays.asList("123"));
        String body = itemRequestInformation.toString();
        Mockito.when(requestItemQueueConsumer.getLogger()).thenReturn(logger);
//        Mockito.when(requestItemQueueConsumer.getItemRequestService()).thenReturn(itemRequestService);
        Mockito.doCallRealMethod().when(requestItemQueueConsumer).lasOutgoingQOnCompletion(body);
        requestItemQueueConsumer.lasOutgoingQOnCompletion(body);
    }

    @Test
    public void testLasIngoingQOnCompletion(){
        ItemRequestInformation itemRequestInformation = new ItemRequestInformation();
        itemRequestInformation.setItemBarcodes(Arrays.asList("123"));
        String body = itemRequestInformation.toString();
        Mockito.when(requestItemQueueConsumer.getLogger()).thenReturn(logger);
        Mockito.doCallRealMethod().when(requestItemQueueConsumer).lasIngoingQOnCompletion(body);
        requestItemQueueConsumer.lasIngoingQOnCompletion(body);
    }

    @Test
    public void testLasResponseRetrivalOnMessage(){
        ItemRequestInformation itemRequestInformation = new ItemRequestInformation();
        itemRequestInformation.setItemBarcodes(Arrays.asList("123"));
        String body = itemRequestInformation.toString();
        Mockito.when(requestItemQueueConsumer.getItemRequestService()).thenReturn(itemRequestService);
        Mockito.when(requestItemQueueConsumer.getLogger()).thenReturn(logger);
        Mockito.doCallRealMethod().when(requestItemQueueConsumer).lasResponseRetrivalOnMessage(body);
        requestItemQueueConsumer.lasResponseRetrivalOnMessage(body);
    }

    @Test
    public void testLasResponseEDDOnMessage(){
        ItemRequestInformation itemRequestInformation = new ItemRequestInformation();
        itemRequestInformation.setItemBarcodes(Arrays.asList("123"));
        String body = itemRequestInformation.toString();
        Mockito.when(requestItemQueueConsumer.getItemRequestService()).thenReturn(itemRequestService);
        Mockito.when(requestItemQueueConsumer.getLogger()).thenReturn(logger);
        Mockito.doCallRealMethod().when(requestItemQueueConsumer).lasResponseEDDOnMessage(body);
        requestItemQueueConsumer.lasResponseEDDOnMessage(body);
    }

    @Test
    public void testLasResponsePWIOnMessage(){
        ItemRequestInformation itemRequestInformation = new ItemRequestInformation();
        itemRequestInformation.setItemBarcodes(Arrays.asList("123"));
        String body = itemRequestInformation.toString();
        Mockito.when(requestItemQueueConsumer.getLogger()).thenReturn(logger);
        Mockito.doCallRealMethod().when(requestItemQueueConsumer).lasResponsePWIOnMessage(body);
        requestItemQueueConsumer.lasResponsePWIOnMessage(body);
    }

    @Test
    public void testLasResponsePWDOnMessage(){
        ItemRequestInformation itemRequestInformation = new ItemRequestInformation();
        itemRequestInformation.setItemBarcodes(Arrays.asList("123"));
        String body = itemRequestInformation.toString();
        Mockito.when(requestItemQueueConsumer.getLogger()).thenReturn(logger);
        Mockito.doCallRealMethod().when(requestItemQueueConsumer).lasResponsePWDOnMessage(body);
        requestItemQueueConsumer.lasResponsePWDOnMessage(body);
    }
    @Test
    public void bulkRequestItemOnMessage() throws Exception{
        String body = "12345";
        Mockito.when(requestItemQueueConsumer.getBulkItemRequestService()).thenReturn(bulkItemRequestService);
//        Mockito.doNothing().when(bulkItemRequestService).bulkRequestItems(1);
        Mockito.when(requestItemQueueConsumer.getLogger()).thenReturn(logger);
        Mockito.doCallRealMethod().when(requestItemQueueConsumer).bulkRequestItemOnMessage(body,exchange);
        requestItemQueueConsumer.bulkRequestItemOnMessage(body,exchange);
    }
    @Test
    public void bulkRequestProcessItemOnMessage() throws Exception{
        message.setHeader(RecapCommonConstants.BULK_REQUEST_ID,1);
        message.setBody("BULK REQUEST");
        exchange.setIn(message);
        String body = "12345";
        Mockito.when(requestItemQueueConsumer.getLogger()).thenReturn(logger);
        Mockito.when(exchange.getIn()).thenReturn(message);
        Mockito.when(requestItemQueueConsumer.getBulkItemRequestProcessService()).thenReturn(bulkItemRequestProcessService);
//        Mockito.doNothing().when(bulkItemRequestProcessService).processBulkRequestItem(body,1);
        Mockito.doCallRealMethod().when(requestItemQueueConsumer).bulkRequestProcessItemOnMessage(body,exchange);
        requestItemQueueConsumer.bulkRequestProcessItemOnMessage(body,exchange);
    }
    @Test
    public  void requestItemLasStatusCheckOnMessage() throws Exception{
        ItemRequestInformation itemRequestInformation = new ItemRequestInformation();
        itemRequestInformation.setItemBarcodes(Arrays.asList("123"));
        ItemInformationResponse itemInformationResponse = new ItemInformationResponse();
        itemInformationResponse.setRequestId(1);
        itemInformationResponse.setBibID("1");
        RequestInformation requestInformation = new RequestInformation();
        requestInformation.setItemRequestInfo(itemRequestInformation);
        requestInformation.setItemResponseInformation(itemInformationResponse);
        JSONObject jsonObject = new JSONObject();
        String body = jsonObject.toString();
//        Mockito.when(om.readValue(body, RequestInformation.class)).thenReturn(requestInformation);
        Mockito.when(requestItemQueueConsumer.getItemRequestService()).thenReturn(itemRequestService);
//        Mockito.when(requestItemQueueConsumer.getItemRequestService().executeLasitemCheck(requestInformation.getItemRequestInfo(), requestInformation.getItemResponseInformation())).thenReturn(true);
        Mockito.when(requestItemQueueConsumer.getLogger()).thenReturn(logger);
        Mockito.doCallRealMethod().when(requestItemQueueConsumer).requestItemLasStatusCheckOnMessage(body,exchange);
        requestItemQueueConsumer.requestItemLasStatusCheckOnMessage(body,exchange);
    }
    @Test
    public  void requestItemLasStatusCheckOnMessageException() throws Exception{
        String body = "12345";
        Mockito.when(requestItemQueueConsumer.getLogger()).thenReturn(logger);
        Mockito.doCallRealMethod().when(requestItemQueueConsumer).requestItemLasStatusCheckOnMessage(body,exchange);
        requestItemQueueConsumer.requestItemLasStatusCheckOnMessage(body,exchange);
    }
}