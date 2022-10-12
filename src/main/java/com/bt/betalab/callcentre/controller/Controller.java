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
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

@Component
public class Controller {
    private Worker worker = new Worker();

    private ObjectMapper mapper = new ObjectMapper();

    public boolean process(Call call) {
        mapper.registerModule(new JavaTimeModule());
        try {
            worker.handle(call);

            call.getCustomer().updateHappy(call.getIsSolved(), call.getIsBounced(), call.getWaitTime(), call.getServiceTime());
            call.setWorkerDetails(worker);

            String encoding = Base64.getEncoder().encodeToString((Config.getReportUrlUser() + ":" + Config.getReportUrlPassword()).getBytes("UTF-8"));

            URL url = new URL(Config.getReportUrl());
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("PUT");
            http.setDoOutput(true);
            http.setRequestProperty("Content-Type", "application/json");
            http.setRequestProperty("Authorization", "Basic " + encoding);

            OutputStream stream = http.getOutputStream();
            stream.write(mapper.writeValueAsBytes(call));

            if (http.getResponseCode() < 200 || http.getResponseCode() > 299) {
                Logger.log("Error occurred while attempting to store call result. Error Code: " + http.getResponseCode(), LogLevel.ERROR);
            }
            http.disconnect();

            return true;
        } catch (Exception e) {
            Logger.log(Messages.HANDLEERRORMESSAGE + e.getMessage(), LogLevel.ERROR);
            return false;
        }
    }
}
