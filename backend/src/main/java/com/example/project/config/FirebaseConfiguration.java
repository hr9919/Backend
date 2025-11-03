package com.example.project.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@Configuration
@ConditionalOnProperty(name = "firebase.enabled", havingValue = "true", matchIfMissing = true)
public class FirebaseConfiguration {

    @Value("${firebase.credentials:}")
    private String firebaseCredentialsPath;

    @Bean
    public FirebaseApp firebaseApp() throws Exception {
        InputStream serviceAccount = null;

        try {
            ClassPathResource resource = new ClassPathResource("firebase-service-account.json");
            if (resource.exists()) {
                serviceAccount = resource.getInputStream();
            }
        } catch (Exception ignored) {}

        if (serviceAccount == null && firebaseCredentialsPath != null && !firebaseCredentialsPath.isBlank()) {
            File file = new File(firebaseCredentialsPath);
            if (file.exists()) {
                serviceAccount = new FileInputStream(file);
            }
        }

        if (serviceAccount == null) {
            String envPath = System.getenv("FIREBASE_CREDENTIALS");
            if (envPath != null && !envPath.isBlank()) {
                File file = new File(envPath);
                if (file.exists()) {
                    serviceAccount = new FileInputStream(file);
                }
            }
        }
        
        if (serviceAccount == null) {
            String envPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
            if (envPath != null && !envPath.isBlank()) {
                File file = new File(envPath);
                if (file.exists()) {
                    serviceAccount = new FileInputStream(file);
                }
            }
        }

        if (serviceAccount == null) {
            throw new IllegalStateException(
                "❌ Firebase credentials not found.\n" +
                " - Put firebase-service-account.json in classpath, OR\n" +
                " - Set firebase.credentials in application.yml, OR\n" +
                " - Set FIREBASE_CREDENTIALS / GOOGLE_APPLICATION_CREDENTIALS environment variable."
            );
        }

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        return FirebaseApp.initializeApp(options);
    }
}
