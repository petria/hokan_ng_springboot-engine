package org.freakz.hokan_ng_springboot.bot.command.handlers;

import com.martiansoftware.jsap.JSAPResult;
import lombok.extern.slf4j.Slf4j;
import org.freakz.hokan_ng_springboot.bot.command.HelpGroup;
import org.freakz.hokan_ng_springboot.bot.command.annotation.HelpGroups;
import org.freakz.hokan_ng_springboot.bot.events.EngineResponse;
import org.freakz.hokan_ng_springboot.bot.events.InternalRequest;
import org.freakz.hokan_ng_springboot.bot.exception.HokanException;
import org.freakz.hokan_ng_springboot.bot.models.StatsData;
import org.freakz.hokan_ng_springboot.bot.models.StatsMapper;
import org.freakz.hokan_ng_springboot.bot.util.StringStuff;
import org.joda.time.DateTime;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Petri Airio on 24.8.2015.
 */
@Component
@Scope("prototype")
@Slf4j
@HelpGroups(
        helpGroups = {HelpGroup.DATA_COLLECTION}
)
public class DailyStatsCmd extends Cmd {

    public DailyStatsCmd() {
        super();
        setHelp("Show daily stats, words written to specific channel.");
    }

    @Override
    public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
        DateTime today = DateTime.now();
        StatsMapper statsMapper = statsService.getDailyStatsForChannel(today, request.getChannel().getChannelName());
        if (!statsMapper.hasError()) {
            List<StatsData> statsDatas = statsMapper.getStatsData();
            String res = StringStuff.formatTime(today.toDate(), StringStuff.STRING_STUFF_DF_DDMMYYYY) + " top words:";
            int i = 1;
            for (StatsData statsData : statsDatas) {
                res += " " + i + ") " + statsData.getNick() + "=" + statsData.getWords();
                i++;
            }
            response.addResponse(res);
        } else {
            response.addResponse(statsMapper.getError());
        }
    }

}
