package org.freakz.hokan_ng_springboot.bot.command.handlers;

import com.martiansoftware.jsap.JSAPResult;
import lombok.extern.slf4j.Slf4j;
import org.freakz.hokan_ng_springboot.bot.events.EngineResponse;
import org.freakz.hokan_ng_springboot.bot.events.InternalRequest;
import org.freakz.hokan_ng_springboot.bot.exception.HokanException;
import org.freakz.hokan_ng_springboot.bot.jpa.entity.SearchReplace;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by pairio on 31.5.2014.
 *
 * @author Petri Airio (petri.j.airio@gmail.com)
 */
@Component
@Scope("prototype")
@Slf4j
public class SrListCmd extends Cmd {

    public SrListCmd() {
        super();
        setHelp("Lists Search / Replaces.");
    }

    @Override
    public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
        List<SearchReplace> list = searchReplaceService.findAll();
        StringBuilder sb = new StringBuilder("Search/Replace list: ");
        if (list.size() > 0) {
            boolean first = true;
            for (SearchReplace sr : list) {
                if (first) {
                    first = false;
                } else {
                    sb.append(", ");
                }
                sb.append(String.format("%d) s/%s/%s/", sr.getId(), sr.getSearch(), sr.getReplace()));
            }
        } else {
            sb.append("<empty>");
        }
        response.addResponse(sb);
        response.setNoSearchReplace(true);
    }
}
