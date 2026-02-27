package org.example.revshop.service;

import org.example.revshop.model.*;

import java.util.List;

public interface CheckoutService {
    public Long checkout(Integer userId,
                         String shipping,
                         String billing,
                         String paymentMethod);


}
