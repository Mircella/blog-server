package kz.mircella.blogserver.web.forumdto;

import lombok.Value;

@Value
public class TopicPostCreateDto {
    private String forumTopicTitle;
    private String authorId;
    private String body;
}
