package com.bt.betalab.callcentre.model;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class CustomerTest {

    @Test
    public void updateHappy() {Customer customer = new Customer();
        customer.setIsHappyToWait(true);
        customer.setIsHappyToWaitForService(true);
        customer.setIsUnderstanding(true);

        boolean thrown = false;

        try {
            customer.updateHappy(true, false, 0, 15);
        } catch (Exception e) {
            thrown = true;
        }
        
        assertFalse(thrown);

        thrown = false;

        try {
            customer.updateHappy(true, false, 10, 0);
        } catch (Exception e) {
            thrown = true;
        }
        
        assertFalse(thrown);

        thrown = false;

        try {
            customer.updateHappy(true, false, 10, 15);
        } catch (Exception e) {
            thrown = true;
        }
        
        assertFalse(thrown);
    }
}