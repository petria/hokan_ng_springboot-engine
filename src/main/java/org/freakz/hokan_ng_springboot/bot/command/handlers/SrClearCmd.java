package org.freakz.hokan_ng_springboot.bot.command.handlers;

import com.martiansoftware.jsap.JSAPResult;
import lombok.extern.slf4j.Slf4j;
import org.freakz.hokan_ng_springboot.bot.common.events.EngineResponse;
import org.freakz.hokan_ng_springboot.bot.common.events.InternalRequest;
import org.freakz.hokan_ng_springboot.bot.common.exception.HokanException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by pairio on 31.5.2014.
 *
 * @author Petri Airio (petri.j.airio@gmail.com)
 */
@Component
@Scope("prototype")
@Slf4j
public class SrClearCmd extends Cmd {

    public SrClearCmd() {
        super();
        setHelp("Clears Search/Replaces");
    }

    @Override
    public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
        searchReplaceService.deleteAll();
        response.addResponse("Search/Replaces cleared!");
    }

}
