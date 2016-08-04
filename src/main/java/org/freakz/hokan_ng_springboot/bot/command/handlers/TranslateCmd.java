package org.freakz.hokan_ng_springboot.bot.command.handlers;

import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import lombok.extern.slf4j.Slf4j;
import org.freakz.hokan_ng_springboot.bot.events.EngineResponse;
import org.freakz.hokan_ng_springboot.bot.events.InternalRequest;
import org.freakz.hokan_ng_springboot.bot.events.ServiceRequestType;
import org.freakz.hokan_ng_springboot.bot.events.ServiceResponse;
import org.freakz.hokan_ng_springboot.bot.exception.HokanException;
import org.freakz.hokan_ng_springboot.bot.models.TranslateData;
import org.freakz.hokan_ng_springboot.bot.models.TranslateResponse;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static org.freakz.hokan_ng_springboot.bot.util.StaticStrings.ARG_TEXT;

/**
 * Created by Petri Airio (petri.j.airio@gmail.com) on 12.11.2015.
 * -
 */
@Component
@Scope("prototype")
@Slf4j
public class TranslateCmd extends Cmd {

  public TranslateCmd() {
    super();
    setHelp("Translate FIN-ENG-FIN");

    UnflaggedOption flg = new UnflaggedOption(ARG_TEXT)
        .setRequired(true)
        .setGreedy(false);
    registerParameter(flg);
  }

/*  @Override
  public String getMatchPattern() {
    return "!trans.*|!translate.*";
  }
*/

  @Override
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {

    String text = results.getString(ARG_TEXT);
    ServiceResponse serviceResponse = doServicesRequest(ServiceRequestType.TRANSLATE_REQUEST, request.getIrcEvent(), text);
    TranslateResponse translateResponse = serviceResponse.getTranslateResponse();
    String responseText = "";
    for (Map.Entry<String, List<TranslateData>> entry : translateResponse.getWordMap().entrySet()) {

      String translations = "";
      for (TranslateData translateData : entry.getValue()) {
        if (translations.length() > 0) {
          translations += ", ";
        }
        translations += translateData.getTranslation();
      }
      if (translations.length() > 0) {
        if (responseText.length() > 0) {
          responseText += " || ";
        }
        responseText += String.format("%s :: %s", entry.getKey(), translations);
      }
    }
    if (responseText.length() == 0) {
      response.addResponse("%s :: n/a", text);
    } else {
      response.addResponse("%s", responseText);
    }

  }
}
