package org.freakz.hokan_ng_springboot.bot.engine;

import org.freakz.hokan_ng_springboot.bot.common.events.EngineResponse;
import org.freakz.hokan_ng_springboot.bot.common.events.InternalRequest;
import org.freakz.hokan_ng_springboot.bot.common.events.IrcMessageEvent;
import org.freakz.hokan_ng_springboot.bot.common.exception.HokanException;
import org.freakz.hokan_ng_springboot.bot.engine.command.CommandHandlerService;
import org.freakz.hokan_ng_springboot.bot.engine.command.handlers.Cmd;
import org.freakz.hokan_ng_springboot.bot.engine.service.CmdHandlerMatches;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Petri Airio on 24.4.2015.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HokanNgSpringBootEngine.class)
public class EngineTests {

    @Autowired
    private CommandHandlerService commandHandlerService;

    @Test
    public void contextLoads() throws HokanException {
        final CmdHandlerMatches matchingCommands = commandHandlerService.getMatchingCommands("!srdel 1");
        final Cmd cmd = matchingCommands.getMatches().get(0);
        InternalRequest request = new InternalRequest();
        IrcMessageEvent event = new IrcMessageEvent();
        event.setNetwork("DEVNet");
        event.setMessage("!srdel 1");
        request.init(event);
        EngineResponse response = new EngineResponse();
        cmd.handleLine(request, response);
        int foo = 0;
    }

}
