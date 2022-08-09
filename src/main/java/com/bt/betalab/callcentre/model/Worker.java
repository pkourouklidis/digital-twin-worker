/**
 * @author Joost Noppen (611749237), BetaLab, Applied Research
 * Date: 04/07/2022
 * Copyright (c) British Telecommunications plc 2022
 **/


package com.bt.betalab.callcentre.model;

import com.bt.betalab.callcentre.config.Config;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Random;

import static com.bt.betalab.callcentre.config.Config.*;

public class Worker {
    private boolean isSkilled;
    private boolean isFast;

    public Worker() {
        Random rand = new Random();

        isSkilled = rand.nextInt(100) < Config.getSkillBias();
        isFast = rand.nextInt(100) < Config.getSpeedBias();
    }

    public boolean isSkilled() {
        return isSkilled;
    }

    public boolean isFast() {
        return isFast;
    }

    public void Handle(Call call) throws InterruptedException {
        call.setPickupTime(Instant.now());

        Random rand = new Random();

        if (Long.valueOf(Duration.between(call.getArrivalTime(), call.getPickupTime()).getSeconds()).intValue() < Config.getBounceWaitTime()) {
            call.setIsSolved(isSkilled ? rand.nextInt(100) > skilledFailureRate : rand.nextInt(100) > unSkilledFailureRate);
            int sleepTime = Config.getNormalServiceTime();
            if (call.getIsEasy()) {
                sleepTime = isSkilled ? sleepTime : sleepTime + rand.nextInt(2);
                sleepTime = isFast ? sleepTime - rand.nextInt(1) : sleepTime ;
            } else {
                sleepTime = isSkilled ? sleepTime + rand.nextInt(2): sleepTime + rand.nextInt(4);
                sleepTime = isFast ? sleepTime : sleepTime + rand.nextInt(3);
            }
//            Thread.sleep(sleepTime * 1000);
            Thread.sleep(1000);
        } else {
            call.setIsBounced(true);
        }

        call.setClosingTime(Instant.now());
    }
}
