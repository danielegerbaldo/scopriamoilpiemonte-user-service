package TAASS.ServiceDBUtenti.rabbitMQ;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class PublishService {

    @Autowired
    private final RabbitTemplate rabbitTemplate;

    public PublishService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    @Scheduled(fixedDelay = 5000)
    public void publishNotification() {
        rabbitTemplate.convertAndSend("publishUserGateway",
                new Notification("USER", LocalTime.now().format(DateTimeFormatter.ISO_TIME)));
    }
}