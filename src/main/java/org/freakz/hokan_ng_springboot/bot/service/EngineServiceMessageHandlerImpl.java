package org.freakz.hokan_ng_springboot.bot.service;

import lombok.extern.slf4j.Slf4j;
import org.freakz.hokan_ng_springboot.bot.command.CommandHandlerService;
import org.freakz.hokan_ng_springboot.bot.command.handlers.Cmd;
import org.freakz.hokan_ng_springboot.bot.enums.HokanModule;
import org.freakz.hokan_ng_springboot.bot.events.EngineResponse;
import org.freakz.hokan_ng_springboot.bot.events.InternalRequest;
import org.freakz.hokan_ng_springboot.bot.events.IrcMessageEvent;
import org.freakz.hokan_ng_springboot.bot.events.ServiceRequest;
import org.freakz.hokan_ng_springboot.bot.jms.JmsEnvelope;
import org.freakz.hokan_ng_springboot.bot.jms.api.JmsSender;
import org.freakz.hokan_ng_springboot.bot.jms.api.JmsServiceMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.util.Date;

/**
 * Created by Petri Airio on 10.2.2015.
 * -
 */
@Controller
@Slf4j
public class EngineServiceMessageHandlerImpl implements JmsServiceMessageHandler {

    private final ApplicationContext context;

    private final CommandHandlerService commandHandlerService;

    private final JmsSender jmsSender;

    @Autowired
    public EngineServiceMessageHandlerImpl(ApplicationContext context, CommandHandlerService commandHandlerService, JmsSender jmsSender) {
        this.context = context;
        this.commandHandlerService = commandHandlerService;
        this.jmsSender = jmsSender;
    }

    @Override
    public void handleJmsEnvelope(JmsEnvelope envelope) throws Exception {
        IrcMessageEvent event = (IrcMessageEvent) envelope.getMessageIn().getPayLoadObject("EVENT");
        if (event == null) {

            ServiceRequest serviceRequest = (ServiceRequest) envelope.getMessageIn().getPayLoadObject("ENGINE_REQUEST");
            if (serviceRequest == null) {
                log.debug("Nothing to do!");
                return;
            }
            event = serviceRequest.getIrcMessageEvent();
        }

        CmdHandlerMatches matches = commandHandlerService.getMatchingCommands(event.getMessage());
        if (matches.getMatches().size() > 0) {
            if (matches.getMatches().size() == 1) {
                Cmd handler = matches.getMatches().get(0);
                executeHandler(event, handler, envelope);
            } else {
                EngineResponse response = new EngineResponse(event);
                String multiple = matches.getFirstWord() + " multiple matches: ";
                for (Cmd match : matches.getMatches()) {
                    multiple += match.getName() + " ";
                }
                response.addResponse(multiple);
                sendReply(response, envelope);
            }
        }
    }

    private void sendReply(EngineResponse response, JmsEnvelope envelope) {
//    log.debug("Sending response: {}", response);
        if (response.getIrcMessageEvent().isWebMessage()) {
            envelope.getMessageOut().addPayLoadObject("SERVICE_RESPONSE", response);
        } else {
            jmsSender.send(HokanModule.HokanIo.getQueueName(), "ENGINE_RESPONSE", response, false);
        }
    }

    private void executeHandler(IrcMessageEvent event, Cmd handler, JmsEnvelope envelope) {
        EngineResponse response = new EngineResponse(event);
        response.setIsEngineRequest(event.isWebMessage());

        InternalRequest internalRequest;
        internalRequest = context.getBean(InternalRequest.class);
        internalRequest.setJmsEnvelope(envelope);
        try {
            internalRequest.init(event);
            internalRequest.getUserChannel().setLastCommand(event.getMessage());
            internalRequest.getUserChannel().setLastCommandTime(new Date());
            internalRequest.saveUserChannel();
            handler.handleLine(internalRequest, response);
        } catch (Exception e) {
            log.error("Command handler returned exception {}", e);
        }
    }

}
