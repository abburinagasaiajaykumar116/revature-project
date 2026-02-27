package org.example.revshop.dtos;

public class CheckoutRequest {

    private String shippingAddress;
    private String billingAddress;
    private String paymentMethod;

    public String getShippingAddress() { return shippingAddress; }


    public String getBillingAddress() { return billingAddress; }


    public String getPaymentMethod() { return paymentMethod; }

}
