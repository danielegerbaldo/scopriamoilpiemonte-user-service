package TAASS.ServiceDBUtenti.rabbitMQ;

import TAASS.ServiceDBUtenti.models.Utente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ListenerService {

    public static final Logger logger = LoggerFactory.getLogger(ListenerService.class);
    @Autowired
    private PublishService publishServ;

    @RabbitListener(queues = "requestUserEvent")
    public void getPojo(UserMessage message) {
        logger.info("From Queue : {}", message);

        publishServ.publishNotification("publishUserGateway",message.userId);

    }


}