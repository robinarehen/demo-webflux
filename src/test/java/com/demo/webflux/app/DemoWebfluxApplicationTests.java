package com.demo.webflux.app;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.demo.webflux.app.document.ProductDocument;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class DemoWebfluxApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void getAllTest() {

		String uri = "api/products";

		this.webTestClient.get().uri(uri).exchange().expectStatus().isOk().expectBodyList(ProductDocument.class)
				.consumeWith(response -> {
					List<ProductDocument> products = response.getResponseBody();

					assertNotNull(products);
					assertFalse(products.isEmpty());
					Integer expect = 4;
					assertThat(products).hasSizeGreaterThanOrEqualTo(expect);
				});
	}

	@Test
	void getByIdTest() {

		String id = "653e24eb8376a15b4d5235d6";
		String name = "TV";
		String uri = "api/products/{id}";

		Map<String, String> pahtVariable = Collections.singletonMap("id", id);

		this.webTestClient.get().uri(uri, pahtVariable).exchange().expectStatus().isOk().expectBody().jsonPath("$.id")
				.isNotEmpty().jsonPath("$.id").isEqualTo(id).jsonPath("$.name").isEqualTo(name).jsonPath("$.value")
				.isNumber();

		this.webTestClient.get().uri(uri, pahtVariable).exchange().expectStatus().isOk()
				.expectBody(ProductDocument.class).consumeWith(response -> {
					ProductDocument product = response.getResponseBody();
					Assertions.assertNotNull(product);
					Assertions.assertEquals(id, product.getId());
					Assertions.assertEquals(name, product.getName());
				});
	}

}
