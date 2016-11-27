package org.freakz.hokan_ng_springboot.bot.command.handlers;

import com.martiansoftware.jsap.JSAP;
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

import static org.freakz.hokan_ng_springboot.bot.common.util.StaticStrings.ARG_ID;

/**
 * User: petria
 * Date: 11/27/13
 * Time: 4:13 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Scope("prototype")
@HelpGroups(
        helpGroups = {HelpGroup.TV}
)
public class TvInfoCmd extends Cmd {

    public TvInfoCmd() {
        super();
        setHelp("Shows info description of given tv program ID.");

        UnflaggedOption opt = new UnflaggedOption(ARG_ID)
                .setStringParser(JSAP.INTEGER_PARSER)
                .setRequired(true)
                .setGreedy(false);
        registerParameter(opt);
    }

    @Override
    public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {

        int id = results.getInt(ARG_ID);
        ServiceResponse serviceResponse = doServicesRequest(ServiceRequestType.TV_INFO_REQUEST, request.getIrcEvent(), id);

        TelkkuProgram prg = serviceResponse.getTvInfoData();
        if (prg != null) {
            SimpleDateFormat dateFormat;
            if (StringStuff.isDateToday(prg.getStartTimeD())) {
                dateFormat = StringStuff.STRING_STUFF_DF_HHMM;
            } else {
                dateFormat = StringStuff.STRING_STUFF_DF_DDMMHHMM;
            }
            String reply = StringStuff.formatTime(prg.getStartTimeD(), dateFormat) +
                    " " + prg.getProgram() + ": " + prg.getDescription();
            response.addResponse(reply);
            return;
        }
        response.addResponse("No program found with id: " + id);
    }

}
