package org.freakz.hokan_ng_springboot.bot.command.handlers;

import com.martiansoftware.jsap.JSAPResult;
import org.freakz.hokan_ng_springboot.bot.command.HelpGroup;
import org.freakz.hokan_ng_springboot.bot.command.annotation.HelpGroups;
import org.freakz.hokan_ng_springboot.bot.events.EngineResponse;
import org.freakz.hokan_ng_springboot.bot.events.InternalRequest;
import org.freakz.hokan_ng_springboot.bot.exception.HokanException;
import org.freakz.hokan_ng_springboot.bot.jpa.entity.TvNotify;
import org.freakz.hokan_ng_springboot.bot.jpa.service.TvNotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User: petria
 * Date: 12/11/13
 * Time: 7:04 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Scope("prototype")
@HelpGroups(
        helpGroups = {HelpGroup.TV}
)
public class TvNotifyListCmd extends Cmd {

    @Autowired
    private TvNotifyService tvNotifyService;

    public TvNotifyListCmd() {
        super();
        setHelp("Show channel TV notify list.");
    }

    @Override
    public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
        List<TvNotify> notifies = tvNotifyService.getTvNotifies(request.getChannel());
        response.addResponse("Keywords currently on TV notify in channel %s:", request.getChannel().getChannelName());
        for (TvNotify notify : notifies) {
            response.addResponse(" %d: %s", notify.getId(), notify.getNotifyPattern());
        }
    }

}
