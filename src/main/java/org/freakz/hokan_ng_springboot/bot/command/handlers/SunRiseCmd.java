package org.freakz.hokan_ng_springboot.bot.command.handlers;

import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import org.freakz.hokan_ng_springboot.bot.command.HelpGroup;
import org.freakz.hokan_ng_springboot.bot.command.annotation.HelpGroups;
import org.freakz.hokan_ng_springboot.bot.events.EngineResponse;
import org.freakz.hokan_ng_springboot.bot.events.InternalRequest;
import org.freakz.hokan_ng_springboot.bot.events.ServiceRequestType;
import org.freakz.hokan_ng_springboot.bot.events.ServiceResponse;
import org.freakz.hokan_ng_springboot.bot.exception.HokanException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.freakz.hokan_ng_springboot.bot.util.StaticStrings.ARG_CITY;

/**
 * Created by Petri Airio on 24.9.2015.
 */
@Component
@Scope("prototype")
@HelpGroups(
        helpGroups = {HelpGroup.DATA_FETCHERS}
)
public class SunRiseCmd extends Cmd {

    public SunRiseCmd() {
        super();
        setHelp("Queries Sun rise / set times from Ilmatieteenlaitos web pages.");

        UnflaggedOption unflaggedOption = new UnflaggedOption(ARG_CITY)
                .setRequired(true)
                .setGreedy(false);
        registerParameter(unflaggedOption);

    }

    @Override
    public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
        String city = results.getString(ARG_CITY);

        ServiceResponse serviceResponse
                = doServicesRequest(ServiceRequestType.SUNRISE_SERVICE_REQUEST, request.getIrcEvent(), city);

        String sunrise = serviceResponse.getSunRiseResponse();

        if (sunrise == null) {
            response.addResponse("No Sunrise data found with: %s", city);
        } else {
            response.addResponse(sunrise);
        }

    }

}
