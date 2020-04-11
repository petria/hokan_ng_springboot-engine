package org.freakz.hokan_ng_springboot.bot.engine.command.handlers;

import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import org.freakz.hokan_ng_springboot.bot.common.events.EngineResponse;
import org.freakz.hokan_ng_springboot.bot.common.events.InternalRequest;
import org.freakz.hokan_ng_springboot.bot.common.events.ServiceRequestType;
import org.freakz.hokan_ng_springboot.bot.common.exception.HokanException;
import org.freakz.hokan_ng_springboot.bot.common.models.MatkaResponse;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.freakz.hokan_ng_springboot.bot.common.util.StaticStrings.ARG_CITY_1;
import static org.freakz.hokan_ng_springboot.bot.common.util.StaticStrings.ARG_CITY_2;

/**
 * User: petria
 * Date: Apr 19, 2010
 * Time: 10:00:55 PM
 */
@Component
@Scope("prototype")
public class MatkaCmd extends Cmd {

    public MatkaCmd() {
        super();
        setHelp("Distance between city1 <-> city2");

        UnflaggedOption flg = new UnflaggedOption(ARG_CITY_1)
                .setRequired(true)
                .setGreedy(false);
        registerParameter(flg);

        flg = new UnflaggedOption(ARG_CITY_2)
                .setRequired(true)
                .setGreedy(false);
        registerParameter(flg);

    }

    @Override
    public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {

        String city1 = results.getString(ARG_CITY_1);
        String city2 = results.getString(ARG_CITY_2);

        MatkaResponse matkaResponse = new DoServiceRequest<MatkaResponse>().doServicesRequest(ServiceRequestType.MATKA_REQUEST, request.getIrcEvent(), city1, city2);
        if (matkaResponse != null) {

        }


    }

}
