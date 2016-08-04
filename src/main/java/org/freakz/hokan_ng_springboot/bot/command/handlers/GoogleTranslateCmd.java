package org.freakz.hokan_ng_springboot.bot.command.handlers;

import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import org.freakz.hokan_ng_springboot.bot.events.EngineResponse;
import org.freakz.hokan_ng_springboot.bot.events.InternalRequest;
import org.freakz.hokan_ng_springboot.bot.events.ServiceRequestType;
import org.freakz.hokan_ng_springboot.bot.events.ServiceResponse;
import org.freakz.hokan_ng_springboot.bot.exception.HokanException;

import static org.freakz.hokan_ng_springboot.bot.util.StaticStrings.ARG_TEXT;

/**
 * Created by Petri Airio (petri.j.airio@gmail.com) on 29.4.2015.
 *
 */
//@Component
//@Scope("prototype")
//@Slf4j
public class GoogleTranslateCmd extends Cmd {


  public GoogleTranslateCmd() {
    super();
    setHelp("Translate using Google API");

    UnflaggedOption flg = new UnflaggedOption(ARG_TEXT)
        .setRequired(true)
        .setGreedy(false);
    registerParameter(flg);
  }

/*  @Override
  public String getMatchPattern() {
    return "!googletrans.*|!gtrans.*";
  }*/

  @Override
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {

    String text = results.getString(ARG_TEXT);
    ServiceResponse serviceResponse = doServicesRequest(ServiceRequestType.TRANSLATE_REQUEST, request.getIrcEvent(), text);
    response.addResponse("%s", serviceResponse.getResponseData("TRANSLATE_RESPONSE"));
  }
}
