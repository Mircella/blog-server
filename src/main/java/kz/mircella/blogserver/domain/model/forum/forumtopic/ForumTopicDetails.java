package kz.mircella.blogserver.domain.model.forum.forumtopic;

import lombok.Value;

@Value
public class ForumTopicDetails {
    private String title;
    private String lastTopicPostDateTime;
    private String lastTopicPostAuthorId;
}
