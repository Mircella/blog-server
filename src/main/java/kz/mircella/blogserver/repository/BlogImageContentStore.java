package kz.mircella.blogserver.repository;

import kz.mircella.blogserver.domain.model.BlogImage;
import org.springframework.content.commons.repository.ContentStore;

public interface BlogImageContentStore extends ContentStore<BlogImage, String> {
}
