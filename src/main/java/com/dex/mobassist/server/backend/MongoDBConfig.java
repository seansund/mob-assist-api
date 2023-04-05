package com.dex.mobassist.server.backend;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import graphql.com.google.common.base.Strings;
import lombok.Data;
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
@Data
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
}
