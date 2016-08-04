package org.freakz.hokan_ng_springboot.bot.command.handlers;

import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import org.freakz.hokan_ng_springboot.bot.events.EngineResponse;
import org.freakz.hokan_ng_springboot.bot.events.InternalRequest;
import org.freakz.hokan_ng_springboot.bot.events.ServiceRequestType;
import org.freakz.hokan_ng_springboot.bot.events.ServiceResponse;
import org.freakz.hokan_ng_springboot.bot.exception.HokanException;
import org.freakz.hokan_ng_springboot.bot.models.MetarData;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.freakz.hokan_ng_springboot.bot.util.StaticStrings.ARG_STATION;

/**
 * Created by Petri Airio on 22.4.2015.
 *
 *
 *
 */
@Component
@Scope("prototype")
public class MetarCmd extends Cmd {

  public MetarCmd() {
    super();
    setHelp("Queries Metar weather datas. See: http://en.wikipedia.org/wiki/METAR");

    UnflaggedOption opt = new UnflaggedOption(ARG_STATION)
        .setRequired(true)
        .setGreedy(false);
    registerParameter(opt);

  }

/*  @Override
  public String getMatchPattern() {
    return "!metar.*";
  }*/


  @Override
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    String station = results.getString(ARG_STATION);
    ServiceResponse serviceResponse = doServicesRequest(ServiceRequestType.METAR_REQUEST, request.getIrcEvent(), station);
    List<MetarData> metarDatas = serviceResponse.getMetarResponse();
    if (metarDatas.size() > 0) {
      StringBuilder sb = new StringBuilder();
      for (MetarData metarData : metarDatas) {
        if (sb.length() > 0) {
          sb.append(" | ");
        }
        sb.append(metarData.getMetarData());
      }
      response.addResponse(sb.toString());
    } else {
      response.addResponse("No Metar data found with: " + station);
    }
  }

}
