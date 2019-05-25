package kz.mircella.blogserver.domain.model.forum.topicpost;

import lombok.Value;

@Value
public class TopicPostDto {
    private String body;
    private String createdAt;
    private String authorId;
}
