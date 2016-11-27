package org.freakz.hokan_ng_springboot.bot.command.handlers;

import com.martiansoftware.jsap.FlaggedOption;
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
import org.freakz.hokan_ng_springboot.bot.common.models.KelikameratWeatherData;
import org.freakz.hokan_ng_springboot.bot.common.util.StringStuff;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import static org.freakz.hokan_ng_springboot.bot.common.util.StaticStrings.ARG_COUNT;
import static org.freakz.hokan_ng_springboot.bot.common.util.StaticStrings.ARG_PLACE;

/**
 * User: petria
 * Date: 11/18/13
 * Time: 9:07 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Scope("prototype")
@HelpGroups(
        helpGroups = {HelpGroup.DATA_FETCHERS}
)
public class WeatherCmd extends Cmd {

    public WeatherCmd() {

        setHelp("Queries weather from http://alk.tiehallinto.fi/alk/tiesaa/");

        FlaggedOption flg = new FlaggedOption(ARG_COUNT)
                .setStringParser(JSAP.INTEGER_PARSER)
                .setDefault("5")
                .setShortFlag('c');
        registerParameter(flg);

        UnflaggedOption opt = new UnflaggedOption(ARG_PLACE)
                .setDefault("Jyväskylä")
                .setRequired(true)
                .setGreedy(false);
        registerParameter(opt);

    }

/*  @Override
  public String getMatchPattern() {
    return "!saa.*|!weather.*";
  }*/

    @Override
//  @SuppressWarnings("unchecked")
    public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {

        String place = results.getString(ARG_PLACE).toLowerCase();

        ServiceResponse serviceResponse = doServicesRequest(ServiceRequestType.WEATHER_REQUEST, request.getIrcEvent(), ".*");
        List<KelikameratWeatherData> datas = serviceResponse.getWeatherResponse();
        if (datas.size() == 0) {
            response.setResponseMessage("Weather data not ready yet!");
            return;
        }

        StringBuilder sb = new StringBuilder();

        if (place.equals("minmax")) {

            KelikameratWeatherData max = datas.get(0);
            KelikameratWeatherData min = datas.get(datas.size() - 1);

            sb.append("Min: ");
            sb.append(StringStuff.formatWeather(min));
            sb.append(" Max: ");
            sb.append(StringStuff.formatWeather(max));

        } else {

            int xx = 0;
            String regexp = ".*" + place + ".*";
            for (KelikameratWeatherData wd : datas) {
                String placeFromUrl = wd.getPlaceFromUrl();
                String stationFromUrl = wd.getUrl().getStationUrl();
                if (StringStuff.match(placeFromUrl, regexp) || StringStuff.match(stationFromUrl, regexp)) {
                    if (wd.getAir() == null) {
                        continue;
                    }
                    if (xx != 0) {
                        sb.append(", ");
                    }
                    sb.append(StringStuff.formatWeather(wd));
                    xx++;
                    if (xx > results.getInt(ARG_COUNT)) {
                        break;
                    }
                }
            }
            if (xx == 0) {
                String hhmmss = StringStuff.formatTime(new Date(), StringStuff.STRING_STUFF_DF_HHMMSS);
                sb.append(String.format("%s %s 26.7°C, hellettä pukkaa!", hhmmss, place));
            }
        }

        response.setResponseMessage(sb.toString());
    }
}
