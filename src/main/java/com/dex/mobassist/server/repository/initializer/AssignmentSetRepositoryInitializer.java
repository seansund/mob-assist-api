package com.dex.mobassist.server.repository.initializer;

import com.dex.mobassist.server.model.AssignmentSet;
import com.dex.mobassist.server.repository.AssignmentSetRepository;
import lombok.NonNull;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

import static com.dex.mobassist.server.model.Assignment.createAssignment;

@Component
@Profile("initialize")
public class AssignmentSetRepositoryInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final AssignmentSetRepository repository;

    public AssignmentSetRepositoryInitializer(AssignmentSetRepository repository) {
        this.repository = repository;
    }

    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        iniitialzeRepository(repository);
    }

    protected void iniitialzeRepository(AssignmentSetRepository repository) {
        int nextId = 1;
        Stream.of(
                AssignmentSet.createAssignmentSet(
                        nextId++,
                        "basic",
                        List.of(
                                createAssignment("Table 2", "A1"),
                                createAssignment("Table 2", "A2"),
                                createAssignment("Table 2", "B3"),
                                createAssignment("Table 2", "B4"),
                                createAssignment("Table 2", "E9"),
                                createAssignment("Table 2", "E10"),
                                createAssignment("Table 2", "F11"),
                                createAssignment("Table 2", "F12"),
                                createAssignment("Table 2", "G13"),
                                createAssignment("Table 2", "G14"),
                                createAssignment("Table 3", "C5"),
                                createAssignment("Table 3", "C6"),
                                createAssignment("Table 3", "D7"),
                                createAssignment("Table 3", "D8"),
                                createAssignment("Table 3", "H15"),
                                createAssignment("Table 3", "H16"),
                                createAssignment("Table 3", "I17"),
                                createAssignment("Table 3", "I18"),
                                createAssignment("Table 3", "J19"),
                                createAssignment("Table 3", "J20"),
                                createAssignment("Table 4", "E21"),
                                createAssignment("Table 4", "E22"),
                                createAssignment("Table 4", "F23"),
                                createAssignment("Table 4", "F24"),
                                createAssignment("Table 4", "G25"),
                                createAssignment("Table 4", "G26"),
                                createAssignment("Table 5", "H27"),
                                createAssignment("Table 5", "H28"),
                                createAssignment("Table 5", "I29"),
                                createAssignment("Table 5", "I30"),
                                createAssignment("Table 5", "J31"),
                                createAssignment("Table 5", "J32")
                        )
                )
        )
                .forEach(repository::addUpdate);
    }
}
