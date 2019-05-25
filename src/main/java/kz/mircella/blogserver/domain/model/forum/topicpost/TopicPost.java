package kz.mircella.blogserver.domain.model.forum.topicpost;

import kz.mircella.blogserver.domain.model.forum.forumtopic.ForumTopic;
import kz.mircella.blogserver.domain.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Document(collection = "topic_posts")
@NoArgsConstructor
@AllArgsConstructor
@TypeAlias("topic_post")
public class TopicPost {
    @Id
    private String id;
    private String body;

    @CreatedDate
    private LocalDateTime createdAt;

    @DBRef
    private User user;

    @DBRef
    private ForumTopic forumTopic;

    public String getAuthorLogin() {
        return user.getLogin();
    }

    public static TopicPost create(String body, LocalDateTime createdAt, User user, ForumTopic forumTopic) {
        return new TopicPost(UUID.randomUUID().toString(), body, createdAt, user, forumTopic);

    }
}
