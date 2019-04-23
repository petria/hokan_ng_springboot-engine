package org.freakz.hokan_ng_springboot.bot.engine.command.handlers;

import com.martiansoftware.jsap.JSAPResult;
import org.freakz.hokan_ng_springboot.bot.common.events.EngineResponse;
import org.freakz.hokan_ng_springboot.bot.common.events.InternalRequest;
import org.freakz.hokan_ng_springboot.bot.common.exception.HokanException;
import org.freakz.hokan_ng_springboot.bot.engine.command.HelpGroup;
import org.freakz.hokan_ng_springboot.bot.engine.command.annotation.HelpGroups;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by Petri Airio on 26.8.2015.
 * -
 */
@Component
@HelpGroups(
        helpGroups = {HelpGroup.DATA_COLLECTION, HelpGroup.GLUGGA}
)
@Scope("prototype")

public class MyGlCmd extends Cmd {

    public MyGlCmd() {
        super();
        setHelp("Channel top gluggas.");
        setChannelOnly(true);
    }

    @Override
    public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
        String channel = request.getIrcEvent().getChannel().toLowerCase();
        String network = request.getIrcEvent().getNetwork().toLowerCase();
        String key = "GLUGGA_COUNT";
        String nick = request.getIrcEvent().getSender().toLowerCase();

        StringBuilder sb = new StringBuilder();

        String gluggas = dataValuesService.getValue(nick, channel, network, key);
        if (gluggas == null) {
            sb.append(nick);
            sb.append(", you need to start *glugga* ASAP!!");
        } else {
            sb.append(nick);
            sb.append(", your *glugga* count so far: ");
            sb.append(gluggas);
        }
        response.addResponse(sb.toString());
    }
}
