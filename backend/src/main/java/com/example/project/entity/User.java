package com.example.project.entity;

import lombok.*;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_social_logins", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "social_provider")
    private Set<String> socialProviders = new HashSet<>();

    public User(String nickname, String username, String email) {
        this.nickname = nickname;
        this.username = username;
        this.email = email;
    }

    public void addSocialProvider(String provider) {
        this.socialProviders.add(provider);
    }
}
