package org.freakz.hokan_ng_springboot.bot.command.handlers;

import com.martiansoftware.jsap.JSAPResult;
import lombok.extern.slf4j.Slf4j;
import org.freakz.hokan_ng_springboot.bot.command.HelpGroup;
import org.freakz.hokan_ng_springboot.bot.command.annotation.HelpGroups;
import org.freakz.hokan_ng_springboot.bot.common.events.EngineResponse;
import org.freakz.hokan_ng_springboot.bot.common.events.InternalRequest;
import org.freakz.hokan_ng_springboot.bot.common.exception.HokanException;
import org.freakz.hokan_ng_springboot.bot.common.jpa.entity.User;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Petri Airio (petri.j.airio@gmail.com) on 11.9.2015.
 */
@Component
@Slf4j
@Scope("prototype")
@HelpGroups(
        helpGroups = {HelpGroup.ACCESS_CONTROL, HelpGroup.USERS}
)
public class WhoCmd extends Cmd {

    public WhoCmd() {
        super();
        setHelp("Shows who has logged in to the bot.");
    }

    @Override
    public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
        List<User> users = userService.findAll();
        StringBuilder sb = new StringBuilder();
        for (User user : users) {
            if (user.isLoggedIn() > 0) {
                sb.append(String.format("%s ", user.getNick()));
            }
        }
        if (sb.length() > 0) {
            response.addResponse("Logged in users: %s", sb.toString());
        } else {
            response.addResponse("No one logged in!");
        }
    }

}
