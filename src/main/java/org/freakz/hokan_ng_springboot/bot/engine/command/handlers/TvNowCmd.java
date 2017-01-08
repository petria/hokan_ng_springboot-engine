package org.freakz.hokan_ng_springboot.bot.engine.command.handlers;

import com.martiansoftware.jsap.JSAPResult;
import org.freakz.hokan_ng_springboot.bot.common.events.EngineResponse;
import org.freakz.hokan_ng_springboot.bot.common.events.InternalRequest;
import org.freakz.hokan_ng_springboot.bot.common.events.ServiceRequestType;
import org.freakz.hokan_ng_springboot.bot.common.events.ServiceResponse;
import org.freakz.hokan_ng_springboot.bot.common.exception.HokanException;
import org.freakz.hokan_ng_springboot.bot.common.models.TelkkuProgram;
import org.freakz.hokan_ng_springboot.bot.common.models.TvNowData;
import org.freakz.hokan_ng_springboot.bot.common.util.StringStuff;
import org.freakz.hokan_ng_springboot.bot.engine.command.HelpGroup;
import org.freakz.hokan_ng_springboot.bot.engine.command.annotation.HelpGroups;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * User: petria
 * Date: 11/26/13
 * Time: 12:53 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Scope("prototype")
@HelpGroups(
        helpGroups = {HelpGroup.TV}
)
public class TvNowCmd extends Cmd {

    public TvNowCmd() {
        setHelp("Shows what's going on in TV. Needs package xmltv to be installed on the host where the Bot is running.");
    }

    @Override
    public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {

        String currentLine = "";
        String nextLine = "";
        ServiceResponse serviceResponse = doServicesRequest(ServiceRequestType.TV_NOW_REQUEST, request.getIrcEvent(), (Object[]) null);
        TvNowData tvNowData = serviceResponse.getTvNowData();


        for (String channel : tvNowData.getChannels()) {
            TelkkuProgram current = tvNowData.getCurrentProgram(channel);
            TelkkuProgram next = tvNowData.getNextProgram(channel);

            if (current != null) {
                currentLine +=
                        "[" + channel + "] " +
                                StringStuff.formatTime(current.getStartTimeD(), StringStuff.STRING_STUFF_DF_HHMM) +
                                " " + current.getProgram() + "(" + current.getId() + ") ";
            }

            if (next != null) {
                nextLine +=
                        "[" + channel + "] " +
                                StringStuff.formatTime(next.getStartTimeD(), StringStuff.STRING_STUFF_DF_HHMM) +
                                " " + next.getProgram() + "(" + next.getId() + ") ";
            }

        }

        response.addResponse(currentLine + "\n");
        response.addResponse(nextLine);
    }
}