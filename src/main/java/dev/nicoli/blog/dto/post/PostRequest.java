package dev.nicoli.blog.dto.post;

public record PostRequest(
        String title,
        String content,
        Long categoryId
) {
    public boolean hasTitle() {
        return title != null && !title.isBlank();
    }

    public boolean hasContent() {
        return content != null && !content.isBlank();
    }

    public boolean hasCategoryId() {
        return categoryId != null;
    }
}
