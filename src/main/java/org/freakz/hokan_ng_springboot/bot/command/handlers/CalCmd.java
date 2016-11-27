package org.freakz.hokan_ng_springboot.bot.command.handlers;

import com.martiansoftware.jsap.JSAPResult;
import lombok.extern.slf4j.Slf4j;
import org.freakz.hokan_ng_springboot.bot.command.HelpGroup;
import org.freakz.hokan_ng_springboot.bot.command.annotation.HelpGroups;
import org.freakz.hokan_ng_springboot.bot.common.events.EngineResponse;
import org.freakz.hokan_ng_springboot.bot.common.events.InternalRequest;
import org.freakz.hokan_ng_springboot.bot.common.exception.HokanException;
import org.freakz.hokan_ng_springboot.bot.common.models.SystemScriptResult;
import org.freakz.hokan_ng_springboot.bot.common.service.SystemScript;
import org.jibble.pircbot.Colors;
import org.joda.time.DateTime;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by Petri Airio on 10.5.2016.
 * -
 */
@Component
@Scope("prototype")
@Slf4j
@HelpGroups(
        helpGroups = {HelpGroup.UTILITY}
)
public class CalCmd extends Cmd {

    public CalCmd() {
        setHelp("Shows the Calendar of current month.");
    }

    @Override
    public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
        SystemScriptResult calendar = scriptRunnerService.runAndGetResult(SystemScript.CAL_SCRIPT);
        String day = String.format(" %d", DateTime.now().getDayOfMonth());
        StringBuilder sb = new StringBuilder();
        for (String line : calendar.getOriginalOutput()) {
            if (line.trim().length() > 0) {
                line = line.replaceFirst(day, Colors.BOLD + day + Colors.NORMAL);
                sb.append(line).append("\n");
            }
        }
        response.addResponse("%s", sb.toString());
    }

}
