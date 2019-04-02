package org.freakz.hokan_ng_springboot.bot.engine.command.handlers;

import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import org.freakz.hokan_ng_springboot.bot.common.events.EngineResponse;
import org.freakz.hokan_ng_springboot.bot.common.events.InternalRequest;
import org.freakz.hokan_ng_springboot.bot.common.events.ServiceRequestType;
import org.freakz.hokan_ng_springboot.bot.common.events.ServiceResponse;
import org.freakz.hokan_ng_springboot.bot.common.exception.HokanException;
import org.freakz.hokan_ng_springboot.bot.common.models.CityData;
import org.freakz.hokan_ng_springboot.bot.common.models.HourlyWeatherData;
import org.freakz.hokan_ng_springboot.bot.common.service.cityresolver.CityResolver;
import org.freakz.hokan_ng_springboot.bot.engine.command.HelpGroup;
import org.freakz.hokan_ng_springboot.bot.engine.command.annotation.HelpGroups;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.freakz.hokan_ng_springboot.bot.common.util.StaticStrings.ARG_PLACE;

/**
 * Created by Petri Airio on 11.8.2016.
 *
 */
@Component
@Scope("prototype")
@HelpGroups(
        helpGroups = {HelpGroup.DATA_FETCHERS}
)
public class WeatherHourlyCmd extends Cmd {

    @Autowired
    private CityResolver cityResolver;

    public WeatherHourlyCmd() {

        UnflaggedOption opt = new UnflaggedOption(ARG_PLACE)
                .setDefault("Oulu")
                .setRequired(true)
                .setGreedy(false);
        registerParameter(opt);

        setHelp("Queries weather from http://alk.tiehallinto.fi/alk/tiesaa/");


    }

    @Override
    public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
        String place = results.getString(ARG_PLACE).toLowerCase();
        CityData cityData = cityResolver.resolveCityNames(place);

        for (String city : cityData.getResolvedCityNames()) {

            ServiceResponse serviceResponse = doServicesRequest(ServiceRequestType.ILMATIETEENLAITOS_HOURLY_REQUEST, request.getIrcEvent(), city);
            HourlyWeatherData hourlyWeatherData = serviceResponse.getHourlyWeatherData();

            if (hourlyWeatherData != null && hourlyWeatherData.getTimes() != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("Hourly forecast: ");
                sb.append(city);
                sb.append("\n");
                String hours = "";
                String temps = "";
                String format = getFormat(hourlyWeatherData.getLongestTemp());
                for (int i = 0; i < hourlyWeatherData.getTimes().length; i++) {
                    hours += String.format(format, hourlyWeatherData.getTimes()[i]);
                    temps += String.format(format, hourlyWeatherData.getTemperatures()[i]);
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

    private String getFormat(int longestTemp) {
        String format = "%" + (longestTemp + 1) + "s";
        return format;
    }


}
