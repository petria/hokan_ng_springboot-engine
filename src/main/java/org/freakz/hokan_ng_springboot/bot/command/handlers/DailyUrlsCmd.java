package org.freakz.hokan_ng_springboot.bot.command.handlers;

import com.martiansoftware.jsap.JSAPResult;
import lombok.extern.slf4j.Slf4j;
import org.freakz.hokan_ng_springboot.bot.command.HelpGroup;
import org.freakz.hokan_ng_springboot.bot.command.annotation.HelpGroups;
import org.freakz.hokan_ng_springboot.bot.events.EngineResponse;
import org.freakz.hokan_ng_springboot.bot.events.InternalRequest;
import org.freakz.hokan_ng_springboot.bot.exception.HokanException;
import org.freakz.hokan_ng_springboot.bot.jpa.entity.Url;
import org.freakz.hokan_ng_springboot.bot.util.StringStuff;
import org.freakz.hokan_ng_springboot.bot.util.TimeUtil;
import org.joda.time.DateTime;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Petri Airio on 22.9.2015.
 */
@Component
@Scope("prototype")
@Slf4j
@HelpGroups(
        helpGroups = {HelpGroup.DATA_COLLECTION}
)
public class DailyUrlsCmd extends Cmd {

    public DailyUrlsCmd() {
        super();
        setHelp("Find urls pasted to channel in certain day. If no day is supplied today is used.");

        // TODO add DAY parameter

    }

    @Override
    public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
        DateTime time = DateTime.now();
        List<Url> urlList = urlLoggerService.findByCreatedBetweenAndChannel(TimeUtil.getStartAndEndTimeForDay(time), request.getChannel().getChannelName());
        if (urlList.size() > 0) {
            int shown = 0;
            String ret = null;

            for (Url row : urlList) {

                if (ret == null) {
                    ret = String.format("Daily URLs for date: %s\n", StringStuff.formatTime(time.toDate(), StringStuff.STRING_STUFF_DF_DDMMYYYY));
                }

                if (shown > 0) {
                    ret += " ";
                }

                shown++;

/*        if (shown == 5) {
          break;
        }*/

                ret += shown + ") " + row.getSender() + ": ";
                ret += row.getUrl();
/*        if (row.getUrlTitle() != null) {
          ret += " \"t: " + row.getUrlTitle() + "\"";
        }
        ret += " [" + StringStuff.formatNiceDate(row.getCreated(), false) + "]";*/
                response.addResponse(ret);
            }

        } else {
            response.addResponse("No urls found for date: %s", time.toString());
        }
    }

}
