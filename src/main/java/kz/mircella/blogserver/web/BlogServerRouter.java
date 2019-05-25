package kz.mircella.blogserver.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class BlogServerRouter {

    @Bean
    public RouterFunction<ServerResponse> route(UserProfileHandler userProfileHandler,
                                                BlogPostHandler blogPostHandler,
                                                ForumHandler forumHandler,
                                                VideoHandler videoHandler) {
        return RouterFunctions
                .route(RequestPredicates.GET("/users/{userId}").and(accept(MediaType.APPLICATION_STREAM_JSON)), userProfileHandler::getUserProfile)
                .andRoute(RequestPredicates.POST("/users").and(accept(MediaType.APPLICATION_STREAM_JSON)), userProfileHandler::saveUserProfile)
                .andRoute(RequestPredicates.POST("/users/{userId}").and(accept(MediaType.MULTIPART_FORM_DATA)), userProfileHandler::saveUserAvatar)
                .andRoute(RequestPredicates.GET("/users/avatar/{userId}").and(accept(MediaType.APPLICATION_STREAM_JSON)), userProfileHandler::getUserAvatar)
                .andRoute(RequestPredicates.GET("/users").and(accept(MediaType.APPLICATION_STREAM_JSON)), serverRequest -> userProfileHandler.getUserProfiles())

                .andRoute(RequestPredicates.GET("/forum/sections").and(accept(MediaType.APPLICATION_STREAM_JSON)), serverRequest -> forumHandler.getForumSections())
                .andRoute(RequestPredicates.POST("/forum/sections/{userId}").and(accept(MediaType.APPLICATION_STREAM_JSON)), forumHandler::createForumSection)
                .andRoute(RequestPredicates.POST("/forum/sections").and(accept(MediaType.APPLICATION_STREAM_JSON)), forumHandler::createForumTopic)
                .andRoute(RequestPredicates.GET("/forum/topics/{topicTitle}").and(accept(MediaType.APPLICATION_STREAM_JSON)), forumHandler::getForumTopic)
                .andRoute(RequestPredicates.POST("/forum/topics").and(accept(MediaType.APPLICATION_STREAM_JSON)), forumHandler::createTopicPost)

                .andRoute(RequestPredicates.GET("/blog-posts/{title}").and(accept(MediaType.APPLICATION_STREAM_JSON)), blogPostHandler::getBlogPostByTitle)
                .andRoute(RequestPredicates.POST("/blog-posts/{title}").and(accept(MediaType.MULTIPART_FORM_DATA)), blogPostHandler::uploadBlogPostImages)
                .andRoute(RequestPredicates.GET("/blog-posts/{imageId}/image").and(accept(MediaType.APPLICATION_STREAM_JSON)), blogPostHandler::getBlogPostImageById)
                .andRoute(RequestPredicates.GET("/blog-posts").and(accept(MediaType.APPLICATION_STREAM_JSON)), serverRequest -> blogPostHandler.getAllBlogPosts())
                .andRoute(RequestPredicates.POST("/blog-posts/{userId}").and(accept(MediaType.APPLICATION_STREAM_JSON)), blogPostHandler::saveNewBlogPost)
                .andRoute(RequestPredicates.GET("/blog-posts/clear").and(accept(MediaType.APPLICATION_STREAM_JSON)), serverRequest -> blogPostHandler.clear())

                .andRoute(RequestPredicates.GET("/videos/{title}").and(accept(MediaType.APPLICATION_OCTET_STREAM)), videoHandler::getVideoByTitle)
                .andRoute(RequestPredicates.GET("/videos").and(accept(MediaType.APPLICATION_OCTET_STREAM)), serverRequest -> videoHandler.getAllVideos())
                .andRoute(RequestPredicates.POST("/videos/{userId}").and(accept(MediaType.APPLICATION_STREAM_JSON)), videoHandler::createVideo);
    }
}
