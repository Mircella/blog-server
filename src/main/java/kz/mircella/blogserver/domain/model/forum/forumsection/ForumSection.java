package kz.mircella.blogserver.domain.model.forum.forumsection;

import kz.mircella.blogserver.domain.model.forum.forumtopic.ForumTopic;
import kz.mircella.blogserver.domain.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static java.util.Collections.EMPTY_LIST;

@Data
@Document(collection = "forum_sections")
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = {"title", "createdAt"})
@TypeAlias("forum_section")
public class ForumSection {
    @Id
    private String id;

    @Indexed(unique = true)
    private String title;

    @DBRef
    private User user;

    @CreatedDate
    private LocalDateTime createdAt;

    @DBRef
    private List<ForumTopic> forumTopics;

    public String getAuthorId() {
        return getUser().getLogin();
    }

    public static ForumSection create(String title, User user, LocalDateTime createdAt) {
        return new ForumSection(UUID.randomUUID().toString(), title, user, createdAt, EMPTY_LIST);
    }
}
