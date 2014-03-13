package org.camelcookbook.ws.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.camel.AsyncCallback;
import org.apache.camel.AsyncProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.camelcookbook.ws.payment_service.FaultMessage;
import org.camelcookbook.ws.payment_service.Payment;
import org.camelcookbook.ws.payment_service.types.TransferRequest;
import org.camelcookbook.ws.payment_service.types.TransferResponse;

public class BackgroundThreadAsyncProcessor implements AsyncProcessor {

  private final ExecutorService executorService = Executors.newSingleThreadExecutor();
  private Payment paymentService;
  
  public void setPaymentService(Payment paymentService) {
    this.paymentService = paymentService;
  }

  @Override
  public void process(Exchange exchange) throws Exception {
    throw new IllegalStateException("this should never be called");

  }

  @Override
  public boolean process(final Exchange exchange, final AsyncCallback callback) {

    final Message in = exchange.getIn();
    final Message out = exchange.getOut();
    final boolean completesSynchronously = false;
    executorService.submit(new Runnable() {
      @Override
      public void run() {
        try {
          TransferResponse response = paymentService.transferFunds(in.getBody(TransferRequest.class));
          out.setBody(response);
        } catch (FaultMessage e) {
          e.printStackTrace();
        }
        // the current thread will continue to process
        // the exchange through the
        // remainder of the route
        callback.done(completesSynchronously);
      }
    });

    return completesSynchronously;
  }
}
