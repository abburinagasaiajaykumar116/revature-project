package org.example.revshop.service.impl;

import org.example.revshop.service.PaymentService;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Override
    public boolean processPayment(double amount, String method) {

        if(method.equalsIgnoreCase("COD"))
            return true;

        if(method.equalsIgnoreCase("UPI"))
            return true;

        if(method.equalsIgnoreCase("CARD"))
            return true;

        return false;
    }

}
