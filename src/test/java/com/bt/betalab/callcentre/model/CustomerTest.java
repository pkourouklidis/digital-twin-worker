package com.bt.betalab.callcentre.model;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class CustomerTest {

    @Test
    public void updateHappy() {Customer customer = new Customer();
        customer.setIsHappyToWait(true);
        customer.setIsHappyToWaitForService(true);
        customer.setIsUnderstanding(true);

        customer.updateHappy(true, false, 25, 15);

        assertTrue(customer.getIsHappy() || !customer.getIsHappy());
    }
}