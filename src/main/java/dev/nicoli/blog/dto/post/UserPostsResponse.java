package dev.nicoli.blog.dto.post;

import dev.nicoli.blog.dto.user.UserViewResponse;

import java.util.List;

public record UserPostsResponse(

        UserViewResponse user,

        List<UserPostResponse> posts

) {
}
