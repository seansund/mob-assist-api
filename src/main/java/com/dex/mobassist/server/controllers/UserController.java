package com.dex.mobassist.server.controllers;

import com.dex.mobassist.server.model.Member;
import com.dex.mobassist.server.service.MemberService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin
public class UserController {

    private final MemberService service;

    public UserController(MemberService service) {
        this.service = service;
    }

    @QueryMapping
    public Member login(@Argument("userId") String userId, @Argument("password") String password) {
        return service.login(userId, password);
    }
}
