package com.dex.mobassist.server.repository.initializer;

import com.dex.mobassist.server.repository.SignupOptionSetRepository;
import lombok.NonNull;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

import static com.dex.mobassist.server.model.SignupOption.createSignupOption;
import static com.dex.mobassist.server.model.SignupOptionSet.createSignupOptionSet;

@Component
@Profile("initialize")
public class SignupOptionSetRepositoryInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final SignupOptionSetRepository repository;

    public SignupOptionSetRepositoryInitializer(SignupOptionSetRepository repository) {
        this.repository = repository;
    }

    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        initializeRepository(repository);
    }

    protected void initializeRepository(SignupOptionSetRepository repository) {
        int nextId = 1;

        Stream.of(
                createSignupOptionSet(nextId++, "service", List.of(
                        createSignupOption(nextId++, "9am"),
                        createSignupOption(nextId++, "10:30am"),
                        createSignupOption(nextId++, "No", true)
                ))
        )
                .forEach(repository::addUpdate);
    }
}
