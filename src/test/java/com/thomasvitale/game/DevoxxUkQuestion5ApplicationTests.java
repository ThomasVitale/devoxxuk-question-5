package com.thomasvitale.game;

import com.thomasvitale.game.model.Answers;
import com.thomasvitale.game.model.GameScore;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = DevoxxUkQuestion5Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "game.eventing.enabled=true")
@Testcontainers
public class DevoxxUkQuestion5ApplicationTests {

    private static final int REDIS_PORT = 6379;
    private static MockWebServer mockWebServer;

    @Container
    static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:7.0"))
            .withExposedPorts(REDIS_PORT);

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("game.eventing.broker-uri", () -> mockWebServer.url("/").uri());
        registry.add("spring.redis.host", () -> redis.getHost());
        registry.add("spring.redis.port", () -> redis.getMappedPort(REDIS_PORT));
    }

    @Autowired
    private WebTestClient webTestClient;

    @BeforeAll
    static void setup() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void clean() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void whenAnswersSubmittedThenGameScoreReturned() throws InterruptedException {
        var answers = new Answers("jon-snow", UUID.randomUUID().toString(), false, true, false, false, 13);

        var mockResponse = new MockResponse().setResponseCode(201);
        mockWebServer.enqueue(mockResponse);

        webTestClient
                .post()
                .uri("/answers")
                .bodyValue(answers)
                .exchange()
                .expectStatus().isOk()
                .expectBody(GameScore.class).value(gameScore -> {
                    assertThat(gameScore.player()).isEqualTo(answers.player());
                    assertThat(gameScore.sessionId()).isEqualTo(answers.sessionId());
                    assertThat(gameScore.gameTime()).isNotNull();
                    assertThat(gameScore.level()).isEqualTo("devoxxuk-question-5");
                    assertThat(gameScore.levelScore()).isEqualTo(5 + answers.remainingTime());
                });

        var recordedRequest = mockWebServer.takeRequest();
        assertThat(recordedRequest.getMethod()).isEqualTo("POST");
        assertThat(recordedRequest.getHeader(HttpHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(recordedRequest.getHeader("ce-id")).isNotEmpty();
        assertThat(recordedRequest.getHeader("ce-specversion")).isNotEmpty();
        assertThat(recordedRequest.getHeader("ce-source")).isNotEmpty();
        assertThat(recordedRequest.getHeader("ce-type")).isEqualTo("GameScoreEvent");
        assertThat(recordedRequest.getBodySize()).isPositive();
    }

}
