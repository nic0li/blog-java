package dev.blog.dto.post;

import org.springframework.util.StringUtils;

public record PostRequest(
        String title,
        String content,
        Long categoryId
) {
    public boolean hasTitle() {
        return StringUtils.hasText(title);
    }

    public boolean hasContent() {
        return StringUtils.hasText(content);
    }

    public boolean hasCategoryId() {
        return categoryId != null;
    }
}
