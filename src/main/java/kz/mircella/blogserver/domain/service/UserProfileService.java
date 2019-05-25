package kz.mircella.blogserver.domain.service;

import kz.mircella.blogserver.domain.model.avatar.Avatar;
import kz.mircella.blogserver.domain.model.user.User;
import kz.mircella.blogserver.domain.model.user.UserDto;
import kz.mircella.blogserver.repository.AvatarContentStore;
import kz.mircella.blogserver.repository.AvatarRepository;
import kz.mircella.blogserver.repository.UserRepository;
import kz.mircella.blogserver.web.userdto.UserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.time.Duration;
import java.util.function.Function;

@Service
@Slf4j
public class UserProfileService {
    private final UserRepository userRepository;
    private final AvatarRepository avatarRepository;
    private final AvatarContentStore avatarContentStore;
    private final UserHelper userHelper;


    public UserProfileService(UserRepository userRepository,
                              AvatarRepository avatarRepository,
                              AvatarContentStore avatarContentStore) {
        this.userRepository = userRepository;
        this.avatarRepository = avatarRepository;
        this.avatarContentStore = avatarContentStore;
        this.userHelper = new UserHelper();
    }

    public Mono<String> saveNewUser(Mono<UserDetails> userDetails) {
        Mono<User> userMono = userDetails.flatMap(user -> userRepository.save(userHelper.mapToUser(user)));
        return userMono.map(User::getId);
    }

    Mono<String> updateUser(User user) {
        String userId = user.getId();
        Mono<User> userMono = userRepository.findById(userId).flatMap(userRepository::save);
        return userMono.map(User::getId);
    }

    public Mono<UserDto> getUserProfile(String userId) {
        Mono<User> userMono = userRepository.findById(userId);
        return userMono.map(userHelper::mapToUserProfile);
    }

    Mono<User> getUser(String userId) {
        return userRepository.findById(userId);
    }

    public Flux<UserDto> getAllUserProfiles() {
        Flux<User> users = userRepository.findAll().delayElements(Duration.ofMillis(1000));
        return users.map(userHelper::mapToUserProfile);
    }

    public Mono<String> saveUserAvatar(String userId, String avatarContentType, InputStream avatarFileContent) {
        return userRepository.findById(userId)
                .flatMap(foundUser -> {
                    Avatar avatar = new Avatar(avatarContentType, foundUser);
                    avatarContentStore.setContent(avatar, avatarFileContent);
                    return avatarRepository.save(avatar).flatMap(updateUserWithAvatar(foundUser, avatar));
                }).map(Function.identity());
    }

    public Mono<InputStream> getUserAvatar(String userId) {
        return userRepository.findById(userId)
                .flatMap(avatarRepository::findByUser)
                .map(avatarContentStore::getContent);
    }

    private Function<Avatar, Mono<String>> updateUserWithAvatar(User foundUser, Avatar avatar) {
        return savedAvatar -> {
            log.info("Image for {} was saved", savedAvatar.getUser().getLogin());
            foundUser.setAvatar(savedAvatar);
            return userRepository.save(foundUser).map(updatedUser -> avatar.getId());
        };
    }
}
