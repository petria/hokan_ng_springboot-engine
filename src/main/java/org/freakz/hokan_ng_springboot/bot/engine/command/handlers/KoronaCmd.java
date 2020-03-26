package org.freakz.hokan_ng_springboot.bot.engine.command.handlers;

import com.martiansoftware.jsap.JSAPResult;
import org.freakz.hokan_ng_springboot.bot.common.events.EngineResponse;
import org.freakz.hokan_ng_springboot.bot.common.events.InternalRequest;
import org.freakz.hokan_ng_springboot.bot.common.events.ServiceRequestType;
import org.freakz.hokan_ng_springboot.bot.common.events.ServiceResponse;
import org.freakz.hokan_ng_springboot.bot.common.exception.HokanException;
import org.freakz.hokan_ng_springboot.bot.engine.command.HelpGroup;
import org.freakz.hokan_ng_springboot.bot.engine.command.annotation.HelpGroups;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Scope("prototype")
@HelpGroups(
        helpGroups = {HelpGroup.SMS}
)
public class KoronaCmd extends Cmd {

    public KoronaCmd() {
        setHelp("Korona status!");
    }

    public String getKoronas() {
        String title = "";
        try {
            String url = "https://korona.kans.io/";
            Document doc = Jsoup.connect(url).get();
            Elements body = doc.getElementsByTag("title");
            return body.text();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {

        String nickId = request.getUser().getNick() + request.getUser().getId();
        ServiceResponse serviceResponse = doServicesRequest(ServiceRequestType.KORONA_REQUEST, request.getIrcEvent(), nickId);

        Integer[] n = serviceResponse.getKoronas();
        if (n != null) {
            String res =
                    String.format("Suomen koronavirus-tartuntatilanne - Tartunnat : %d (+%d) - Parantuneet: %d (+%d) - Menehtyneet: %d (+%d)",
                            n[0], n[3],
                            n[1], n[4],
                            n[2], n[5]
                    );
            response.addResponse("%s", res);
        } else {
            response.addResponse("Kaik kuallu?!");

        }
    }
}
