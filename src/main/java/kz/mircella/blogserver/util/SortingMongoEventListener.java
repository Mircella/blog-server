package kz.mircella.blogserver.util;

import org.apache.commons.beanutils.BeanComparator;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterConvertEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SortingMongoEventListener extends AbstractMongoEventListener {
    @Override
    public void onBeforeConvert(BeforeConvertEvent event) {
        Object obj = event.getSource();
        Class clazz = obj.getClass();
        ReflectionUtils.doWithFields(clazz, new SortingFieldCallBack(obj, SortPhase.BEFORE_CONVERT));
    }

    @Override
    public void onAfterConvert(AfterConvertEvent event) {
        Object obj = event.getSource();
        Class clazz = obj.getClass();
        ReflectionUtils.doWithFields(clazz, new SortingFieldCallBack(obj, SortPhase.AFTER_CONVERT));
    }

    private static class SortingFieldCallBack implements ReflectionUtils.FieldCallback {

        private Object source;
        private SortPhase sortPhase;

        public SortingFieldCallBack(Object source, SortPhase sortPhase) {
            this.source = source;
            this.sortPhase = sortPhase;
        }

        @Override
        public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
            if (field.isAnnotationPresent(OrderBy.class)) {
                OrderBy orderBy = field.getAnnotation(OrderBy.class);
                List<SortPhase> sortPhases = Arrays.asList(orderBy.phase());
                if (sortPhases.contains(sortPhase)) {
                    ReflectionUtils.makeAccessible(field);
                    Object value = field.get(source);
                    sort(value, orderBy);
                }
            }
        }

        private void sort(Object value, OrderBy orderBy) {
            if (ClassUtils.isAssignable(List.class, value.getClass())) {
                final List list = (List) value;
                if (orderBy.direction() == Sort.Direction.ASC) {
                    Collections.sort(list, new BeanComparator(orderBy.value()));
                } else {
                    Collections.sort(list, new BeanComparator(orderBy.value(), Collections.reverseOrder()));
                }
            }
        }
    }

}
