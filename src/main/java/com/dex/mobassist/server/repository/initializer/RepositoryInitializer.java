package com.dex.mobassist.server.repository.initializer;

import com.dex.mobassist.server.model.*;
import com.dex.mobassist.server.model.simple.SimpleAssignmentSet;
import com.dex.mobassist.server.model.simple.SimpleMember;
import com.dex.mobassist.server.repository.*;
import lombok.NonNull;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

import static com.dex.mobassist.server.model.simple.SimpleAssignment.createAssignment;
import static com.dex.mobassist.server.model.simple.SimpleSignup.createSignup;
import static com.dex.mobassist.server.model.simple.SimpleSignupOption.createSignupOption;
import static com.dex.mobassist.server.model.simple.SimpleSignupOptionSet.createSignupOptionSet;

@Component
@Profile("initialize")
public class RepositoryInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final SignupRepository signupRepository;
    private final AssignmentSetRepository assignmentSetRepository;
    private final SignupOptionSetRepository signupOptionSetRepository;
    private final SignupOptionRepository signupOptionRepository;
    private final AssignmentRepository assignmentRepository;
    private final MemberRepository memberRepository;

    public RepositoryInitializer(
            SignupRepository signupRepository,
            AssignmentSetRepository assignmentSetRepository,
            SignupOptionSetRepository signupOptionSetRepository,
            SignupOptionRepository signupOptionRepository,
            AssignmentRepository assignmentRepository,
            MemberRepository memberRepository
    ) {
        this.signupRepository = signupRepository;
        this.assignmentSetRepository = assignmentSetRepository;
        this.signupOptionSetRepository = signupOptionSetRepository;
        this.signupOptionRepository = signupOptionRepository;
        this.assignmentRepository = assignmentRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        initializeAssignmentRepository(assignmentRepository);
        initializeAssignmentSetRepository(assignmentSetRepository);

        initializeMemberRepository(memberRepository);

        initializeSignupOptionRepository(signupOptionRepository);
        initializeSignupOptionSetRepository(signupOptionSetRepository);
        initializeSignupRepository(signupRepository);
    }

    protected void initializeAssignmentRepository(AssignmentRepository repository) {
        Stream.of(
                        createAssignment("Table 2", "A1", 1),
                        createAssignment("Table 2", "A2", 1),
                        createAssignment("Table 2", "B3", 1),
                        createAssignment("Table 2", "B4", 1),
                        createAssignment("Table 2", "E9", 2),
                        createAssignment("Table 2", "E10", 2),
                        createAssignment("Table 2", "F11", 2),
                        createAssignment("Table 2", "F12", 2),
                        createAssignment("Table 2", "G13", 2),
                        createAssignment("Table 2", "G14", 2),
                        createAssignment("Table 3", "C5", 1),
                        createAssignment("Table 3", "C6", 1),
                        createAssignment("Table 3", "D7", 1),
                        createAssignment("Table 3", "D8", 1),
                        createAssignment("Table 3", "H15", 2),
                        createAssignment("Table 3", "H16", 2),
                        createAssignment("Table 3", "I17", 2),
                        createAssignment("Table 3", "I18", 2),
                        createAssignment("Table 3", "J19", 2),
                        createAssignment("Table 3", "J20", 2),
                        createAssignment("Table 4", "E21", 3),
                        createAssignment("Table 4", "E22", 3),
                        createAssignment("Table 4", "F23", 3),
                        createAssignment("Table 4", "F24", 3),
                        createAssignment("Table 4", "G25", 3),
                        createAssignment("Table 4", "G26", 3),
                        createAssignment("Table 5", "H27", 3),
                        createAssignment("Table 5", "H28", 3),
                        createAssignment("Table 5", "I29", 3),
                        createAssignment("Table 5", "I30", 3),
                        createAssignment("Table 5", "J31", 3),
                        createAssignment("Table 5", "J32", 3)
                )
                .forEach(repository::addUpdate);

    }

    protected void initializeAssignmentSetRepository(AssignmentSetRepository repository) {

        Stream.of(
                        SimpleAssignmentSet.createAssignmentSet(
                                "basic",
                                assignmentRepository.list()
                        )
                )
                .forEach(repository::addUpdate);
    }

    private void initializeSignupRepository(SignupRepository repository) {

        final String title = "Communion";
        final AssignmentSet assignmentSet = assignmentSetRepository.list().stream().findFirst().orElseThrow();
        final SignupOptionSet options = signupOptionSetRepository.list().stream().findFirst().orElseThrow();

        Stream.of(
                createSignup("02/19/2023", title, assignmentSet, options),
                createSignup("03/19/2023", title, assignmentSet, options),
                createSignup("04/16/2023", title, assignmentSet, options),
                createSignup("05/21/2023", title, assignmentSet, options),
                createSignup("06/18/2023", title, assignmentSet, options),
                createSignup("07/16/2023", title, assignmentSet, options),
                createSignup("08/20/2023", title, assignmentSet, options),
                createSignup("09/17/2023", title, assignmentSet, options),
                createSignup("10/15/2023", title, assignmentSet, options),
                createSignup("11/19/2023", title, assignmentSet, options),
                createSignup("12/17/2023", title, assignmentSet, options),
                createSignup("01/21/2024", title, assignmentSet, options)
        )
                .forEach(repository::addUpdate);
    }

    protected void initializeSignupOptionSetRepository(SignupOptionSetRepository repository) {
        Stream.of(
                        createSignupOptionSet("service", signupOptionRepository.list())
                )
                .forEach(repository::addUpdate);
    }

    protected void initializeSignupOptionRepository(SignupOptionRepository repository) {
        Stream.of(
                        createSignupOption("9am"),
                        createSignupOption("10:30am"),
                        createSignupOption("No", true)
                )
                .forEach(repository::addUpdate);
    }

    protected void initializeMemberRepository(MemberRepository repository) {
        Stream.of(
                        SimpleMember.createMember("5126535564", "Sean", "Sundberg", "seansund@gmail.com", "text"),
                        SimpleMember.createMember("5128977929", "Harry", "Sundberg", "hasundberg@yahoo.com", "text")
                )
                .forEach(repository::addUpdate);
    }
}
