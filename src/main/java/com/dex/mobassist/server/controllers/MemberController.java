package com.dex.mobassist.server.controllers;

import com.dex.mobassist.server.model.Member;
import com.dex.mobassist.server.model.SimpleResult;
import com.dex.mobassist.server.service.MemberService;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import reactor.adapter.rxjava.RxJava3Adapter;
import reactor.core.publisher.Flux;

import java.util.List;

@Controller
@CrossOrigin
public class MemberController {
    private final MemberService service;

    public MemberController(MemberService service) {
        this.service = service;
    }

    @QueryMapping
    public List<Member> listMembers() {
        return service.list();
    }

    @QueryMapping
    public Member getMemberByPhone(@Argument("phone") String phone) {
        return service.getById(phone);
    }

    @MutationMapping
    public Member addUpdateMember(@Argument("phone") String phone, @Argument("firstName") String firstName, @Argument("lastName") String lastName, @Argument("email") String email, @Argument("preferredContact") String preferredContact) {
        final Member member = Member.createMember(
                phone,
                firstName,
                lastName,
                email,
                preferredContact
        );

        System.out.println("Updating member: " + member);
        return service.addUpdate(member);
    }

    @MutationMapping
    public SimpleResult removeMember(@Argument("phone") String phone) {
        return new SimpleResult(service.delete(phone));
    }

    @SubscriptionMapping
    public Flux<List<Member>> members() {
        return RxJava3Adapter.observableToFlux(service.observable(), BackpressureStrategy.LATEST);
    }
}
