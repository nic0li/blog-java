package dev.nicoli.blog.dto.post;

public record PostFiltersRequest(
        String title,
        String category
) {
    public boolean hasTitle() {
        return title != null && !title.isBlank();
    }

    public boolean hasCategory() {
        return category != null && !category.isBlank();
    }
}
