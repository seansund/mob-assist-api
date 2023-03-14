package com.dex.mobassist.server.repository.initializer;

import com.dex.mobassist.server.cargo.*;
import com.dex.mobassist.server.model.*;
import com.dex.mobassist.server.repository.*;
import lombok.NonNull;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Component
@Profile("initialize")
public class RepositoryInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final SignupRepository signupRepository;
    private final AssignmentSetRepository assignmentSetRepository;
    private final SignupOptionSetRepository signupOptionSetRepository;
    private final SignupOptionRepository signupOptionRepository;
    private final AssignmentRepository assignmentRepository;
    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;

    public RepositoryInitializer(
            SignupRepository signupRepository,
            AssignmentSetRepository assignmentSetRepository,
            SignupOptionSetRepository signupOptionSetRepository,
            SignupOptionRepository signupOptionRepository,
            AssignmentRepository assignmentRepository,
            MemberRepository memberRepository,
            MemberRoleRepository memberRoleRepository
    ) {
        this.signupRepository = signupRepository;
        this.assignmentSetRepository = assignmentSetRepository;
        this.signupOptionSetRepository = signupOptionSetRepository;
        this.signupOptionRepository = signupOptionRepository;
        this.assignmentRepository = assignmentRepository;
        this.memberRepository = memberRepository;
        this.memberRoleRepository = memberRoleRepository;
    }

    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        System.out.println("Initializing database");

        initializeAssignmentRepository(assignmentRepository);
        initializeAssignmentSetRepository(assignmentSetRepository);

        initializeMemberRoleRepository(memberRoleRepository);
        initializeMemberRepository(memberRepository);

        initializeSignupOptionRepository(signupOptionRepository);
        initializeSignupOptionSetRepository(signupOptionSetRepository);
        initializeSignupRepository(signupRepository);

        System.out.println("Database initialization complete!");
    }

    protected Assignment createAssignment(String name, String group, int row) {
        return new AssignmentCargo()
                .withName(name)
                .withGroup(group)
                .withRow(row);
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
                .forEach(repository::save);

    }

    protected AssignmentSet createAssignmentSet(String name, List<? extends Assignment> assignments) {
        return new AssignmentSetCargo()
                .withName(name)
                .withAssignments(assignments);
    }

    protected void initializeAssignmentSetRepository(AssignmentSetRepository repository) {

        Stream.of(
                        createAssignmentSet(
                                "basic",
                                assignmentRepository.findAll()
                        )
                )
                .forEach(repository::save);
    }

    protected Signup createSignup(String date, String title, AssignmentSet assignmentSet, SignupOptionSet options) {
        return new SignupCargo()
                .withDate(date)
                .withTitle(title)
                .withAssignments(assignmentSet)
                .withOptions(options);
    }

    protected void initializeSignupRepository(SignupRepository repository) {

        final String title = "Communion";
        final AssignmentSet assignmentSet = assignmentSetRepository.findAll().stream().findFirst().orElseThrow();
        final SignupOptionSet options = signupOptionSetRepository.findAll().stream().findFirst().orElseThrow();

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
                .forEach(repository::save);
    }

    protected SignupOptionSet createSignupOptionSet(String name, List<? extends SignupOption> options) {
        return new SignupOptionSetCargo()
                .withName(name)
                .withOptions(options);
    }

    protected void initializeSignupOptionSetRepository(SignupOptionSetRepository repository) {
        Stream.of(
                        createSignupOptionSet("service", signupOptionRepository.findAll())
                )
                .forEach(repository::save);
    }

    protected SignupOption createSignupOption(@NonNull String value, @NonNull Integer sortIndex) {
        return createSignupOption(value, sortIndex, false);
    }

    protected SignupOption createSignupOption(@NonNull String value, boolean declineOption) {
        return createSignupOption(value, 10000, declineOption);
    }

    protected SignupOption createSignupOption(@NonNull String value, @NonNull Integer sortIndex, boolean declineOption) {
        return new SignupOptionCargo()
                .withValue(value)
                .withDeclineOption(declineOption);
    }

    protected void initializeSignupOptionRepository(SignupOptionRepository repository) {
        Stream.of(
                        createSignupOption("9am", 900),
                        createSignupOption("10:30am", 1030),
                        createSignupOption("No", true)
                )
                .forEach(repository::save);
    }

    protected Member createMember(@NonNull String phone, String firstName, String lastName, String email, String preferredContact, List<? extends MemberRole> roles) {
        return new MemberCargo(phone)
                .withPhone(phone)
                .withEmail(email)
                .withFirstName(firstName)
                .withLastName(lastName)
                .withPreferredContact(preferredContact)
                .withRoles(roles);
    }

    protected MemberRole createMemberRole(String name) {
        return new MemberRoleCargo()
                .withName(name);
    }

    protected void initializeMemberRoleRepository(MemberRoleRepository repository) {
        Stream.of(
                createMemberRole("user"),
                createMemberRole("admin")
        )
                .forEach(repository::save);
    }

    protected void initializeMemberRepository(MemberRepository repository) {
        final List<? extends MemberRole> allRoles = memberRoleRepository.findAll();
        final List<? extends MemberRole> roles = allRoles.stream().filter((role) -> role.getName().equals("user")).toList();

        Stream.of(
                        createMember("5126535564", "Sean", "Sundberg", "seansund@gmail.com", "text", allRoles),
                        createMember("5128977929", "Harry", "Sundberg", "hasundberg@yahoo.com", "text", roles)
                )
                .forEach(repository::save);
    }
}
