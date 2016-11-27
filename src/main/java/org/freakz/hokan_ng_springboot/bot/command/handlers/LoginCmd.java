package org.freakz.hokan_ng_springboot.bot.command.handlers;

import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import lombok.extern.slf4j.Slf4j;
import org.freakz.hokan_ng_springboot.bot.command.HelpGroup;
import org.freakz.hokan_ng_springboot.bot.command.annotation.HelpGroups;
import org.freakz.hokan_ng_springboot.bot.common.events.EngineResponse;
import org.freakz.hokan_ng_springboot.bot.common.events.InternalRequest;
import org.freakz.hokan_ng_springboot.bot.common.exception.HokanException;
import org.freakz.hokan_ng_springboot.bot.common.jpa.entity.User;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.freakz.hokan_ng_springboot.bot.common.util.StaticStrings.ARG_PASSWORD;

/**
 * Created by Petri Airio on 10.9.2015.
 */
@Component
@Slf4j
@Scope("prototype")
@HelpGroups(
        helpGroups = {HelpGroup.ACCESS_CONTROL}
)
public class LoginCmd extends Cmd {

    public LoginCmd() {
        super();
        setHelp("Login user to the bot.");

        setPrivateOnly(true);

        UnflaggedOption flg = new UnflaggedOption(ARG_PASSWORD)
                .setRequired(true)
                .setGreedy(false);
        registerParameter(flg);

    }

    @Override
    public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
        User user = request.getUser();
        String plainPassword = results.getString(ARG_PASSWORD);
        boolean authOk = accessControlService.authenticate(user, plainPassword);
        if (authOk) {
            response.addResponse("Login ok!");
        } else {
            response.addResponse("Login failed!");
        }
    }

}
