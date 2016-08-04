package org.freakz.hokan_ng_springboot.bot.command.handlers;

import com.martiansoftware.jsap.JSAPResult;
import lombok.extern.slf4j.Slf4j;
import org.freakz.hokan_ng_springboot.bot.events.EngineResponse;
import org.freakz.hokan_ng_springboot.bot.events.InternalRequest;
import org.freakz.hokan_ng_springboot.bot.events.ServiceRequestType;
import org.freakz.hokan_ng_springboot.bot.events.ServiceResponse;
import org.freakz.hokan_ng_springboot.bot.exception.HokanException;
import org.freakz.hokan_ng_springboot.bot.models.NimipaivaData;
import org.freakz.hokan_ng_springboot.bot.util.StringStuff;
import org.joda.time.DateTime;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by Petri Airio on 8.10.2015.
 *
 */
@Component
@Scope("prototype")
@Slf4j
public class TodayCmd extends Cmd {

  public TodayCmd() {
    super();
    setHelp("What day it is ?");
  }

  @Override
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    DateTime dateTime = DateTime.now();
    ServiceResponse serviceResponse = doServicesRequest(ServiceRequestType.NIMIPAIVA_DAY_REQUEST, request.getIrcEvent(), dateTime);
    NimipaivaData names = serviceResponse.getNimipaivaDayResponse();
    int week = DateTime.now().getWeekOfWeekyear();
    StringBuilder sb = new StringBuilder("Today is " + StringStuff.formatTime(names.getDay().toDate(), StringStuff.STRING_STUFF_DF_DDMMYYYY) + " (W: " +week + ") ::");
    for (int i = 0; i < names.getNames().size(); i++) {
      if (i > 0) {
        sb.append(", ");
      }
      sb.append(names.getNames().get(i));
    }
    response.addResponse(sb.toString());
  }
}
