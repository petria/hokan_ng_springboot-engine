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
import org.freakz.hokan_ng_springboot.bot.jpa.service.UserService;
import org.freakz.hokan_ng_springboot.bot.util.StringStuff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.freakz.hokan_ng_springboot.bot.util.StaticStrings.ARG_NICK;

/**
 * User: petria
 * Date: 12/31/13
 * Time: 10:00 AM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Slf4j
@Scope("prototype")
@HelpGroups(
        helpGroups = {HelpGroup.USERS}
)
public class UsersCmd extends Cmd {

    @Autowired
    private UserService userService;

    public UsersCmd() {
        super();
        setHelp("Lists users the Bot has meet on channels.");

        UnflaggedOption flg = new UnflaggedOption(ARG_NICK)
                .setRequired(false)
                .setGreedy(false);
        registerParameter(flg);

    }

    @Override
    public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
        String nick = results.getString(ARG_NICK);
        if (nick == null) {
            nick = ".*";
        } else {
            nick = ".*" + nick + ".*";
        }
        List<User> users = userService.findAll();
        response.addResponse("Known users: ");
        for (User user : users) {
            if (StringStuff.match(user.getNick(), nick)) {
                response.addResponse("%s ", user.getNick());
            }
        }
    }

}
