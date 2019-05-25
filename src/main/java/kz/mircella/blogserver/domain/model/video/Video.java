package kz.mircella.blogserver.domain.model.video;

import kz.mircella.blogserver.domain.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Video {
    @Id
    private String id;
    private String title;
    private String description;
    private String videoImageUrl;
    private String videoUrl;
    private LocalDateTime createdAt;

    @DBRef
    private User user;

    public static Video create(String title, String description, String videoImageUrl, String videoUrl, LocalDateTime createdAt, User user) {
        return Video.builder()
                .id(UUID.randomUUID().toString())
                .title(title)
                .description(description)
                .videoImageUrl(videoImageUrl)
                .videoUrl(videoUrl)
                .createdAt(createdAt)
                .user(user)
                .build();
    }
}