package kz.mircella.blogserver.domain.model.forum.forumtopic;

import kz.mircella.blogserver.domain.model.forum.topicpost.TopicPostDto;
import lombok.Value;

import java.util.List;

@Value
public class ForumTopicDto {
    private String title;
    private String creationDateTime;
    private String creatorId;
    private String forumSectionTitle;
    private List<TopicPostDto> topicPosts;
}
