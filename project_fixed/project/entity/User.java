package com.example.project.entity;

import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import com.example.project.enums.SocialLoginType;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true, length = 50)
    private String nickname;

    @Column(length = 500)
    private String profileImage;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(length = 50)
    private String tagId;

    @Enumerated(EnumType.STRING)
    private SocialLoginType loginType;

    @Column(length = 255)
    private String kakaoId;

    @Column(length = 255)
    private String googleId;

    @Column(length = 255)
    private String naverId;

    @Column(nullable = false, unique = true, length = 255)
    private String username;

    @ElementCollection
    @CollectionTable(
        name = "user_social_providers",
        joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "provider")
    @Builder.Default
    private List<String> socialProviders = new ArrayList<>();
}
