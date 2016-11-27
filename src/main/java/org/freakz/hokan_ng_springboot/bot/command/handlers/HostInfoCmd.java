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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * User: petria
 * Date: 12/13/13
 * Time: 9:33 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Scope("prototype")
@Slf4j
@HelpGroups(
        helpGroups = {HelpGroup.SYSTEM}
)
public class HostInfoCmd extends Cmd {

    public HostInfoCmd() {
        super();
        setHelp("Shows information about the host machine where the Bot is running on.");
    }

    @Override
    public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
        SystemScriptResult hostInfo = scriptRunnerService.runAndGetResult(SystemScript.HOST_INFO_SCRIPT);
        response.addResponse("I am running on: %s", hostInfo.getFormattedOutput());
    }

}
