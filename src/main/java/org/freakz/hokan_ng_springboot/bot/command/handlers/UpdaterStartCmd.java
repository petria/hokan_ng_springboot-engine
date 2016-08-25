package org.freakz.hokan_ng_springboot.bot.command.handlers;

import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import lombok.extern.slf4j.Slf4j;
import org.freakz.hokan_ng_springboot.bot.command.HelpGroup;
import org.freakz.hokan_ng_springboot.bot.command.annotation.HelpGroups;
import org.freakz.hokan_ng_springboot.bot.events.EngineResponse;
import org.freakz.hokan_ng_springboot.bot.events.InternalRequest;
import org.freakz.hokan_ng_springboot.bot.events.ServiceRequestType;
import org.freakz.hokan_ng_springboot.bot.events.ServiceResponse;
import org.freakz.hokan_ng_springboot.bot.exception.HokanException;
import org.freakz.hokan_ng_springboot.bot.models.DataUpdaterModel;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.freakz.hokan_ng_springboot.bot.util.StaticStrings.ARG_UPDATER;

/**
 * Created by Petri Airio on 17.6.2015.
 */
@Component
@Scope("prototype")
@Slf4j
@HelpGroups(
        helpGroups = {HelpGroup.UPDATERS}
)
public class UpdaterStartCmd extends Cmd {

    public UpdaterStartCmd() {
        super();
        setHelp("Starts specific updater.");

        UnflaggedOption opt = new UnflaggedOption(ARG_UPDATER)
                .setRequired(true)
                .setGreedy(false);
        registerParameter(opt);

        setAdminUserOnly(true);
    }

    @Override
    public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
        String updater = results.getString(ARG_UPDATER);
        ServiceResponse serviceResponse = doServicesRequest(ServiceRequestType.UPDATERS_START_REQUEST, request.getIrcEvent(), updater);
        List<DataUpdaterModel> modelList = serviceResponse.getStartUpdaterListData();
        if (modelList.size() > 0) {
            response.addResponse("Started following updaters:");
            for (DataUpdaterModel model : modelList) {
                String txt = String.format("%15s\n", model.getName());
                response.addResponse(txt);
            }
        } else {
            response.addResponse("No updaters started!");
        }

    }
}
