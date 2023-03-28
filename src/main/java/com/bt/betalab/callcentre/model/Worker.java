/**
 * @author Joost Noppen (611749237), BetaLab, Applied Research
 * Date: 04/07/2022
 * Copyright (c) British Telecommunications plc 2022
 **/


package com.bt.betalab.callcentre.model;

import com.bt.betalab.callcentre.config.Config;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;

import static com.bt.betalab.callcentre.config.Config.*;

public class Worker {
    private boolean isSkilled;
    private boolean isFast;

    public Worker() {
        Random rand = new Random();

        isSkilled = rand.nextInt(100) > Config.getSkillBias();
        isFast = rand.nextInt(100) > Config.getSpeedBias();
    }

    public boolean isSkilled() {
        return isSkilled;
    }

    public boolean isFast() {
        return isFast;
    }

    public void handle(Call call) throws InterruptedException {
        call.setPickupTime(Instant.now());

        Random rand = new Random();

        if (Long.valueOf(Duration.between(call.getArrivalTime(), call.getPickupTime()).getSeconds()).intValue() < Config.getBounceWaitTime()) {
            call.setIsSolved(isSkilled ? rand.nextInt(100) > skilledFailureRate : rand.nextInt(100) > unSkilledFailureRate);
            int mean = Config.getNormalServiceTime();
            mean = call.getIsEasy() ? mean : mean + 1;
            mean = isSkilled ? mean : mean + 1;
            mean = isFast ? mean : mean + 1;
            int sleepTime = (int) Math.round(rand.nextGaussian() + mean);
            Thread.sleep(sleepTime * 1000);
        } else {
            call.setIsBounced(true);
        }

        call.setClosingTime(Instant.now());
    }
}
