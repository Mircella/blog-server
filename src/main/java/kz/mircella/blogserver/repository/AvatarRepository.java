package kz.mircella.blogserver.repository;

import kz.mircella.blogserver.domain.model.avatar.Avatar;
import kz.mircella.blogserver.domain.model.user.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface AvatarRepository extends ReactiveMongoRepository<Avatar, String> {
    Mono<Avatar> findByUser(User user);
}
