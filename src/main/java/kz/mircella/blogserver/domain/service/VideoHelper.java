package kz.mircella.blogserver.domain.service;

import kz.mircella.blogserver.domain.model.user.User;
import kz.mircella.blogserver.domain.model.video.Video;
import kz.mircella.blogserver.web.videodto.VideoDetails;
import kz.mircella.blogserver.web.videodto.VideoItemDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class VideoHelper {
    private final DateTimeFormatter formatter;
    private final Clock clock;

    public VideoHelper() {
        this.formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT);
        this.clock = Clock.systemDefaultZone();
    }

    public Video createVideo(String videoTitle, String videoDescription, String videoImageUrl, String videoUrl, User user) {
        return Video.create(videoTitle, videoDescription, videoImageUrl, videoUrl, LocalDateTime.now(clock), user);
    }

    public VideoDetails mapToVideoDetails(Video video) {
        return new VideoDetails(
                video.getTitle(),
                video.getDescription(),
                video.getVideoImageUrl(),
                video.getVideoUrl(),
                String.format("Created at %s", formatter.format(video.getCreatedAt())),
                String.format("Created by %s", video.getUser().getLogin()));
    }

    public VideoItemDto mapToVideoItemDto(Video video) {
        return new VideoItemDto(
                video.getTitle(),
                String.format("Created at %s", formatter.format(video.getCreatedAt()))
        );
    }
}

