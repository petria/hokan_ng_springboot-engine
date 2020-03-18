package org.freakz.hokan_ng_springboot.bot.engine.command.handlers;

import com.martiansoftware.jsap.JSAPResult;
import org.freakz.hokan_ng_springboot.bot.common.events.EngineResponse;
import org.freakz.hokan_ng_springboot.bot.common.events.InternalRequest;
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

        String korona = getKoronas();
        if (korona != null) {
            response.addResponse("%s", korona);
        } else {
            response.addResponse("Kaik kuallu?!");

        }
    }
}
