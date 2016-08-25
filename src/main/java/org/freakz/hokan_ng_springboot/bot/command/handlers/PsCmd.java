package org.freakz.hokan_ng_springboot.bot.command.handlers;

import com.martiansoftware.jsap.JSAPResult;
import lombok.extern.slf4j.Slf4j;
import org.freakz.hokan_ng_springboot.bot.command.HelpGroup;
import org.freakz.hokan_ng_springboot.bot.command.annotation.HelpGroups;
import org.freakz.hokan_ng_springboot.bot.enums.HokanModule;
import org.freakz.hokan_ng_springboot.bot.events.EngineResponse;
import org.freakz.hokan_ng_springboot.bot.events.InternalRequest;
import org.freakz.hokan_ng_springboot.bot.exception.HokanException;
import org.freakz.hokan_ng_springboot.bot.jpa.entity.CommandHistory;
import org.freakz.hokan_ng_springboot.bot.jpa.entity.CommandStatus;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Petri Airio on 18.5.2015.
 */
@Component
@Scope("prototype")
@Slf4j
@HelpGroups(
        helpGroups = {HelpGroup.PROCESS, HelpGroup.SYSTEM}
)
public class PsCmd extends Cmd {

    public PsCmd() {
        setHelp("Shows active processes running in Bot.");
    }

    @Override
    public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
        List<CommandHistory> allRunning = new ArrayList<>();
        for (HokanModule module : HokanModule.values()) {
            long sessionId = propertyService.getPropertyAsLong(module.getModuleProperty(), -1);
            if (sessionId != -1) {
                List<CommandHistory> running = commandHistoryService.findByHokanModuleAndSessionIdAndCommandStatus(module.toString(), sessionId, CommandStatus.RUNNING);
                allRunning.addAll(running);
            }
        }
        if (allRunning.size() > 0) {
            Comparator<CommandHistory> comparator = new Comparator<CommandHistory>() {
                @Override
                public int compare(CommandHistory o1, CommandHistory o2) {
                    Long pid1 = o1.getPid();
                    Long pid2 = o2.getPid();
                    return pid1.compareTo(pid2);
                }
            };
            Collections.sort(allRunning, comparator);

            response.addResponse("%5s - %10s - %-13s - %s\n", "PID", "STARTED_BY", "MODULE", "CLASS");
            for (CommandHistory cmd : allRunning) {
                response.addResponse("%5d - %10s - %-13s - %s\n", cmd.getPid(), cmd.getStartedBy(), cmd.getHokanModule(), cmd.getRunnable().replaceAll("class org.freakz.hokan_ng_springboot.bot.", ""));
            }
        }

    }

}
