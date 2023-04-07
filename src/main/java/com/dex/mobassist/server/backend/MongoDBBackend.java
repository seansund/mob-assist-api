package com.dex.mobassist.server.backend;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import graphql.com.google.common.base.Strings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

@Configuration
public class MongoDBBackend {
    final private MongoDBConfig config;

    public MongoDBBackend(MongoDBConfig config) {
        this.config = config;
    }

    protected SSLContext buildSSLContext() {
        try {
            final String certificateDecoded = new String(Base64.getDecoder().decode(config.getCaCertBase64()));

            System.out.println("Got CA certificate: " + certificateDecoded);

            InputStream inputStream = new ByteArrayInputStream(certificateDecoded.getBytes(StandardCharsets.UTF_8));
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            X509Certificate caCert = (X509Certificate) certificateFactory.generateCertificate(inputStream);

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null); // You don't need the KeyStore instance to come from a file.
            keyStore.setCertificateEntry("caCert", caCert);

            trustManagerFactory.init(keyStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

            return sslContext;
        } catch (Exception ex) {
            throw new RuntimeException("Error creating SSLContext", ex);
        }
    }

    @Bean
    @Profile("mongodb")
    public MongoClient mongo() {
        System.out.println("Found mongo connect string: " + config.getDatabaseUri());

        ConnectionString connectionString = new ConnectionString(config.getDatabaseUri());
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .credential(MongoCredential.createCredential(config.getDatabaseUsername(), "admin", config.getDatabasePassword().toCharArray()))
                .applyToSslSettings(builder -> {
                    if (!Strings.isNullOrEmpty(config.getCaCertBase64())) {
                        System.out.println("No CA cert found. Skipping SSL settings");
                        builder.invalidHostNameAllowed(true)
                                .enabled(true)
                                .context(buildSSLContext());
                    } else {
                        System.out.println("No CA cert found. Skipping SSL settings");
                    }
                })
                .build();

        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    @Profile("mongodb")
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongo(), config.getDatabaseName());
    }
}
