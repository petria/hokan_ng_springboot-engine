package org.freakz.hokan_ng_springboot.bot.command.handlers;

import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import org.freakz.hokan_ng_springboot.bot.command.HelpGroup;
import org.freakz.hokan_ng_springboot.bot.command.annotation.HelpGroups;
import org.freakz.hokan_ng_springboot.bot.common.events.EngineResponse;
import org.freakz.hokan_ng_springboot.bot.common.events.InternalRequest;
import org.freakz.hokan_ng_springboot.bot.common.events.ServiceRequestType;
import org.freakz.hokan_ng_springboot.bot.common.events.ServiceResponse;
import org.freakz.hokan_ng_springboot.bot.common.exception.HokanException;
import org.freakz.hokan_ng_springboot.bot.common.models.TelkkuProgram;
import org.freakz.hokan_ng_springboot.bot.common.util.StringStuff;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;

import static org.freakz.hokan_ng_springboot.bot.common.util.StaticStrings.ARG_PROGRAM;

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
public class TvFindCmd extends Cmd {

    public TvFindCmd() {
        super();
        setHelp("Search TV programs.");

        UnflaggedOption opt = new UnflaggedOption(ARG_PROGRAM)
                .setRequired(true)
                .setGreedy(false);
        registerParameter(opt);

    }

    @Override
    public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
        String program = results.getString(ARG_PROGRAM);
        ServiceResponse serviceResponse = doServicesRequest(ServiceRequestType.TV_FIND_REQUEST, request.getIrcEvent(), program);
        List<TelkkuProgram> matching = serviceResponse.getTvFindData();
        if (matching.size() > 0) {
            String reply = "";
            String lastChannel = "";
            for (TelkkuProgram prg : matching) {
                SimpleDateFormat dateFormat;
                if (StringStuff.isDateToday(prg.getStartTimeD())) {
                    dateFormat = StringStuff.STRING_STUFF_DF_HHMM;
                } else {
                    dateFormat = StringStuff.STRING_STUFF_DF_DDMMHHMM;
                }

                String channel = prg.getChannel();
                if (!channel.equalsIgnoreCase(lastChannel)) {
                    reply += "[" + channel + "] ";
                }
                lastChannel = channel;
                reply += StringStuff.formatTime(prg.getStartTimeD(), dateFormat) +
                        " " + prg.getProgram() + "(" + prg.getId() + ") ";

            }
            response.addResponse(reply);
        } else {
            response.addResponse("No matching Telkku programs found with: " + program);
        }
    }
}
