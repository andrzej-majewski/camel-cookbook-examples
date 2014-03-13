package org.camelcookbook.ws.client;

import java.util.concurrent.Future;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.spi.Synchronization;
import org.camelcookbook.ws.payment_service.Payment;

public class BackgroundTemplateProcessor implements Processor {

  @Override
  public void process(Exchange exchange) throws Exception {
    final Message out = exchange.getOut();
    ProducerTemplate producerTemplate = exchange.getContext().createProducerTemplate();

     producerTemplate.asyncCallbackRequestBody("paymentService",
        exchange.getIn().getBody(), new Synchronization() {
          @Override
          public void onComplete(Exchange exchange) {
            out.setBody(exchange.getOut().getBody());
          }

          @Override
          public void onFailure(Exchange exchange) {
           System.out.println("failure");
          }
        });

    
    
  }
}
