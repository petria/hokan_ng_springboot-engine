package org.freakz.hokan_ng_springboot.bot.command.handlers;

import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import lombok.extern.slf4j.Slf4j;
import org.freakz.hokan_ng_springboot.bot.command.HelpGroup;
import org.freakz.hokan_ng_springboot.bot.command.annotation.HelpGroups;
import org.freakz.hokan_ng_springboot.bot.events.EngineResponse;
import org.freakz.hokan_ng_springboot.bot.events.InternalRequest;
import org.freakz.hokan_ng_springboot.bot.exception.HokanException;
import org.freakz.hokan_ng_springboot.bot.models.StatsData;
import org.freakz.hokan_ng_springboot.bot.models.StatsMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.freakz.hokan_ng_springboot.bot.util.StaticStrings.ARG_CHANNEL;

/**
 * Created by Petri Airio on 26.8.2015.
 *
 */
@Component
@HelpGroups(
    helpGroups = {HelpGroup.SYSTEM}
)
@Scope("prototype")
@Slf4j
public class TopCmd extends Cmd {

  public TopCmd() {
    super();
    setHelp("Calculates top statistics from the logs.");

    UnflaggedOption uflg = new UnflaggedOption(ARG_CHANNEL)
        .setRequired(false)
        .setGreedy(false);
    registerParameter(uflg);

  }

  @Override
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    String channel = results.getString(ARG_CHANNEL, request.getChannel().getChannelName());
    StatsMapper statsMapper = statsService.getStatsForChannel(channel);
    if (!statsMapper.hasError()) {
      List<StatsData> statsDatas = statsMapper.getStatsData();
      String res = "Top words for channel " + channel + ": ";
      int i = 1;
      for (StatsData statsData : statsDatas) {
        res += " " + i + ") " + statsData.getNick() + ": " + statsData.getWords();
        i++;
      }
      response.addResponse(res);
    } else {
      response.addResponse(statsMapper.getError());
    }

  }
}
