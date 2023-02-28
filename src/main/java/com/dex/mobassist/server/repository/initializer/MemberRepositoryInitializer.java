package com.dex.mobassist.server.repository.initializer;

import com.dex.mobassist.server.model.Member;
import com.dex.mobassist.server.repository.MemberRepository;
import lombok.NonNull;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
@Profile("initialize")
public class MemberRepositoryInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final MemberRepository repository;

    public MemberRepositoryInitializer(MemberRepository repository) {
        this.repository = repository;
    }

    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        initializeRepository(repository);
    }

    protected void initializeRepository(MemberRepository repository) {
        Stream.of(
                Member.createMember("5126535564", "Sean", "Sundberg", "seansund@gmail.com", "text"),
                Member.createMember("5128977929", "Harry", "Sundberg", "hasundberg@yahoo.com", "text")
                )
                .forEach(repository::addUpdate);
    }
}
