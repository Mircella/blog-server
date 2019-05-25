package kz.mircella.blogserver.repository;


import kz.mircella.blogserver.domain.model.user.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserRepository extends ReactiveMongoRepository<User, String> {
}
