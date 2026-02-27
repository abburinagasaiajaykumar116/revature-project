package org.example.revshop.serviceimpltest;



import org.example.revshop.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentServiceImplTest {

    private final PaymentServiceImpl paymentService =
            new PaymentServiceImpl();

    // ✅ COD
    @Test
    void testProcessPayment_COD() {
        boolean result = paymentService.processPayment(1000.0, "COD");
        assertTrue(result);
    }

    // ✅ UPI
    @Test
    void testProcessPayment_UPI() {
        boolean result = paymentService.processPayment(1000.0, "UPI");
        assertTrue(result);
    }

    // ✅ CARD
    @Test
    void testProcessPayment_CARD() {
        boolean result = paymentService.processPayment(1000.0, "CARD");
        assertTrue(result);
    }

    // ✅ Case insensitive check
    @Test
    void testProcessPayment_CaseInsensitive() {
        boolean result = paymentService.processPayment(1000.0, "cod");
        assertTrue(result);
    }

    // ❌ Invalid method
    @Test
    void testProcessPayment_InvalidMethod() {
        boolean result = paymentService.processPayment(1000.0, "NETBANKING");
        assertFalse(result);
    }

    // ❌ Null method (will throw NullPointerException)
    @Test
    void testProcessPayment_NullMethod() {
        assertThrows(NullPointerException.class,
                () -> paymentService.processPayment(1000.0, null));
    }
}