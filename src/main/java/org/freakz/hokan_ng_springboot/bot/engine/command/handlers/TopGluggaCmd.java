package org.freakz.hokan_ng_springboot.bot.engine.command.handlers;

import com.martiansoftware.jsap.JSAPResult;
import org.freakz.hokan_ng_springboot.bot.common.events.EngineResponse;
import org.freakz.hokan_ng_springboot.bot.common.events.InternalRequest;
import org.freakz.hokan_ng_springboot.bot.common.exception.HokanException;
import org.freakz.hokan_ng_springboot.bot.common.models.DataValuesModel;
import org.freakz.hokan_ng_springboot.bot.engine.command.HelpGroup;
import org.freakz.hokan_ng_springboot.bot.engine.command.annotation.HelpGroups;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Petri Airio on 26.8.2015.
 * -
 */
@Component
@HelpGroups(
        helpGroups = {HelpGroup.SYSTEM}
)
@Scope("prototype")

public class TopGluggaCmd extends Cmd {

    public TopGluggaCmd() {
        super();
        setHelp("Channel top gluggas.");

    }

    @Override
    public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
        String channel = request.getIrcEvent().getChannel().toLowerCase();
        String network = request.getIrcEvent().getNetwork().toLowerCase();
        String key = "GLUGGA_COUNT";

        List<DataValuesModel> dataValues = dataValuesService.getDataValues(channel, network, key);
        Comparator<? super DataValuesModel> comparator = (Comparator<DataValuesModel>) (o1, o2) -> {
            Integer i1 = Integer.parseInt(o1.getValue());
            Integer i2 = Integer.parseInt(o2.getValue());
            return i1.compareTo(i2);
        };
        Collections.sort(dataValues, comparator);
        int c = 1;
        StringBuilder sb = new StringBuilder("Top *glugga*: ");
        for (DataValuesModel value : dataValues) {
            sb.append(c).append(": ");
            sb.append(value.getNick());
            sb.append("=");
            sb.append(value.getValue());
            sb.append(" ");
        }
        response.addResponse(sb.toString());
    }
}
