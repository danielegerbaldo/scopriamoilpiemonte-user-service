package TAASS.ServiceDBUtenti.rabbitMQ;

import TAASS.ServiceDBUtenti.models.Utente;
import TAASS.ServiceDBUtenti.repositories.UtenteRepository;
import TAASS.ServiceDBUtenti.services.SecureUserService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class PublishService {

    private final RabbitTemplate rabbitTemplate;
    private final UtenteRepository userRepository;

    public PublishService(RabbitTemplate rabbitTemplate, UtenteRepository userRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.userRepository = userRepository;
    }


    public void publishNotification(String queue, long userId) {
        Utente user = userRepository.findById(userId).get();
        rabbitTemplate.convertAndSend(queue,
                new UserMessage(userId,user));
    }
}