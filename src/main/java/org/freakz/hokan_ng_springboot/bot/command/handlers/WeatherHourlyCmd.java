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
import org.freakz.hokan_ng_springboot.bot.models.HourlyWeatherData;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.freakz.hokan_ng_springboot.bot.util.StaticStrings.ARG_PLACE;

/**
 * Created by Petri Airio on 11.8.2016.
 * -
 */
@Component
@Scope("prototype")
@HelpGroups(
    helpGroups = {HelpGroup.DATA_FETCHERS}
)
public class WeatherHourlyCmd extends Cmd {

  public WeatherHourlyCmd() {

    UnflaggedOption opt = new UnflaggedOption(ARG_PLACE)
        .setDefault("Jyväskylä")
        .setRequired(true)
        .setGreedy(false);
    registerParameter(opt);

    setHelp("Queries weather from http://alk.tiehallinto.fi/alk/tiesaa/");


  }

  @Override
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    String place = results.getString(ARG_PLACE).toLowerCase();

    ServiceResponse serviceResponse = doServicesRequest(ServiceRequestType.ILMATIETEENLAITOS_HOURLY_REQUEST, request.getIrcEvent(), place);
    HourlyWeatherData hourlyWeatherData = serviceResponse.getHourlyWeatherData();

    if (hourlyWeatherData.getTimes() != null) {
      StringBuilder sb = new StringBuilder();
      sb.append("Hourly forecast: ");
      sb.append(place);
      sb.append("\n");
      String hours = "";
      String temps = "";
      for (int i = 0; i < hourlyWeatherData.getTimes().length; i++) {
        hours += String.format("%4s", hourlyWeatherData.getTimes()[i]);
        temps += String.format("%4s", hourlyWeatherData.getTemperatures()[i]);
      }
      sb.append(hours);
      sb.append("\n");
      sb.append(temps);
      sb.append("\n");
      response.addResponse("%s", sb.toString());

    } else {

      response.addResponse("Nothing found: %s, use whole city names!", place);

    }
  }

}
