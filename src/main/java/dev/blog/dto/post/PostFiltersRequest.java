package dev.blog.dto.post;

import org.springframework.util.StringUtils;

public record PostFiltersRequest(
        String title,
        String category
) {
    public boolean hasTitle() {
        return StringUtils.hasText(title);
    }

    public boolean hasCategory() {
        return StringUtils.hasText(category);
    }
}
