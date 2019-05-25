package kz.mircella.blogserver.domain.model.forum.forumtopic;

import kz.mircella.blogserver.domain.model.forum.forumsection.ForumSection;
import kz.mircella.blogserver.domain.model.forum.topicpost.TopicPost;
import kz.mircella.blogserver.domain.model.user.User;
import kz.mircella.blogserver.util.OrderBy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.emptyList;

@Data
@Document(collection = "forum_topics")
@NoArgsConstructor
@AllArgsConstructor
@TypeAlias("forum_topic")
@ToString(of = {"title", "createdAt"})
public class ForumTopic {
    @Id
    private String id;
    private String title;

    @CreatedDate
    private LocalDateTime createdAt;

    @DBRef
    private User user;

    @DBRef
    private ForumSection forumSection;

    @DBRef
    @OrderBy(value = "createdAt", direction = Sort.Direction.DESC)
    private List<TopicPost> topicPosts;

    public static ForumTopic create(String title, LocalDateTime createdAt, User author, ForumSection forumSection) {
        return new ForumTopic(UUID.randomUUID().toString(), title, createdAt, author, forumSection, emptyList());
    }

    public String getCreatorLogin() {
        return user.getLogin();
    }

    public String getForumSectionTitle() {
        return forumSection.getTitle();
    }

    public Optional<LocalDateTime> getLastTopicPostDateTime() {
        return getTopicPosts().stream().findAny().map(TopicPost::getCreatedAt);
    }

    public Optional<String> getLastTopicPostAuthorLogin() {
        return getTopicPosts().stream().findAny().map(TopicPost::getAuthorLogin);
    }
}
