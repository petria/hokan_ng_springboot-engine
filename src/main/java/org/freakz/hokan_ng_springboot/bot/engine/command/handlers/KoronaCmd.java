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
import java.util.HashMap;
import java.util.Map;

@Component
@Scope("prototype")
@HelpGroups(
        helpGroups = {HelpGroup.SMS}
)
public class KoronaCmd extends Cmd {

    private static Map<String, Integer> valueMap = new HashMap<>();

    static {
        valueMap.put("infected", 0);
        valueMap.put("healed", 0);
        valueMap.put("dead", 0);
    }

    private Integer[] extractNumbers(String title) {
        String[] split = title.split(" ");
        Integer infected = Integer.parseInt(split[6]);
        Integer healed = Integer.parseInt(split[9]);
        Integer dead = Integer.parseInt(split[12]);
        Integer[] diffs = {0, 0, 0, 0, 0, 0};

        if (infected > valueMap.get("infected")) {
            diffs[0] = infected - valueMap.get("infected");
        }
        if (healed > valueMap.get("healed")) {
            diffs[1] = healed - valueMap.get("healed");
        }
        if (dead > valueMap.get("dead")) {
            diffs[2] = dead - valueMap.get("dead");
        }
        valueMap.put("infected", infected);
        valueMap.put("healed", healed);
        valueMap.put("dead", dead);
        diffs[3] = infected;
        diffs[4] = healed;
        diffs[5] = dead;

        return diffs;
    }

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
            Integer[] n = extractNumbers(korona);
            String res =
                    String.format("Suomen koronavirus-tartuntatilanne - Tartunnat : %d (+%d) - Parantuneet: %d (+%d) - Menehtyneet: %d (+%d)",
                            n[3], n[0],
                            n[4], n[1],
                            n[5], n[2]
                    );
            response.addResponse("%s", res);
        } else {
            response.addResponse("Kaik kuallu?!");

        }
    }
}
