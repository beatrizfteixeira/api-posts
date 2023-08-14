package bia.apiassyncactivemq.messagebroker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;


@Component
public class MessageReceiver {

    @JmsListener(destination = "processPost", containerFactory = "myFactory")
    public void receiveMessagePost(Integer id) {
//        System.out.println("Received Post <" + id + ">");
//        System.out.println("Now processing...");
        //processMessageService.processPost(id);
    }

}
