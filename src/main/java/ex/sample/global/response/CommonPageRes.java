package ex.sample.global.response;

import java.util.List;
import org.springframework.data.domain.Page;

/**
 * 공통 페이지 응답 객체
 */
public record CommonPageRes<T>(
    List<T> content,
    PageMeta meta
) {

    public static <T> CommonPageRes<T> from(Page<T> page) {
        return new CommonPageRes<>(page.getContent(), PageMeta.from(page));
    }

    public record PageMeta(
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious
    ) {

        public static PageMeta from(Page<?> page) {
            return new PageMeta(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.hasPrevious()
            );
        }
    }
}
