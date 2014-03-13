package org.camelcookbook.ws.client;

import org.apache.camel.component.dataset.DataSetSupport;
import org.camelcookbook.ws.payment_service.types.TransferRequest;

public class InputDataset extends DataSetSupport {
  @Override
  protected Object createMessageBody(final long messageIndex) {
    
    TransferRequest request = new TransferRequest();
    request.setBank("Test data"+messageIndex);
    request.setAmount("" + messageIndex);
    request.setValueTest((int)messageIndex);

    return request;
  }
}