package dev.nicoli.blog.dto.post;

public record PostUpdateRequest(

        String title,

        String content,

        Long categoryId

) {
}
