/**
 * @author Joost Noppen (611749237), BetaLab, Applied Research
 * Date: 04/07/2022
 * Copyright (c) British Telecommunications plc 2022
 **/


package com.bt.betalab.callcentre.model;

import java.util.Random;

import static com.bt.betalab.callcentre.config.Config.*;

public class Customer {
    private boolean isHappyToWait;
    private boolean isHappyToWaitForService;
    private boolean isUnderstanding;
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
            boolean isHappyWithWait =  isHappyToWait ? rand.nextInt(waitTime) > normalWaitTime + 2 : rand.nextInt(waitTime) > normalWaitTime;

            if (isHappyWithWait) {
                isHappy = isUnderstanding ? rand.nextInt(100) > 90 : rand.nextInt(100) > 99;
            } else {
                isHappy = false;
            }
        } else {
            if (isSolved) {
                boolean isHappyWithWait =  isHappyToWait ? rand.nextInt(waitTime) > normalWaitTime + 5 : rand.nextInt(waitTime) > normalWaitTime + 2;
                boolean isHappyWithWaitForService =  isHappyToWaitForService ? rand.nextInt(serviceTime) > normalServiceTime + 3 : rand.nextInt(serviceTime) > normalServiceTime;

                isHappy = isHappyWithWait && isHappyWithWaitForService;
            } else {
                boolean isHappyWithWait =  isHappyToWait ? rand.nextInt(waitTime) > normalWaitTime : rand.nextInt(waitTime) > normalWaitTime;
                boolean isHappyWithWaitForService =  isHappyToWaitForService ? rand.nextInt(serviceTime) > normalServiceTime : rand.nextInt(serviceTime) > normalServiceTime;
                boolean isHappyWithOutcome = isUnderstanding ? rand.nextInt(100) > 80 : rand.nextInt(100) > 95;

                isHappy = isHappyWithOutcome && isHappyWithWait && isHappyWithWaitForService;
            }
        }
    }
}
