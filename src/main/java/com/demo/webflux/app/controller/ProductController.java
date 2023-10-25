package com.demo.webflux.app.controller;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.demo.webflux.app.document.ProductDocument;
import com.demo.webflux.app.service.ProductService;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {

	private ProductService productService;

	@GetMapping("/load-init-data")
	public ResponseEntity<Void> loadInitData() {
		this.productService.loadInitData();
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping
	public Mono<ResponseEntity<Flux<ProductDocument>>> getAll() {
		return Mono.just(ResponseEntity.ok(this.productService.getAllProducts()));
	}

	@GetMapping("/{id}")
	public Mono<ResponseEntity<ProductDocument>> getById(@PathVariable String id) {
		return this.productService.getProductById(id).map(product -> ResponseEntity.ok(product))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PostMapping
	public Mono<ResponseEntity<ProductDocument>> create(@RequestBody ProductDocument product) {
		return this.productService.createProduct(product).map(savedProduct -> {
			String location = String.format("/api/products/%s", savedProduct.getId());
			return ResponseEntity.created(URI.create(location)).body(savedProduct);
		});
	}
	
	@PostMapping("/with-image")
	public Mono<ResponseEntity<ProductDocument>> createWithImage(@RequestPart FilePart file ,ProductDocument product) {
		return this.productService.createProduct(file, product).map(savedProduct -> {
			String location = String.format("/api/products/%s", savedProduct.getId());
			return ResponseEntity.created(URI.create(location)).body(savedProduct);
		});
	}
}
