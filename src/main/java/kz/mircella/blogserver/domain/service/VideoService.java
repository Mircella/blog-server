package kz.mircella.blogserver.domain.service;

import kz.mircella.blogserver.domain.model.video.Video;
import kz.mircella.blogserver.repository.VideoRepository;
import kz.mircella.blogserver.web.videodto.VideoCreateDto;
import kz.mircella.blogserver.web.videodto.VideoDetails;
import kz.mircella.blogserver.web.videodto.VideoItemDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Service
@Slf4j
public class VideoService {
    private final UserProfileService userProfileService;
    private final VideoRepository videoRepository;
    private final VideoHelper videoHelper;

    public VideoService(UserProfileService userProfileService, VideoRepository videoRepository) {
        this.userProfileService = userProfileService;
        this.videoRepository = videoRepository;
        this.videoHelper = new VideoHelper();
    }

    public Mono<String> saveVideo(String authorId, VideoCreateDto videoCreateDto) {
        String videoTitle = videoCreateDto.getTitle();
        String videoDescription = videoCreateDto.getDescription();
        String videoImageUrl = videoCreateDto.getVideoImageUrl();
        String videoUrl = videoCreateDto.getVideoUrl();
        return userProfileService.getUser(authorId)
                .flatMap(user -> {
                    Video video = videoHelper.createVideo(videoTitle, videoDescription, videoImageUrl, videoUrl, user);
                    return videoRepository.save(video).flatMap(savedVideo -> {
                        user.addVideo(savedVideo);
                        return userProfileService.updateUser(user).map(updatedUser -> video.getId());
                    });
                }).map(Function.identity());
    }

    public Mono<VideoDetails> getVideoByTitle(String title) {
        return videoRepository.findByTitle(title).map(videoHelper::mapToVideoDetails);
    }

    public Flux<VideoItemDto> getAllVideos() {
        return videoRepository.findAll().map(videoHelper::mapToVideoItemDto);
    }
}
