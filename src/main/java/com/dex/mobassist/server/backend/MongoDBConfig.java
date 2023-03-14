package com.dex.mobassist.server.backend;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import graphql.com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Value;
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
@Profile("mongodb")
public class MongoDBConfig {

    @Value("${spring.data.mongodb.uri}")
    private String databaseUri;
    @Value("${spring.data.mongodb.database}")
    private String databaseName;
    @Value("${spring.data.mongodb.username}")
    private String databaseUsername;
    @Value("${spring.data.mongodb.password}")
    private String databasePassword;

    @Value("${spring.data.mongodb.ca-cert-base64}")
    private String caCertBase64;

    protected SSLContext buildSSLContext() {
        try {
            final String certificateDecoded = new String(Base64.getDecoder().decode(caCertBase64));

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
        System.out.println("Found mongo connect string: " + databaseUri);

        ConnectionString connectionString = new ConnectionString(databaseUri);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .credential(MongoCredential.createCredential(databaseUsername, "admin", databasePassword.toCharArray()))
                .applyToSslSettings(builder -> {
                    if (!Strings.isNullOrEmpty(caCertBase64)) {
                        builder.invalidHostNameAllowed(true)
                                .enabled(true)
                                .context(buildSSLContext());
                    }
                })
                .build();

        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    @Profile("mongodb")
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongo(), databaseName);
    }
}
