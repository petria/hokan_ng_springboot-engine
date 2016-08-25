package org.freakz.hokan_ng_springboot.bot.command.handlers;

import com.martiansoftware.jsap.JSAPResult;
import lombok.extern.slf4j.Slf4j;
import org.freakz.hokan_ng_springboot.bot.command.HelpGroup;
import org.freakz.hokan_ng_springboot.bot.command.annotation.HelpGroups;
import org.freakz.hokan_ng_springboot.bot.enums.LunchPlace;
import org.freakz.hokan_ng_springboot.bot.events.EngineResponse;
import org.freakz.hokan_ng_springboot.bot.events.InternalRequest;
import org.freakz.hokan_ng_springboot.bot.events.ServiceRequestType;
import org.freakz.hokan_ng_springboot.bot.events.ServiceResponse;
import org.freakz.hokan_ng_springboot.bot.exception.HokanException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Petri Airio on 27.1.2016.
 * -
 */
@Component
@Scope("prototype")
@Slf4j
@HelpGroups(
        helpGroups = {HelpGroup.DATA_FETCHERS, HelpGroup.LUNCH}
)
public class LunchPlacesCmd extends Cmd {

    public LunchPlacesCmd() {
        super();
        setHelp("Shows available Lunch places where menu lists are fetched.");
    }

    @Override
    public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
        ServiceResponse serviceResponse = doServicesRequest(ServiceRequestType.LUNCH_PLACES_REQUEST, request.getIrcEvent(), "");
        List<LunchPlace> lunchPlaces = serviceResponse.getLunchPlacesResponse();
        if (lunchPlaces.size() == 0) {
            response.addResponse("No lunch places!!");
        } else {
            String places = "I know following lunch places: ";
            for (LunchPlace lunchPlace : lunchPlaces) {
                places += "  " + lunchPlace.getName();
            }
            response.addResponse("%s", places);
        }
    }

}
