package kz.mircella.blogserver.domain.model.avatar;

import kz.mircella.blogserver.domain.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.content.commons.annotations.ContentId;
import org.springframework.content.commons.annotations.ContentLength;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
@TypeAlias("avatar")
public class Avatar {
    @Id
    private String id;
    private String mimeType;

    @DBRef
    private User user;

    @ContentId
    private String contentId;
    @ContentLength
    private long contentLength;

    public Avatar(String avatarMimeType, User user) {
        this.id = UUID.randomUUID().toString();
        this.mimeType = avatarMimeType;
        this.user = user;
    }

    public String getUserId() {
        return this.getUser().getId();
    }
}
