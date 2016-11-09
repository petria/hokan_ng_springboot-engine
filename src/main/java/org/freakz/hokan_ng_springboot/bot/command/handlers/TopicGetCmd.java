package org.freakz.hokan_ng_springboot.bot.command.handlers;

import com.martiansoftware.jsap.JSAPResult;
import lombok.extern.slf4j.Slf4j;
import org.freakz.hokan_ng_springboot.bot.command.HelpGroup;
import org.freakz.hokan_ng_springboot.bot.command.annotation.HelpGroups;
import org.freakz.hokan_ng_springboot.bot.events.EngineResponse;
import org.freakz.hokan_ng_springboot.bot.events.InternalRequest;
import org.freakz.hokan_ng_springboot.bot.events.ServiceRequestType;
import org.freakz.hokan_ng_springboot.bot.events.ServiceResponse;
import org.freakz.hokan_ng_springboot.bot.exception.HokanException;
import org.freakz.hokan_ng_springboot.bot.models.ChannelSetTopic;
import org.freakz.hokan_ng_springboot.bot.util.StringStuff;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by Petri Airio on 21.9.2015.
 * -
 */
@Component
@Scope("prototype")
@Slf4j
@HelpGroups(
        helpGroups = {HelpGroup.CHANNELS}
)
public class TopicGetCmd extends Cmd {

    public TopicGetCmd() {
        setHelp("Shows channel topic.");
    }

    @Override
    public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
        ServiceResponse serviceResponse = doServicesRequest(ServiceRequestType.CHANNEL_TOPIC_GET_REQUEST, request.getIrcEvent(), "");
        ChannelSetTopic setTopic = serviceResponse.getChannelSetTopic();
        if (setTopic != null) {
            response.addResponse("Topic '%s' set by %s at %s.", setTopic.getTopic(), setTopic.getSender(), StringStuff.formatNiceDate(setTopic.getTimestamp(), true, false));
        } else {
            response.addResponse("n/a");
        }
    }

}
