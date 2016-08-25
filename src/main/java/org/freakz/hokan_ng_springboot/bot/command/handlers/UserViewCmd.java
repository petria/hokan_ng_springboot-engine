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
import org.freakz.hokan_ng_springboot.bot.jpa.entity.UserChannel;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.freakz.hokan_ng_springboot.bot.util.StaticStrings.ARG_NICK;

/**
 * User: petria
 * Date: 12/16/13
 * Time: 9:24 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
@Component
@Slf4j
@Scope("prototype")
@HelpGroups(
        helpGroups = {HelpGroup.USERS}
)
public class UserViewCmd extends Cmd {

    public UserViewCmd() {
        super();
        setHelp("UserViewCmd help");

        UnflaggedOption flg = new UnflaggedOption(ARG_NICK)
                .setRequired(false)
                .setGreedy(false);
        registerParameter(flg);
    }

    @Override
    public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
        String nick = results.getString(ARG_NICK);
        User hUser;
        if (nick == null) {
            hUser = request.getUser();
        } else {
            hUser = userService.findFirstByNick(nick);
            if (hUser == null) {
                response.addResponse("User not found: %s", nick);
                return;
            }
        }

        String ret = "-= " + hUser.getNick() + " (" + hUser.getFullName();
        if (hUser.getEmail() != null && hUser.getEmail().length() > 0) {
            ret += ", " + hUser.getEmail();
        }
        ret += ") ";
        if (accessControlService.isAdminUser(hUser)) {
            ret += "[AdminUser] ";
        }
        if (accessControlService.isChannelOp(hUser, request.getChannel())) {
            ret += "[ChannelOp] ";
        }
        ret += "=-\n";

        ret += "SetMask  : " + hUser.getMask() + " (CurrentMask: " + hUser.getRealMask() + ")\n";
        ret += "Flags    : " + hUser.getFlagsString() + "\n";
        ret += "Channels :\n";

        List<UserChannel> userChannels = userChannelService.findByUser(hUser);
        for (UserChannel channel : userChannels) {
            ret += "  " + channel.getChannel().getChannelName() + "\n";
        }
        response.addResponse(ret);
    }

}
