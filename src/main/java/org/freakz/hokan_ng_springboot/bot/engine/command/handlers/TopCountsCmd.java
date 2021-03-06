package org.freakz.hokan_ng_springboot.bot.engine.command.handlers;

import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import org.freakz.hokan_ng_springboot.bot.common.events.EngineResponse;
import org.freakz.hokan_ng_springboot.bot.common.events.InternalRequest;
import org.freakz.hokan_ng_springboot.bot.common.exception.HokanException;
import org.freakz.hokan_ng_springboot.bot.common.models.DataValuesModel;
import org.freakz.hokan_ng_springboot.bot.engine.command.HelpGroup;
import org.freakz.hokan_ng_springboot.bot.engine.command.annotation.HelpGroups;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static org.freakz.hokan_ng_springboot.bot.common.util.StaticStrings.ARG_CHANNEL;
import static org.freakz.hokan_ng_springboot.bot.common.util.StaticStrings.ARG_TOP_KEY;

/**
 * Created by Petri Airio on 26.8.2015.
 * -
 */
@Component
@HelpGroups(
        helpGroups = {HelpGroup.DATA_COLLECTION, HelpGroup.DATA_COLLECTION}
)
@Scope("prototype")

public class TopCountsCmd extends Cmd {

    public TopCountsCmd() {
        super();
        setHelp("Channel top key word counts.");

        UnflaggedOption uflg = new UnflaggedOption(ARG_TOP_KEY)
                .setRequired(false)
                .setGreedy(false);
        registerParameter(uflg);

        uflg = new UnflaggedOption(ARG_CHANNEL)
                .setRequired(false)
                .setGreedy(false);
        registerParameter(uflg);

    }

    @Override
    public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
        String key = results.getString(ARG_TOP_KEY);
        if (key == null) {
            String ret = String.format("%s\n  TopKey must be one of: %s", getUsage(), formatKeys(dataValuesService.getMatchingKeys(".*_COUNT")));
            response.setResponseMessage(ret);
            return;
        }

        String channel;
        if (request.getIrcEvent().isPrivate()) {
            channel = "#amigafin";
        } else {
            channel = results.getString(ARG_CHANNEL, request.getIrcEvent().getChannel()).toLowerCase();
        }
        String network = request.getIrcEvent().getNetwork().toLowerCase();

        List<DataValuesModel> dataValues = dataValuesService.getDataValues(channel, network, key);
        if (dataValues.size() > 0) {
            Comparator<? super DataValuesModel> comparator = (Comparator<DataValuesModel>) (o1, o2) -> {
                Integer i1 = Integer.parseInt(o1.getValue());
                Integer i2 = Integer.parseInt(o2.getValue());
                return i2.compareTo(i1);
            };
            dataValues.sort(comparator);
            int c = 1;
            String starredKey = String.format(" *%s*: ", dataValues.get(0).getKeyName().split("_")[0].toLowerCase());
            StringBuilder sb = new StringBuilder("Top " + channel + starredKey);
            for (DataValuesModel value : dataValues) {
                sb.append(c).append(") ");
                sb.append(value.getNick());
                sb.append("=");
                sb.append(value.getValue());
                sb.append(" ");
                c++;
                if (c == 11) {
                    break;
                }
            }
            response.addResponse(sb.toString());
        }
    }

    private String formatKeys(Set<String> set) {
        return String.join(", ", set);
    }

}
