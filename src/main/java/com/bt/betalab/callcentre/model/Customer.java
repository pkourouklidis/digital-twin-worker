/**
 * @author Joost Noppen (611749237), BetaLab, Applied Research
 * Date: 04/07/2022
 * Copyright (c) British Telecommunications plc 2022
 **/


package com.bt.betalab.callcentre.model;

import com.bt.betalab.callcentre.config.Config;

import java.util.Random;

import static com.bt.betalab.callcentre.config.Config.*;

public class Customer {
    private boolean isHappyToWait;
    private boolean isHappyToWaitForService;
    private boolean isUnderstanding;

    public boolean getIsHappy() {
        return isHappy;
    }

    private boolean isHappy;

    public boolean getIsHappyToWait() {
        return isHappyToWait;
    }

    public void setIsHappyToWait(boolean happyToWait) {
        isHappyToWait = happyToWait;
    }

    public boolean getIsHappyToWaitForService() {
        return isHappyToWaitForService;
    }

    public void setIsHappyToWaitForService(boolean happyToWaitForService) {
        isHappyToWaitForService = happyToWaitForService;
    }

    public boolean getIsUnderstanding() {
        return isUnderstanding;
    }

    public void setIsUnderstanding(boolean understanding) {
        isUnderstanding = understanding;
    }

    public void updateHappy(boolean isSolved, boolean isBounced, int waitTime, int serviceTime) {
        Random rand = new Random();

        if (isBounced) {
            boolean isHappyWithWait =  isHappyToWait ? rand.nextInt(waitTime) > Config.getNormalWaitTime() + 2 : rand.nextInt(waitTime) > Config.getNormalWaitTime();

            if (isHappyWithWait) {
                isHappy = isUnderstanding ? rand.nextInt(100) > 90 : rand.nextInt(100) > 99;
            } else {
                isHappy = false;
            }
        } else {
            if (isSolved) {
                boolean isHappyWithWait =  isHappyToWait ? rand.nextInt(waitTime) < Config.getNormalWaitTime() + 5 : rand.nextInt(waitTime) < Config.getNormalWaitTime() + 2;
                boolean isHappyWithWaitForService =  isHappyToWaitForService ? rand.nextInt(serviceTime) < Config.getNormalServiceTime() + 3 : rand.nextInt(serviceTime) < Config.getNormalServiceTime();

                isHappy = isHappyWithWait && isHappyWithWaitForService;
            } else {
                boolean isHappyWithWait =  isHappyToWait ? rand.nextInt(waitTime) < Config.getNormalWaitTime() : rand.nextInt(waitTime) < Config.getNormalWaitTime();
                boolean isHappyWithWaitForService =  isHappyToWaitForService ? rand.nextInt(serviceTime) < Config.getNormalServiceTime() : rand.nextInt(serviceTime) < Config.getNormalServiceTime();
                boolean isHappyWithOutcome = isUnderstanding ? rand.nextInt(100) > 80 : rand.nextInt(100) > 95;

                isHappy = isHappyWithOutcome && isHappyWithWait && isHappyWithWaitForService;
            }
        }
    }
}
