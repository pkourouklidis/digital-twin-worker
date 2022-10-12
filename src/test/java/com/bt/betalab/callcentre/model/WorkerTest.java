/*
 * Created Date: Wednesday, October 12th 2022, 10:15:18 am
 * Author: Joost Noppen, BetaLab, Applied Research
 * Copyright (c) 2022 British Telecommunications plc
 */


package com.bt.betalab.callcentre.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.Instant;

import org.junit.Test;

public class WorkerTest {
    @Test
    public void testHandle() {
        Worker worker = new Worker();

        Call call = new Call();
        call.setSimulationId("test id");
        call.setSimulationStartTime(Instant.now().toString());
        call.setIsEasy(false);

        Customer customer = new Customer();
        customer.setIsHappyToWait(true);
        customer.setIsHappyToWaitForService(true);
        customer.setIsUnderstanding(true);
        call.setCustomer(customer);
        
        call.setArrivalTime(Instant.now());

        boolean thrown = false;

        try {
            worker.handle(call);
        } catch (InterruptedException e) {
            thrown = true;
        }

        assertFalse(thrown);
        assertFalse(call.getIsBounced());

        call.setArrivalTime(Instant.now());

        thrown = false;
        call.setIsEasy(true);

        try {
            worker.handle(call);
        } catch (InterruptedException e) {
            thrown = true;
        }

        assertFalse(thrown);
        assertFalse(call.getIsBounced());

        call.setArrivalTime(Instant.parse("2022-03-20T10:11:12Z"));

        thrown = false;

        try {
            worker.handle(call);
        } catch (InterruptedException e) {
            thrown = true;
        }

        assertFalse(thrown);
        assertTrue(call.getIsBounced());

        call.setArrivalTime(Instant.parse("2023-03-20T10:11:12Z"));

        thrown = false;

        try {
            worker.handle(call);
        } catch (InterruptedException e) {
            thrown = true;
        }

        assertFalse(thrown);
        assertTrue(call.getIsBounced());
    }

    @Test
    public void testIsFast() {

    }

    @Test
    public void testIsSkilled() {

    }
}
