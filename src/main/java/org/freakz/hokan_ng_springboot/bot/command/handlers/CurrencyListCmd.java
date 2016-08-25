package org.freakz.hokan_ng_springboot.bot.command.handlers;

import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;
import lombok.extern.slf4j.Slf4j;
import org.freakz.hokan_ng_springboot.bot.events.EngineResponse;
import org.freakz.hokan_ng_springboot.bot.events.InternalRequest;
import org.freakz.hokan_ng_springboot.bot.events.ServiceRequestType;
import org.freakz.hokan_ng_springboot.bot.events.ServiceResponse;
import org.freakz.hokan_ng_springboot.bot.exception.HokanException;
import org.freakz.hokan_ng_springboot.bot.models.GoogleCurrency;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.freakz.hokan_ng_springboot.bot.util.StaticStrings.ARG_CURRENCY;

/**
 * Created by Petri Airio on 2.9.2015.
 */
@Component
@Scope("prototype")
@Slf4j
public class CurrencyListCmd extends Cmd {

    public CurrencyListCmd() {
        super();
        setHelp("Shows / searches from know currencies to use with !currencyconvert.");
        UnflaggedOption flg = new UnflaggedOption(ARG_CURRENCY)
                .setRequired(false)
                .setGreedy(false);
        registerParameter(flg);

    }

    @Override
    public void handleRequest(InternalRequest request, EngineResponse response, JSAPResult results) throws HokanException {
        String currency = results.getString(ARG_CURRENCY);

        ServiceResponse serviceResponse = doServicesRequest(ServiceRequestType.CURRENCY_LIST_REQUEST,
                request.getIrcEvent(), currency);
        List<GoogleCurrency> currencyList = serviceResponse.getCurrencyListResponse();
        if (currency == null) {
            String resp = "Known currencies:";
            for (GoogleCurrency googleCurrency : currencyList) {
                resp += " " + googleCurrency.getShortName();
            }
            response.addResponse(resp);
        } else {
            response.addResponse("TODO!");
        }

    }


}
