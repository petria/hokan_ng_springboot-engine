package org.freakz.hokan_ng_springboot.bot.command.handlers;

import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import org.freakz.hokan_ng_springboot.bot.events.EngineResponse;
import org.freakz.hokan_ng_springboot.bot.events.InternalRequest;
import org.freakz.hokan_ng_springboot.bot.events.ServiceRequestType;
import org.freakz.hokan_ng_springboot.bot.events.ServiceResponse;
import org.freakz.hokan_ng_springboot.bot.exception.HokanException;
import org.freakz.hokan_ng_springboot.bot.models.HoroHolder;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.freakz.hokan_ng_springboot.bot.util.StaticStrings.ARG_HORO;

/**
 * Created by Petri Airio (petri.j.airio@gmail.com) on 26.4.2015.
 *
 */
@Component
@Scope("prototype")
public class HoroCmd extends Cmd {

  public HoroCmd() {
    super();
    UnflaggedOption opt = new UnflaggedOption(ARG_HORO)
        .setRequired(true)
        .setGreedy(false);
    registerParameter(opt);

  }

/*  @Override
  public String getMatchPattern() {
    return "!horo.*";
  }*/


  @Override
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    String horo = results.getString(ARG_HORO);
    ServiceResponse serviceResponse = doServicesRequest(ServiceRequestType.HORO_REQUEST, request.getIrcEvent(), horo);
    HoroHolder hh = serviceResponse.getHoroResponse();
    if (hh != null) {
      response.setResponseMessage(hh.toString());
    } else {
      response.setResponseMessage("Saat dildoo perään ja et pääse pylsimään!");
    }

  }

}
