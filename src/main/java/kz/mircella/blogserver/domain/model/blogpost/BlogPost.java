package kz.mircella.blogserver.domain.model.blogpost;

import kz.mircella.blogserver.domain.model.BlogImage;
import kz.mircella.blogserver.domain.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
@TypeAlias("blogpost")
public class BlogPost {
    @Id
    private String id;
    private String title;
    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    @DBRef
    private User user;

    @DBRef
    private List<BlogImage> blogImages;

    public static BlogPost create(String title, String content, LocalDateTime createdAt, LocalDateTime modifiedAt, User user) {
        return new BlogPost(UUID.randomUUID().toString(), title, content, createdAt, modifiedAt, user, Collections.EMPTY_LIST);
    }

    public String getAuthorId() {
        return this.getUser().getLogin();
    }
}
