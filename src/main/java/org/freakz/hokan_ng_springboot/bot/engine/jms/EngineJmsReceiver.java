package org.freakz.hokan_ng_springboot.bot.engine.jms;

import lombok.extern.slf4j.Slf4j;
import org.freakz.hokan_ng_springboot.bot.common.jms.JmsEnvelope;
import org.freakz.hokan_ng_springboot.bot.common.jms.SpringJmsReceiver;
import org.freakz.hokan_ng_springboot.bot.common.jms.api.JmsSender;
import org.freakz.hokan_ng_springboot.bot.common.jms.api.JmsServiceMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by petria on 5.2.2015.
 */
@Component
@Slf4j
public class EngineJmsReceiver extends SpringJmsReceiver {

    @Autowired
    private JmsSender jmsSender;

    @Autowired
    private JmsServiceMessageHandler jmsServiceMessageHandler;


    @Override
    public String getDestinationName() {
        return "HokanNGEngineQueue";
    }

    @Override
    public void handleJmsEnvelope(JmsEnvelope envelope) throws Exception {
        jmsServiceMessageHandler.handleJmsEnvelope(envelope);
    }

/*  @Override
  public void handleJmsMessage(Message message) throws JMSException {
    ObjectMessage objectMessage = (ObjectMessage) message;
    JmsMessage jmsMessage = (JmsMessage) objectMessage.getObject();
    JmsMessage jmsReplyMessage;
    try {
      log.info("---->");
      jmsReplyMessage = jmsServiceMessageHandler.handleJmsServiceMessage(jmsMessage);
      log.info("<----");
    } catch (Exception e) {
      jmsReplyMessage = new JmsMessage();
      jmsReplyMessage.addPayLoadObject("REPLY", e.getMessage());
      e.printStackTrace();
      log.error("Something went wrong!");
    }
    Destination replyTo = message.getJMSReplyTo();
    log.debug("got message: {}, replyTo: {}", jmsMessage, replyTo);
    if (replyTo != null) {
      if (jmsReplyMessage != null) {
        log.info("Sending reply: {}", jmsReplyMessage);
        jmsSender.sendJmsMessage(replyTo, jmsReplyMessage);
      }
    }

  }
*/

}
