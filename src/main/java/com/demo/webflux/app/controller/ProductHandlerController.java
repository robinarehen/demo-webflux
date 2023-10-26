package com.demo.webflux.app.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.demo.webflux.app.document.ProductDocument;
import com.demo.webflux.app.service.ProductService;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class ProductHandlerController {

	private ProductService productService;

	public Mono<ServerResponse> getAll(ServerRequest request) {
		return ServerResponse.ok().body(this.productService.getAllProducts(), ProductDocument.class);
	}

	public Mono<ServerResponse> getById(ServerRequest request) {
		return this.productService.getProductById(request.pathVariable("id"))
				.flatMap(product -> ServerResponse.ok().body(BodyInserters.fromValue(product)))
				.switchIfEmpty(ServerResponse.notFound().build());
	}
}
