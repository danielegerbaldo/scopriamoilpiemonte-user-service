package TAASS.ServiceDBUtenti.rabbitMQ;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ListenerService {

    public static final Logger logger = LoggerFactory.getLogger(ListenerService.class);

    @RabbitListener(queues = "requestUserGateway")
    public void getPojo(Notification message) {
        logger.info("From Queue : {}", message);
    }
}