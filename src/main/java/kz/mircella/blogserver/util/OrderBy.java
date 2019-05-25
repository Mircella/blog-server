package kz.mircella.blogserver.util;

import org.springframework.data.domain.Sort;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface OrderBy {
    String value();
    Sort.Direction direction() default Sort.Direction.ASC;
    SortPhase[] phase() default SortPhase.AFTER_CONVERT;
}
