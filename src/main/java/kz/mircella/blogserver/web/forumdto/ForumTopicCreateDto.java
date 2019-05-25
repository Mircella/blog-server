package kz.mircella.blogserver.web.forumdto;

import lombok.Value;

@Value
public class ForumTopicCreateDto {
    private String authorId;
    private String topicTitle;
    private String forumSectionTitle;
}
