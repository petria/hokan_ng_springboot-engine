package org.freakz.hokan_ng_springboot.bot.command.handlers;

import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import lombok.extern.slf4j.Slf4j;
import org.freakz.hokan_ng_springboot.bot.command.HelpGroup;
import org.freakz.hokan_ng_springboot.bot.command.annotation.HelpGroups;
import org.freakz.hokan_ng_springboot.bot.events.EngineResponse;
import org.freakz.hokan_ng_springboot.bot.events.InternalRequest;
import org.freakz.hokan_ng_springboot.bot.exception.HokanException;
import org.freakz.hokan_ng_springboot.bot.jpa.entity.ChannelStats;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.freakz.hokan_ng_springboot.bot.util.StaticStrings.ARG_TOPIC;

/**
 * Created by Petri Airio on 21.9.2015.
 *
 */
@Component
@Scope("prototype")
@Slf4j
@HelpGroups(
    helpGroups = {HelpGroup.CHANNELS}
)
public class TopicCmd extends Cmd {

  public TopicCmd() {
    super();
    setHelp("Shows or sets channel topic.");

    UnflaggedOption flg = new UnflaggedOption(ARG_TOPIC)
        .setRequired(false)
        .setGreedy(false);
    registerParameter(flg);

  }

  @Override
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    ChannelStats channelStats = request.getChannelStats();
    String topic = results.getString(ARG_TOPIC);
    if (topic == null) {
      response.addResponse("'%s' set by %s on %s", channelStats.getTopicSet(), channelStats.getTopicSetBy(), channelStats.getTopicSetDate());
    } else {
      String newTopic;
      if (topic.startsWith("+")) {
        newTopic = channelStats.getTopicSet() + " | " + topic.substring(1);
      } else {
        newTopic = topic;
      }
      response.addEngineMethodCall("setTopic", request.getChannel().getChannelName(), newTopic);
    }
  }

}
