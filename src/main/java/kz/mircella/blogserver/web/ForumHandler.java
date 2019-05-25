package kz.mircella.blogserver.web;

import kz.mircella.blogserver.domain.model.forum.forumsection.ForumSectionDto;
import kz.mircella.blogserver.domain.model.forum.forumtopic.ForumTopicDto;
import kz.mircella.blogserver.domain.service.ForumService;
import kz.mircella.blogserver.web.forumdto.ForumSectionCreateDto;
import kz.mircella.blogserver.web.forumdto.ForumTopicCreateDto;
import kz.mircella.blogserver.web.forumdto.TopicPostCreateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class ForumHandler {

    private final ForumService forumService;

    public ForumHandler(ForumService forumService) {
        this.forumService = forumService;
    }

    Mono<ServerResponse> getForumSections() {
        Flux<ForumSectionDto> forumSectionDtoFlux = forumService.getAllForumSections();
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(forumSectionDtoFlux, ForumSectionDto.class);
    }

    Mono<ServerResponse> createForumSection(ServerRequest request) {
        String userId = request.pathVariable("userId");
        Mono<String> responseBody = request
                .bodyToMono(ForumSectionCreateDto.class)
                .flatMap(forumSectionCreateDto -> forumService.saveNewForumSection(userId, forumSectionCreateDto));
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(responseBody, String.class);
    }

    Mono<ServerResponse> createForumTopic(ServerRequest request) {
        Mono<String> responseBody = request
                .bodyToMono(ForumTopicCreateDto.class)
                .flatMap(forumService::saveNewForumTopic);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(responseBody, String.class);
    }

    Mono<ServerResponse> getForumTopic(ServerRequest request) {
        String forumTopicTitle = request.pathVariable("topicTitle");
        Mono<ForumTopicDto> responseBody = forumService.getForumTopicDto(forumTopicTitle);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(responseBody, ForumTopicDto.class);
    }

    Mono<ServerResponse> createTopicPost(ServerRequest request) {
        Mono<String> responseBody = request
                .bodyToMono(TopicPostCreateDto.class)
                .flatMap(forumService::saveNewTopicPost);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(responseBody, String.class);
    }
}
