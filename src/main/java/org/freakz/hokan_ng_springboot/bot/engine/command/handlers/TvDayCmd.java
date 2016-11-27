package org.freakz.hokan_ng_springboot.bot.engine.command.handlers;

import com.martiansoftware.jsap.JSAPResult;
import lombok.extern.slf4j.Slf4j;
import org.freakz.hokan_ng_springboot.bot.common.events.EngineResponse;
import org.freakz.hokan_ng_springboot.bot.common.events.InternalRequest;
import org.freakz.hokan_ng_springboot.bot.common.events.ServiceRequestType;
import org.freakz.hokan_ng_springboot.bot.common.events.ServiceResponse;
import org.freakz.hokan_ng_springboot.bot.common.exception.HokanException;
import org.freakz.hokan_ng_springboot.bot.common.models.TelkkuProgram;
import org.freakz.hokan_ng_springboot.bot.common.util.StringStuff;
import org.freakz.hokan_ng_springboot.bot.engine.command.HelpGroup;
import org.freakz.hokan_ng_springboot.bot.engine.command.annotation.HelpGroups;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * User: petria
 * Date: 12/13/13
 * Time: 8:43 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Scope("prototype")
@Slf4j
@HelpGroups(
        helpGroups = {HelpGroup.TV}
)
public class TvDayCmd extends Cmd {

    public TvDayCmd() {
        super();
        setHelp("Is there something interesting (=notified) programs coming from TV?");
    }

    @Override
    public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {

        ServiceResponse serviceResponse = doServicesRequest(ServiceRequestType.TV_DAY_REQUEST, request.getIrcEvent(), request.getChannel(), new Date());
        List<TelkkuProgram> daily = serviceResponse.getTvDayData();
        if (daily.size() == 0) {
            response.addResponse("Nothing interesting in TV today!");
            return;
        }
        String lastChannelStr = "";
        int i = 0;
        String reply = "Programs on TV notify today: ";
        for (TelkkuProgram prg : daily) {
            String channelStr = "[" + prg.getChannel() + "] ";
            if (channelStr.equalsIgnoreCase(lastChannelStr)) {
                lastChannelStr = channelStr;
                channelStr = "";
            } else {
                lastChannelStr = channelStr;
            }
            reply += String.format("%d) %s%s: %s(%d) ", i + 1, channelStr, StringStuff.formatTime(prg.getStartTimeD(), StringStuff.STRING_STUFF_DF_HHMM), prg.getProgram(), prg.getId());
            i++;
        }
        response.addResponse(reply);
    }

}
