package org.freakz.hokan_ng_springboot.bot.engine.command.handlers;

import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import org.freakz.hokan_ng_springboot.bot.common.events.EngineResponse;
import org.freakz.hokan_ng_springboot.bot.common.events.InternalRequest;
import org.freakz.hokan_ng_springboot.bot.common.exception.HokanException;
import org.freakz.hokan_ng_springboot.bot.common.jpa.entity.TvNotify;
import org.freakz.hokan_ng_springboot.bot.engine.command.HelpGroup;
import org.freakz.hokan_ng_springboot.bot.engine.command.annotation.HelpGroups;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.freakz.hokan_ng_springboot.bot.common.util.StaticStrings.ARG_PROGRAM;

/**
 * User: petria
 * Date: 12/11/13
 * Time: 6:39 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Scope("prototype")
@HelpGroups(
        helpGroups = {HelpGroup.TV}
)
public class TvNotifyAddCmd extends Cmd {

    public TvNotifyAddCmd() {

        setHelp("Adds notify for Telkku programs.");

        UnflaggedOption opt = new UnflaggedOption(ARG_PROGRAM)
                .setRequired(true)
                .setGreedy(false);
        registerParameter(opt);

    }

    @Override
    public void handleRequest(InternalRequest iRequest, EngineResponse response, JSAPResult results) throws HokanException {

        TvNotify notify = tvNotifyService.getTvNotify(iRequest.getChannel(), results.getString(ARG_PROGRAM));
        if (notify != null) {
            response.addResponse("TvNotify: %d: %s already in notify list!", notify.getId(), notify.getNotifyPattern());
            return;
        }
        notify = tvNotifyService.addTvNotify(iRequest.getChannel(), results.getString(ARG_PROGRAM), iRequest.getIrcEvent().getSender());
        response.addResponse("Added TvNotify: %d: %s", notify.getId(), notify.getNotifyPattern());
    }

}
