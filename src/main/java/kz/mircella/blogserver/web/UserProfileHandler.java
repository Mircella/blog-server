package kz.mircella.blogserver.web;

import kz.mircella.blogserver.domain.model.user.UserDto;
import kz.mircella.blogserver.domain.service.UserProfileService;
import kz.mircella.blogserver.web.userdto.UserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.util.Map;

@Slf4j
@Component
public class UserProfileHandler {

    private final UserProfileService userProfileService;

    public UserProfileHandler(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }


    Mono<ServerResponse> getUserProfile(ServerRequest request) {
        String userId = request.pathVariable("userId");
        Mono<UserDto> userProfile = userProfileService.getUserProfile(userId);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(userProfile, UserDto.class);
    }

    Mono<ServerResponse> saveUserProfile(ServerRequest request) {
        Mono<UserDetails> userDetailsMono = request.bodyToMono(UserDetails.class);
        Mono<String> userId = userProfileService.saveNewUser(userDetailsMono);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(userId, String.class);
    }

    Mono<ServerResponse> saveUserAvatar(ServerRequest request) {
        String userId = request.pathVariable("userId");
        return request.body(BodyExtractors.toMultipartData()).flatMap(parts -> {
            Map<String, Part> partMap = parts.toSingleValueMap();
            FilePart filePart = (FilePart) partMap.get("file");
            log.info("Filename {}", filePart.filename());
            Flux<DataBuffer> dataBufferFlux = filePart.content();
            Mono<String> userIdMono = dataBufferFlux.collect(InputStreamCollector::new, (t, b) -> t.collectInputStream(b.asInputStream())).flatMap(inputStreamCollector -> {
                InputStream inputStream = inputStreamCollector.getInputStream();
                return userProfileService.saveUserAvatar(userId, MediaType.IMAGE_JPEG_VALUE, inputStream);
            });
            return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_STREAM_JSON)
                    .body(userIdMono, String.class);
        });
    }

    Mono<ServerResponse> getUserAvatar(ServerRequest request) {
        String userId = request.pathVariable("userId");
        Mono<InputStream> userAvatarMono = userProfileService.getUserAvatar(userId);
        Mono<InputStreamResource> resourceMono = userAvatarMono.map(InputStreamResource::new);
        return ServerResponse.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resourceMono, InputStreamResource.class);
    }

    Mono<ServerResponse> getUserProfiles() {
        Flux<UserDto> userProfiles = userProfileService.getAllUserProfiles();
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(userProfiles, UserDto.class);
    }
}
