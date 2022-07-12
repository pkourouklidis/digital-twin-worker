/**
 * @author Joost Noppen (611749237), BetaLab, Applied Research
 * Date: 04/07/2022
 * Copyright (c) British Telecommunications plc 2022
 **/


package com.bt.betalab.callcentre.controller;

import com.bt.betalab.callcentre.config.Config;
import com.bt.betalab.callcentre.logging.LogLevel;
import com.bt.betalab.callcentre.logging.Logger;
import com.bt.betalab.callcentre.logging.Messages;
import com.bt.betalab.callcentre.model.Call;
import com.bt.betalab.callcentre.model.Worker;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Controller {
    private Worker worker = new Worker();

    private ObjectMapper mapper = new ObjectMapper();

    public boolean process(Call call) {
        try {
            worker.Handle(call);

            int waitTime = Long.valueOf(call.getPickupTime().getTime() - call.getArrivalTime().getTime()).intValue();
            int serviceTime = Long.valueOf(call.getClosingTime().getTime() - call.getPickupTime().getTime()).intValue();
            call.getCustomer().updateHappy(call.isSolved(), call.isBounced(), waitTime, serviceTime);
            call.setWorkerDetails(worker);

            URL url = new URL(Config.getReportUrl());
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("PUT");
            http.setDoOutput(true);
            http.setRequestProperty("Content-Type", "application/json");

            OutputStream stream = http.getOutputStream();
            stream.write(mapper.writeValueAsBytes(call));

            if (http.getResponseCode() < 200 || http.getResponseCode() > 299) {
                Logger.log("Error occured while attempting to store call result. Error Code: " + http.getResponseCode(), LogLevel.ERROR);
            }
            http.disconnect();

            return true;
        } catch (Exception e) {
            Logger.log(Messages.HANDLEERRORMESSAGE + e.getMessage(), LogLevel.ERROR);
            return false;
        }
    }
}
