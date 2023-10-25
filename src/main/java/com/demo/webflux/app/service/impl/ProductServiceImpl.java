package com.demo.webflux.app.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.demo.webflux.app.document.ProductDocument;
import com.demo.webflux.app.repository.ProductRepository;
import com.demo.webflux.app.service.ProductService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

	private ProductRepository productRepository;

	@Override
	public void loadInitData() {

		this.productRepository.deleteAll().subscribe();

		Flux.just(new ProductDocument("TV", 250.00), new ProductDocument("Camara", 150.00),
				new ProductDocument("Celular", 350.00), new ProductDocument("Portatil", 450.00)).flatMap(product -> {
					product.setCreateAt(LocalDateTime.now());
					return this.productRepository.save(product);
				}).subscribe(product -> log.info("insert: {}", product));
	}

	@Override
	public Flux<ProductDocument> getAllProducts() {
		return this.productRepository.findAll();
	}

	@Override
	public Mono<ProductDocument> getProductById(String id) {
		return this.productRepository.findById(id);
	}

	@Override
	public Mono<ProductDocument> createProduct(ProductDocument product) {
		return this.productRepository.save(product);
	}

}
