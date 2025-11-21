package org.labcabrera.sample.archetype;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SampleArchetypeApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void contextLoads() {
	}

	@Test
	void testHelloWorldEndpoint() {
		webTestClient.get()
			.uri("/api/v1/counters")
			.exchange()
			.expectStatus().isOk()
			.expectBodyList(Object.class);
	}

	@Test
	void testCounterIncrement() {
		webTestClient.post()
			.uri("/api/v1/counters/test-counter")
			.exchange()
			.expectStatus().isOk()
			.expectBody(Integer.class)
			.isEqualTo(1);
		webTestClient.post()
			.uri("/api/v1/counters/test-counter")
			.exchange()
			.expectStatus().isOk()
			.expectBody(Integer.class)
			.isEqualTo(2);
	}
}
