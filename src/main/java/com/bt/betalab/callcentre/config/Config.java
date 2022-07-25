/**
 * @author Joost Noppen (611749237), BetaLab, Applied Research
 * Date: 04/07/2022
 * Copyright (c) British Telecommunications plc 2022
 **/


package com.bt.betalab.callcentre.config;

public class Config {
    public static int normalWaitTime = 9;
    public static int bounceWaitTime = 15;
    public static int normalServiceTime = 3;

    public static int skilledFailureRate = 25;
    public static int unSkilledFailureRate = 40;

    public static int skillBias;

    public static int speedBias;

    public static boolean isValid() {
        boolean hasAddress = System.getenv("QUEUE_ADDRESS") != null;
        boolean hasPort = System.getenv("QUEUE_PORT") != null;
        boolean hasUser = System.getenv("QUEUE_USER") != null;
        boolean hasPassword = System.getenv("QUEUE_PASSWORD") != null;
        boolean hasName = System.getenv("QUEUE_NAME") != null;
        boolean hasReportUrl = System.getenv("REPORT_URL") != null;
        boolean hasSkillBias = System.getenv("SKILL_BIAS") != null;
        boolean hasSpeedBias = System.getenv("SPEED_BIAS") != null;
        return hasAddress && hasPort && hasUser && hasPassword && hasName && hasReportUrl && hasSkillBias && hasSpeedBias;
    }

    public static String getMessageQueueAddress() {
        return System.getenv("QUEUE_ADDRESS");
    }

    public static int getMessageQueuePort() {
        return Integer.parseInt(System.getenv("QUEUE_PORT"));
    }

    public static String getMessageQueueUser() {
        return System.getenv("QUEUE_USER");
    }

    public static String getMessageQueuePassword() {
        return System.getenv("QUEUE_PASSWORD");
    }

    public static String getMessageQueueName() {
        return System.getenv("QUEUE_NAME");
    }

    public static String getReportUrl() {
        return System.getenv("REPORT_URL");
    }

    public static int getSkillBias() {
        try {
            return Integer.parseInt(System.getenv("SKILL_BIAS"));
        } catch (NumberFormatException e) {
            return 50;
        }
    }

    public static int getSpeedBias() {
        try {
            return Integer.parseInt(System.getenv("SPEED_BIAS"));
        } catch (NumberFormatException e) {
            return 50;
        }
    }
}
