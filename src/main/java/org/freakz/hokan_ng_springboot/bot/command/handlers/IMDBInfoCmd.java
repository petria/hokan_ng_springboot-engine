package org.freakz.hokan_ng_springboot.bot.command.handlers;

import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import com.omertron.omdbapi.model.OmdbVideoFull;
import lombok.extern.slf4j.Slf4j;
import org.freakz.hokan_ng_springboot.bot.command.HelpGroup;
import org.freakz.hokan_ng_springboot.bot.command.annotation.HelpGroups;
import org.freakz.hokan_ng_springboot.bot.events.EngineResponse;
import org.freakz.hokan_ng_springboot.bot.events.InternalRequest;
import org.freakz.hokan_ng_springboot.bot.events.ServiceRequestType;
import org.freakz.hokan_ng_springboot.bot.events.ServiceResponse;
import org.freakz.hokan_ng_springboot.bot.exception.HokanException;
import org.freakz.hokan_ng_springboot.bot.models.IMDBDetails;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.freakz.hokan_ng_springboot.bot.util.StaticStrings.ARG_TEXT;

/**
 * Created by Petri Airio on 20.11.2015.
 * -
 */
@Component
@Scope("prototype")
@Slf4j
@HelpGroups(
    helpGroups = {HelpGroup.DATA_FETCHERS}
)
public class IMDBInfoCmd extends Cmd {

  public IMDBInfoCmd() {

    super();
    setHelp("Queries IMDB database and shows detailed info about IMDB item.");

    UnflaggedOption flg = new UnflaggedOption(ARG_TEXT)
        .setRequired(true)
        .setGreedy(false);
    registerParameter(flg);

  }

  @Override
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    String text = results.getString(ARG_TEXT);
    ServiceResponse serviceResponse = doServicesRequest(ServiceRequestType.IMDB_DETAILED_INFO_REQUEST, request.getIrcEvent(), text);
    IMDBDetails details = serviceResponse.getIMDBDetails();
    if (details.getDetails() != null) {
      OmdbVideoFull omdb = details.getDetails();
      String runtime = omdb.getRuntime();
      String rate = omdb.getImdbRating();
      String votes = omdb.getImdbVotes();
      String director = omdb.getDirector();
      String imdbUrl = "http://www.imdb.com/title/" + omdb.getImdbID();
      response.addResponse("[%s] \"%s\", director %s, year %s, %s, rate %s (%s votes), plot: \"%s\" -> %s\n",
          omdb.getType(), omdb.getTitle(), director, omdb.getYear(), runtime, rate, votes, omdb.getPlot(), imdbUrl);
    } else {
      response.addResponse("Nothing found with: %s", text);
    }
  }
}
