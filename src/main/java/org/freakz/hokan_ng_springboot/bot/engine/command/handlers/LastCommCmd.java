package org.freakz.hokan_ng_springboot.bot.engine.command.handlers;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPResult;
import org.freakz.hokan_ng_springboot.bot.common.enums.HokanModule;
import org.freakz.hokan_ng_springboot.bot.common.events.EngineResponse;
import org.freakz.hokan_ng_springboot.bot.common.events.InternalRequest;
import org.freakz.hokan_ng_springboot.bot.common.exception.HokanException;
import org.freakz.hokan_ng_springboot.bot.common.jpa.entity.CommandHistory;
import org.freakz.hokan_ng_springboot.bot.engine.command.HelpGroup;
import org.freakz.hokan_ng_springboot.bot.engine.command.annotation.HelpGroups;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.freakz.hokan_ng_springboot.bot.common.util.StaticStrings.ARG_COUNT;

/**
 * Created by Petri Airio on 19.5.2015.
 */
@Component
@Scope("prototype")

@HelpGroups(
        helpGroups = {HelpGroup.PROCESS}
)
public class LastCommCmd extends Cmd {

    public LastCommCmd() {
        super();
        setHelp("Shows executed processes in Bot.");

        FlaggedOption flg = new FlaggedOption(ARG_COUNT)
                .setStringParser(JSAP.INTEGER_PARSER)
                .setDefault("5")
                .setShortFlag('c');
        registerParameter(flg);
    }

    @Override
    public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
        List<CommandHistory> all = new ArrayList<>();
        for (HokanModule module : HokanModule.values()) {
            long sessionId = propertyService.getPropertyAsLong(module.getModuleProperty(), -1);
            if (sessionId != -1) {
                List<CommandHistory> running = commandHistoryService.findByHokanModuleAndSessionId(module.toString(), sessionId);
                all.addAll(running);
            }
        }
        if (all.size() > 0) {
            Comparator<CommandHistory> comparator = (o1, o2) -> o2.getStartTime().compareTo(o1.getStartTime());
            Collections.sort(all, comparator);
            int max = results.getInt(ARG_COUNT);
            int count = 0;
            response.addResponse("%2s - %10s - %-13s - %s\n", "PID", "STARTED_BY", "START_TIME", "CLASS");
            for (CommandHistory cmd : all) {
                response.addResponse("%2d - %10s - %-13s - %s\n", cmd.getPid(), cmd.getStartedBy(), cmd.getStartTime(), cmd.getRunnable().replaceAll("class org.freakz.hokan_ng_springboot.bot.", ""));
                count++;
                if (count == max) {
                    break;
                }
            }
        }

    }
}
