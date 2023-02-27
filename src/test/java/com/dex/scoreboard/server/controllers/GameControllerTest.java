package com.dex.scoreboard.server.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.graphql.test.tester.WebSocketGraphQlTester;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;

import java.net.URI;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GameControllerTest {

    @LocalServerPort
    private int port;

    @Value("http://localhost:${local.server.port}${spring.graphql.websocket.path}")
    private String baseUrl;

    private GraphQlTester graphQlTester;

    @Autowired
    private GameRepository repo;

    @BeforeEach
    void setUp() {
        URI url = URI.create(baseUrl);
        this.graphQlTester = WebSocketGraphQlTester.builder(url, new ReactorNettyWebSocketClient()).build();
    }

    void getGame() {
        final GraphQlTester.EntityList<Game> value = this.graphQlTester.document("{ getGame }")
                .execute()
                .path("getGame")
                .entityList(Game.class);

        final List<Game> list = value.get();
        System.out.println("Game result: " + list);

        value.contains(Game.createGame(Team.createTeam("Grace", "navy"), Team.createTeam("Harvest", "Maroon")));
    }
}
