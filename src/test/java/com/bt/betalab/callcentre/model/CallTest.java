/*
 * Created Date: Wednesday, October 12th 2022, 3:25:55 pm
 * Author: Joost Noppen, BetaLab, Applied Research
 * Copyright (c) 2022 British Telecommunications plc
 */


package com.bt.betalab.callcentre.model;

import static org.junit.Assert.assertEquals;

import java.time.Instant;

import org.junit.Test;

public class CallTest {
    @Test
    public void testGetServiceTime() {
        Call call = new Call();

        call.setArrivalTime(Instant.parse("2023-03-20T10:11:12Z"));
        call.setPickupTime(Instant.parse("2023-03-20T10:11:13Z"));

        assertEquals(1, call.getWaitTime());

        call.setPickupTime(Instant.parse("2023-03-20T10:11:12Z"));

        assertEquals(0, call.getWaitTime());

        call.setPickupTime(Instant.parse("2023-03-20T10:11:11Z"));

        assertEquals(-1, call.getWaitTime());
    }

    @Test
    public void testGetWaitTime() {
        Call call = new Call();

        call.setPickupTime(Instant.parse("2023-03-20T10:11:12Z"));
        call.setClosingTime(Instant.parse("2023-03-20T10:11:13Z"));

        assertEquals(1, call.getServiceTime());

        call.setClosingTime(Instant.parse("2023-03-20T10:11:12Z"));

        assertEquals(0, call.getServiceTime());

        call.setClosingTime(Instant.parse("2023-03-20T10:11:11Z"));

        assertEquals(-1, call.getServiceTime());
    }
}
