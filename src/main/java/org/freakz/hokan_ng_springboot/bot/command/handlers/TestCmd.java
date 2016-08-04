package org.freakz.hokan_ng_springboot.bot.command.handlers;

import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import lombok.extern.slf4j.Slf4j;
import org.freakz.hokan_ng_springboot.bot.events.EngineResponse;
import org.freakz.hokan_ng_springboot.bot.events.InternalRequest;
import org.freakz.hokan_ng_springboot.bot.exception.HokanException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.freakz.hokan_ng_springboot.bot.util.StaticStrings.ARG_TEXT;

/**
 * Created by Petri Airio on 15.1.2016.
 * -
 */
@Component
@Scope("prototype")
@Slf4j
public class TestCmd extends Cmd {
  public TestCmd() {
    super();
    UnflaggedOption flg = new UnflaggedOption(ARG_TEXT)
        .setRequired(true)
        .setGreedy(false);
    registerParameter(flg);

  }

  @Override
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    String text = results.getString(ARG_TEXT);
    sendToUi(String.format("<%10s> %s: %s", request.getChannel().getChannelName(), request.getUser().getNick(), text));
    response.addResponse("Sent: %s", text);
  }

}
