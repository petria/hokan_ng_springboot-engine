package org.freakz.hokan_ng_springboot.bot.command.handlers;

import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
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

import static org.freakz.hokan_ng_springboot.bot.util.StaticStrings.ARG_PROGRAM;

/**
 * User: petria
 * Date: 12/12/13
 * Time: 6:06 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Scope("prototype")
@HelpGroups(
    helpGroups = {HelpGroup.TV}
)
public class TvNotifyDelCmd extends Cmd {

  @Autowired
  private TvNotifyService tvNotifyService;

  public TvNotifyDelCmd() {
    super();
    setHelp("Removes TvNotify either by Id or by keyword");

    UnflaggedOption opt = new UnflaggedOption(ARG_PROGRAM)
        .setRequired(true)
        .setGreedy(false);
    registerParameter(opt);

  }

  @Override
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {

    String program = results.getString(ARG_PROGRAM);
    if (program.equals("all")) {
      int removed = tvNotifyService.delTvNotifies(request.getChannel());
      response.addResponse("Removed %d TvNotifies.", removed);
    } else {
      TvNotify notify;
      try {
        long id = Long.parseLong(program);
        notify = tvNotifyService.getTvNotifyById(id);
      } catch (NumberFormatException e) {
        notify = tvNotifyService.getTvNotify(request.getChannel(), program);
      }
      if (notify != null) {
        tvNotifyService.delTvNotify(notify);
        response.addResponse("Removed TvNotify: %d: %s", notify.getId(), notify.getNotifyPattern());

      } else {
        response.addResponse("No TvNotify found with: %s", program);
      }
    }
  }

}
