package TAASS.ServiceDBUtenti.rabbitMQ;

import TAASS.ServiceDBUtenti.exception.MyCustomException;
import TAASS.ServiceDBUtenti.models.Utente;
import TAASS.ServiceDBUtenti.repositories.UtenteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class ListenerService {

    public static final Logger logger = LoggerFactory.getLogger(ListenerService.class);
    private final PublishService publishServ;
    private final UtenteRepository utenteRepository;

    public ListenerService(PublishService publishServ, UtenteRepository utenteRepository) {
        this.publishServ = publishServ;
        this.utenteRepository = utenteRepository;
    }

    @RabbitListener(queues = "eventSubscriptionRequest")
    public void getPojo(UserMessage message) {
        System.out.println("RABBITMQ RECEIVED: UserId:" + message.userId + " EventId: " + message.eventId + " isSubscription: " + message.isSubscription);

        eventSubscription(message);
        //publishServ.publishNotification("publishUserGateway",message.userId);

    }


    public void eventSubscription(UserMessage message){
        Utente user = utenteRepository.findById(message.userId).isPresent()? utenteRepository.findById(message.userId).get() : null;

        if(user!=null){
            if(message.isSubscription)
                user.getIscrizioni().add(message.eventId);
            else
                user.getIscrizioni().remove(message.eventId);

            utenteRepository.save(user);
        }else {
            throw new MyCustomException("RABBITMQ: USER NOT FOUND", HttpStatus.NOT_FOUND);
        }
    }
}