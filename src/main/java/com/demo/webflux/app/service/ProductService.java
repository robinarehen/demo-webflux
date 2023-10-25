package com.demo.webflux.app.service;

import com.demo.webflux.app.document.ProductDocument;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {

	void loadInitData();

	Flux<ProductDocument> getAllProducts();

	Mono<ProductDocument> getProductById(String id);

	Mono<ProductDocument> createProduct(ProductDocument product);

}
