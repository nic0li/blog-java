package blog.dto.auth;

import blog.dto.user.UserResponse;

public record LoginResponse(

    UserResponse user,

    String token

) {}
