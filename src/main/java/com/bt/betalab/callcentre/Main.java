/**
 * @author Joost Noppen (611749237), BetaLab, Applied Research
 * Date: 04/07/2022
 * Copyright (c) British Telecommunications plc 2022
 **/


package com.bt.betalab.callcentre;

import com.bt.betalab.callcentre.config.Config;
import com.bt.betalab.callcentre.controller.Controller;
import com.bt.betalab.callcentre.logging.LogLevel;
import com.bt.betalab.callcentre.logging.Logger;
import com.bt.betalab.callcentre.logging.Messages;
import com.bt.betalab.callcentre.model.Call;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@SpringBootApplication
public class Main implements CommandLineRunner {

    @Autowired
    Controller controller;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        start();
    }

    public void start() {
        if (Config.isValid()) {
            startQueueConsumption();
        } else {
            Logger.log(Messages.INVALIDCONFIGMESSAGE, LogLevel.ERROR);
        }
    }

    /**
     * Method that connects the application to the RabbitMQ message queue
     * server (push-based) and handles every message its get sent by calling
     * the handle method for processing.
     */
    public void startQueueConsumption() {
        ConnectionFactory conFactory = new ConnectionFactory();
        conFactory.setHost(Config.getMessageQueueAddress());
        conFactory.setPort(Config.getMessageQueuePort());
        conFactory.setUsername(Config.getMessageQueueUser());
        conFactory.setPassword(Config.getMessageQueuePassword());
        conFactory.setAutomaticRecoveryEnabled(true);

        Logger.log(Messages.CONNECTINGTOMESSAGEQUEUEMESSAGE + Config.getMessageQueueAddress() + ":" + Config.getMessageQueuePort(), LogLevel.INFO);

        try {

            Connection connection = conFactory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(Config.getMessageQueueName(), true, false, false, null);

            Logger.log(Messages.CONNECTEDTOMESSAGEQUEUEMESSAGE, LogLevel.INFO);

            channel.basicConsume(Config.getMessageQueueName(), false, createConsumer(channel));

        } catch (IOException e) {
            Logger.log(Messages.CHANNELERRORMESSAGEQUEUEMESSAGE + e.getMessage(), LogLevel.ERROR);
        } catch (TimeoutException e) {
            Logger.log(Messages.CONNECTIONERRORMESSAGEQUEUEMESSAGE + e.getMessage(), LogLevel.ERROR);
        }
    }

    public DefaultConsumer createConsumer(Channel channel) {
        return new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {

                try {
                    String message = new String(body, "UTF-8");
                    boolean resultOK = handle(message);

                    if (resultOK) {
                        Logger.log(Messages.OPERATIONSUCCESSMESSAGE, LogLevel.INFO);
                    } else {
                        Logger.log(Messages.OPERATIONFAILUREMESSAGE, LogLevel.ERROR);
                    }
                } catch (Exception e) {
                    Logger.log(Messages.RETRIEVINGMESSAGEERRORMESSAGE + e.getMessage(), LogLevel.ERROR);
                } finally {
                    try {
                        channel.basicAck(envelope.getDeliveryTag(), false);
                    } catch (IOException e) {
                        Logger.log(Messages.HANDLEERRORMESSAGE + e.getMessage(), LogLevel.ERROR);
                    }
                }
            }
        };
    }

    /**
     * Method that handles the input JSON message for the call-centre.
     *
     * @param  input the JSON message that was sent to the message queue
     */
    public boolean handle(String input) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        Call call = null;
        try {
            call = mapper.readValue(input, Call.class);
            return controller.process(call);
        } catch (JsonProcessingException e) {
            Logger.log(Messages.INVALIDERRORMESSAGE + e.getMessage(), LogLevel.ERROR);
            return false;
        }
    }
}
