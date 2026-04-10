package com.alpheratz.config;

import java.io.FileInputStream;
import java.io.InputStream;

import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import jakarta.annotation.PostConstruct;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                InputStream serviceAccount = new FileInputStream("/etc/secrets/firebase-service-account.json");

                FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

                FirebaseApp.initializeApp(options);
                System.out.println("✅ Firebase Admin inicializado");
            }
        } catch (Exception e) {
            System.err.println("❌ Error inicializando Firebase: " + e.getMessage());
        }
    }
}