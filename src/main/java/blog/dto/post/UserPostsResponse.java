package blog.dto.post;

import blog.dto.user.UserViewResponse;

import java.util.List;

public record UserPostsResponse(

    UserViewResponse user,

    List<UserPostResponse> posts

) {}
