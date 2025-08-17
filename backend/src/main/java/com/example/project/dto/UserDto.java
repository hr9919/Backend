package com.example.project.dto;

import com.example.project.entity.User;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;
    private String nickname;
    private String email;
    private String username;
    private List<String> socialProviders;

    public UserDto(User user) {
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.socialProviders = user.getSocialProviders();
    }

    public static UserDto fromEntity(User user) {
        return new UserDto(user);
    }
}
