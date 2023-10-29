package com.demo.webflux.app.controller;

import java.net.URI;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
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

	public Mono<ServerResponse> getAll() {
		return ServerResponse.ok().body(this.productService.getAllProducts(), ProductDocument.class);
	}

	public Mono<ServerResponse> getById(ServerRequest request) {
		return this.productService.getProductById(request.pathVariable("id"))
				.flatMap(product -> ServerResponse.ok().body(BodyInserters.fromValue(product)))
				.switchIfEmpty(ServerResponse.notFound().build());
	}

	public Mono<ServerResponse> create(ServerRequest request) {
		Mono<ProductDocument> monoProduct = request.bodyToMono(ProductDocument.class);
		return monoProduct.flatMap(this.productService::createProduct)
				.flatMap(product -> ServerResponse.created(this.getLocation(product.getId())).bodyValue(product));
	}

	public Mono<ServerResponse> createWithImage(ServerRequest request) {

		Mono<FilePart> monoFile = request.multipartData().map(multipart -> multipart.get("file")).cast(FilePart.class);

		Mono<ProductDocument> monoProduct = request.multipartData().map(multipart -> {
			String name = this.getField(multipart, "name");
			String value = this.getField(multipart, "value");
			return new ProductDocument(name, Double.parseDouble(value));
		});

		return monoFile.zipWith(monoProduct, (file, product) -> this.productService.createProduct(file, product))
				.flatMap(savedProduct -> savedProduct.flatMap(
						product -> ServerResponse.created(this.getLocation(product.getId())).bodyValue(product)));
	}

	private URI getLocation(String id) {
		String location = String.format("/api/v2/products/%s", id);
		return URI.create(location);
	}

	private String getField(MultiValueMap<String, Part> multipart, String fileName) {
		return ((FormFieldPart) multipart.get(fileName)).value();
	}
}
