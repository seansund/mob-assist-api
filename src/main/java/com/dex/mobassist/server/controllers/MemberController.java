package com.dex.mobassist.server.controllers;

import com.dex.mobassist.server.cargo.MemberCargo;
import com.dex.mobassist.server.cargo.MemberRoleCargo;
import com.dex.mobassist.server.cargo.MemberRoleRefCargo;
import com.dex.mobassist.server.model.Member;
import com.dex.mobassist.server.model.MemberRole;
import com.dex.mobassist.server.model.MemberRoleRef;
import com.dex.mobassist.server.model.SimpleResult;
import com.dex.mobassist.server.service.MemberService;
import com.dex.mobassist.server.util.Strings;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import reactor.adapter.rxjava.RxJava3Adapter;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Stream;

@Controller
@CrossOrigin
public class MemberController {
    private final MemberService service;

    public MemberController(MemberService service) {
        this.service = service;
    }

    @QueryMapping
    public List<? extends Member> listMembers() {
        return service.list();
    }

    @QueryMapping
    public List<? extends MemberRole> listMemberRoles() {
        return service.listRoles();
    }

    @QueryMapping
    public Member getMemberByPhone(@Argument("phone") String phone) {
        return service.findByPhone(phone);
    }

    @MutationMapping
    public Member addUpdateMember(@Argument("phone") String phone, @Argument("firstName") String firstName, @Argument("lastName") String lastName, @Argument("email") String email, @Argument("preferredContact") String preferredContact, @Argument("roleIds") String roleIds, @Argument("password") String password) {
        final List<? extends MemberRoleRef> roles = roleIds == null
                ? null
                : Stream.of(roleIds.split(",")).map(MemberRoleRefCargo::new).toList();

        final String hashedPassword = Strings.nonNullOrEmpty(password) ? Strings.hashString(password) : null;

        final Member member = new MemberCargo(phone)
                .withPhone(phone)
                .withFirstName(firstName)
                .withLastName(lastName)
                .withEmail(email)
                .withPreferredContact(preferredContact)
                .withRoles(roles)
                .withPassword(hashedPassword);

        System.out.println("Updating member: " + member);

        return service.addUpdate(member);
    }

    @MutationMapping
    public SimpleResult removeMember(@Argument("phone") String phone) {
        return new SimpleResult(service.delete(phone));
    }

    @MutationMapping
    public MemberRole addUpdateMemberRole(@Argument("id") String id, @Argument("name") String name) {
        final MemberRole memberRole = new MemberRoleCargo(id)
                .withName(name);

        return service.addUpdateMemberRole(memberRole);
    }

    @MutationMapping
    public SimpleResult removeMemberRole(@Argument("id") String id) {
        return new SimpleResult(service.removeRole(id));
    }

    @SubscriptionMapping
    public Flux<List<? extends Member>> members() {
        return RxJava3Adapter.observableToFlux(service.observable(), BackpressureStrategy.LATEST);
    }

    @SchemaMapping(typeName="Member", field="roles")
    protected List<? extends MemberRole> loadMemberRoles(Member member) {
        System.out.println("Getting roles: " + member.getRoles());

        return (List<? extends MemberRole>) member.getRoles();
    }
}
