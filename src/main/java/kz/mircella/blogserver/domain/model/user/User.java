package kz.mircella.blogserver.domain.model.user;

import kz.mircella.blogserver.domain.model.avatar.Avatar;
import kz.mircella.blogserver.domain.model.blogpost.BlogPost;
import kz.mircella.blogserver.domain.model.video.Video;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.Collections.emptyList;
import static kz.mircella.blogserver.domain.model.user.UserStatus.BEGINNER;

@Data
@Document
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TypeAlias("user")
@ToString(of = {"login", "birthDate", "country", "occupancy", "userStatus"})
public class User {
    @Id
    private String id;
    private String login;
    private LocalDate birthDate;
    private String country;
    private String occupancy;
    private UserStatus userStatus;

    @DBRef
    private List<BlogPost> blogPosts;

    @DBRef
    private List<Video> videos;

    @CreatedDate
    private LocalDateTime registeredAt;

    @DBRef
    private Avatar avatar;

    public void addBlogPost(BlogPost blogPost) {
        if (this.getBlogPosts() == null) {
            this.blogPosts = new ArrayList<>();
        }
        this.blogPosts.add(blogPost);
    }

    public void addVideo(Video video) {
        if (this.getVideos() == null) {
            this.videos = new ArrayList<>();
        }
        this.videos.add(video);
    }

    public static User create(String login, LocalDate birthDate, String country, String occupancy, LocalDateTime registeredAt) {
        return User.builder()
                .id(UUID.randomUUID().toString())
                .login(login)
                .birthDate(birthDate)
                .country(country)
                .occupancy(occupancy)
                .userStatus(BEGINNER)
                .blogPosts(emptyList())
                .registeredAt(registeredAt)
                .build();
    }
}