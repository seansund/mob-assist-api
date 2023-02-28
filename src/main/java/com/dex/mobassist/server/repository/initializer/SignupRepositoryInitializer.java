package com.dex.mobassist.server.repository.initializer;

import com.dex.mobassist.server.model.AssignmentSet;
import com.dex.mobassist.server.model.SignupOptionSet;
import com.dex.mobassist.server.repository.AssignmentSetRepository;
import com.dex.mobassist.server.repository.SignupOptionSetRepository;
import com.dex.mobassist.server.repository.SignupRepository;
import lombok.NonNull;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

import static com.dex.mobassist.server.model.Signup.createSignup;

@Component
@Profile("initialize")
public class SignupRepositoryInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final SignupRepository repository;
    private final AssignmentSetRepository assignmentSetRepository;
    private final SignupOptionSetRepository signupOptionSetRepository;

    public SignupRepositoryInitializer(SignupRepository repository, AssignmentSetRepository assignmentSetRepository, SignupOptionSetRepository signupOptionSetRepository) {
        this.repository = repository;
        this.assignmentSetRepository = assignmentSetRepository;
        this.signupOptionSetRepository = signupOptionSetRepository;
    }

    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        initializeRepository(repository);
    }

    private void initializeRepository(SignupRepository repository) {

        final String title = "Communion";
        final AssignmentSet assignmentSet = assignmentSetRepository.list().stream().findFirst().orElseThrow();
        final SignupOptionSet options = signupOptionSetRepository.list().stream().findFirst().orElseThrow();

        int nextId = 1;

        Stream.of(
                createSignup(nextId++, "02/19/2023", title, assignmentSet, options),
                createSignup(nextId++, "03/19/2023", title, assignmentSet, options),
                createSignup(nextId++, "04/16/2023", title, assignmentSet, options),
                createSignup(nextId++, "05/21/2023", title, assignmentSet, options),
                createSignup(nextId++, "06/18/2023", title, assignmentSet, options),
                createSignup(nextId++, "07/16/2023", title, assignmentSet, options),
                createSignup(nextId++, "08/20/2023", title, assignmentSet, options),
                createSignup(nextId++, "09/17/2023", title, assignmentSet, options),
                createSignup(nextId++, "10/15/2023", title, assignmentSet, options),
                createSignup(nextId++, "11/19/2023", title, assignmentSet, options),
                createSignup(nextId++, "12/17/2023", title, assignmentSet, options),
                createSignup(nextId++, "01/21/2024", title, assignmentSet, options)
        )
                .forEach(repository::addUpdate);
    }
}
