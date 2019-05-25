package kz.mircella.blogserver.web;

import kz.mircella.blogserver.domain.service.VideoService;
import kz.mircella.blogserver.web.videodto.VideoCreateDto;
import kz.mircella.blogserver.web.videodto.VideoDetails;
import kz.mircella.blogserver.web.videodto.VideoItemDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class VideoHandler {
    private final VideoService videoService;

    public VideoHandler(VideoService videoService) {
        this.videoService = videoService;
    }

    Mono<ServerResponse> createVideo(ServerRequest request) {
        String userId = request.pathVariable("userId");
        Mono<String> responseBody = request
                .bodyToMono(VideoCreateDto.class)
                .flatMap(videoCreateDto -> videoService.saveVideo(userId, videoCreateDto));
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(responseBody, String.class);
    }

    Mono<ServerResponse> getVideoByTitle(ServerRequest request) {
        String title = request.pathVariable("title");
        Mono<VideoDetails> videoDetailsMono = videoService.getVideoByTitle(title);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_STREAM_JSON).body(videoDetailsMono, VideoDetails.class);
    }

    public Mono<ServerResponse> getAllVideos() {
        Flux<VideoItemDto> videoItems = videoService.getAllVideos();
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(videoItems, VideoItemDto.class);
    }
}
