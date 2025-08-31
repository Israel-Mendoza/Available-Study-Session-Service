package dev.artisra.availablesessions.mappers;

import dev.artisra.availablesessions.entities.Topic;
import dev.artisra.availablesessions.models.res.TopicResponse;
import org.springframework.stereotype.Component;

@Component
public class TopicMapper {
    public TopicResponse topicToTopicDTO(Topic topic) {
        return new TopicResponse(
                topic.getId(),
                topic.getSubject().getId(),
                topic.getName(),
                topic.getDescription()
        );
    }
}
