package org.freakz.hokan_ng_springboot.bot.command.handlers;

import com.martiansoftware.jsap.JSAPResult;
import org.freakz.hokan_ng_springboot.bot.command.HelpGroup;
import org.freakz.hokan_ng_springboot.bot.command.annotation.HelpGroups;
import org.freakz.hokan_ng_springboot.bot.events.EngineResponse;
import org.freakz.hokan_ng_springboot.bot.events.InternalRequest;
import org.freakz.hokan_ng_springboot.bot.exception.HokanException;
import org.freakz.hokan_ng_springboot.bot.jpa.entity.PropertyEntity;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Petri Airio (petri.j.airio@gmail.com) on 26.8.2015.
 *
 */
@Component
@Scope("prototype")
@HelpGroups(
    helpGroups = {HelpGroup.PROPERTIES, HelpGroup.SYSTEM}
)
public class SysEnvCmd extends Cmd {

  public SysEnvCmd() {
    super();
    setHelp("Shows system properties.");
    setAdminUserOnly(true);
  }

  @Override
  public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {

    List<PropertyEntity> propertyList = propertyService.findAll();
    for (PropertyEntity p : propertyList) {
      response.addResponse("%25s = %s\n", p.getPropertyName(), p.getValue());
    }
  }

}
