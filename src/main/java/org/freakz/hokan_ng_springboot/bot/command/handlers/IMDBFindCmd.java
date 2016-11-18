package org.freakz.hokan_ng_springboot.bot.command.handlers;

import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import lombok.extern.slf4j.Slf4j;
import org.freakz.hokan_ng_springboot.bot.command.HelpGroup;
import org.freakz.hokan_ng_springboot.bot.command.annotation.HelpGroups;
import org.freakz.hokan_ng_springboot.bot.events.EngineResponse;
import org.freakz.hokan_ng_springboot.bot.events.InternalRequest;
import org.freakz.hokan_ng_springboot.bot.events.ServiceRequestType;
import org.freakz.hokan_ng_springboot.bot.events.ServiceResponse;
import org.freakz.hokan_ng_springboot.bot.exception.HokanException;
import org.freakz.hokan_ng_springboot.bot.models.IMDBSearchResults;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.freakz.hokan_ng_springboot.bot.util.StaticStrings.ARG_TEXT;

/**
 * Created by Petri Airio on 18.11.2015.
 * -
 */
@Component
@Scope("prototype")
@Slf4j
@HelpGroups(
        helpGroups = {HelpGroup.DATA_FETCHERS}
)
public class IMDBFindCmd extends Cmd {

    public IMDBFindCmd() {
        super();
        setHelp("Queries IMDB database using title search.");

        UnflaggedOption flg = new UnflaggedOption(ARG_TEXT)
                .setRequired(true)
                .setGreedy(false);
        registerParameter(flg);

    }

    @Override
    public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
        String text = results.getString(ARG_TEXT);
        ServiceResponse serviceResponse = doServicesRequest(ServiceRequestType.IMDB_TITLE_REQUEST, request.getIrcEvent(), text);
        IMDBSearchResults imdbSearchResults = serviceResponse.getIMDBTitleData();
/*        if (imdbSearchResults.getSearchResults() != null) {
            for (OmdbVideoBasic omdb : imdbSearchResults.getSearchResults()) {
                String imdbURL = String.format("http://www.imdb.com/title/%s/", omdb.getImdbID());
                response.addResponse("[%7s] %25s :: \"%s\" (%s)\n", omdb.getType(), imdbURL, omdb.getTitle(), omdb.getYear());
            }
        } else {
            response.addResponse("Nothing found with: %s", text);
        }
        TODO
        */
    }
}
