package kz.mircella.blogserver.repository;

import kz.mircella.blogserver.domain.model.avatar.Avatar;
import org.springframework.content.commons.repository.ContentStore;

public interface AvatarContentStore extends ContentStore<Avatar, String> {
}
