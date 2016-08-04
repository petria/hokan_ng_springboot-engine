package org.freakz.hokan_ng_springboot.bot.command.handlers;

import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import lombok.extern.slf4j.Slf4j;
import org.freakz.hokan_ng_springboot.bot.command.HelpGroup;
import org.freakz.hokan_ng_springboot.bot.command.annotation.HelpGroups;
import org.freakz.hokan_ng_springboot.bot.events.EngineResponse;
import org.freakz.hokan_ng_springboot.bot.events.InternalRequest;
import org.freakz.hokan_ng_springboot.bot.exception.HokanException;
import org.freakz.hokan_ng_springboot.bot.jpa.entity.User;
import org.freakz.hokan_ng_springboot.bot.jpa.entity.UserFlag;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Set;

import static org.freakz.hokan_ng_springboot.bot.util.StaticStrings.ARG_FLAGS;
import static org.freakz.hokan_ng_springboot.bot.util.StaticStrings.ARG_NICK;

/**
 * Created by Petri Airio on 24.2.2016.
 * -
 */
@Component
@Slf4j
@Scope("prototype")
@HelpGroups(
    helpGroups = {HelpGroup.ACCESS_CONTROL, HelpGroup.USERS}
)
public class UserFlagDelCmd extends Cmd {

  public UserFlagDelCmd() {

    setHelp("Removes User Flags.");
    setHelpWikiUrl("https://github.com/petria/hokan_ng_springboot/wiki/UserFlags");
    setAdminUserOnly(true);

    UnflaggedOption unflaggedOption = new UnflaggedOption(ARG_NICK)
        .setRequired(true)
        .setGreedy(false);
    registerParameter(unflaggedOption);

    unflaggedOption = new UnflaggedOption(ARG_FLAGS)
        .setRequired(false)
        .setGreedy(false);
    registerParameter(unflaggedOption);

  }

  @Override
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
    String target = results.getString(ARG_NICK, null);

    User user;
    if (target.equals("me")) {
      user = request.getUser();
    } else {
      user = userService.findFirstByNick(target);
    }
    if (user == null) {
      response.addResponse("No User found with: " + target);
      return;
    }

    String flagsStr = results.getString(ARG_FLAGS, null);
    if (flagsStr == null) {
      response.addResponse("%s UserFlags: %s", user.getNick(), UserFlag.getStringFromFlagSet(user));
      return;
    }
    Set<UserFlag> flags = UserFlag.getFlagSetFromString(flagsStr);
    if (flags.size() == 0) {
      response.addResponse("No flags: " + flagsStr);
      return;
    }
    user = accessControlService.removeUserFlags(user, flags);
    response.addResponse("%s flags now: %s", user.getNick(), UserFlag.getStringFromFlagSet(user.getUserFlagsSet()));

  }

}
