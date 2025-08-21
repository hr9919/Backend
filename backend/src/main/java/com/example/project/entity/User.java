package com.example.project.entity;

import com.example.project.enums.SocialLoginType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;   // PK

    private String email;
    private String nickname;
    private String username;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "bio")
    private String bio;

    @Column(name = "tag_id")
    private String tagId;

    @Builder.Default
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "login_type")
    private SocialLoginType loginType;

    @Column(name = "kakao_id")
    private String kakaoId;

    @Column(name = "google_id")
    private String googleId;

    @Column(name = "naver_id")
    private String naverId;

    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_social_providers", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "provider")
    private List<String> socialProviders = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReadingLog> readingLogs = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> following = new ArrayList<>();

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followers = new ArrayList<>();

    public void addSocialProvider(String provider) {
        if (!this.socialProviders.contains(provider)) {
            this.socialProviders.add(provider);
        }
    }
}