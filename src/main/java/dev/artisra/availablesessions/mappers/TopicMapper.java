package dev.artisra.availablesessions.mappers;

import dev.artisra.availablesessions.entities.Topic;
import dev.artisra.availablesessions.models.TopicDTO;
import org.springframework.stereotype.Component;

@Component
public class TopicMapper {
    public TopicDTO topicToTopicDTO(Topic topic) {
        return new TopicDTO(
                topic.getId(),
                topic.getSubject().getId(),
                topic.getName(),
                topic.getDescription()
        );
    }
}
