package com.demo.webflux.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.webflux.app.document.ProductDocument;
import com.demo.webflux.app.repository.ProductRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	@Autowired
	private ProductRepository productRepository;

	@GetMapping
	public Flux<ProductDocument> getAll() {
		return this.productRepository.findAll();
	}

	@GetMapping("/{id}")
	public Mono<ProductDocument> getById(@PathVariable String id) {
		return this.productRepository.findById(id);
	}
}
