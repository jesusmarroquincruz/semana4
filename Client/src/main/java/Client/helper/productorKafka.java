package Client.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class productorKafka {

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplat;
    private final String KafkaTopic="topico";

    public void send(String message){
        kafkaTemplat.send(KafkaTopic,message);
    }

}
