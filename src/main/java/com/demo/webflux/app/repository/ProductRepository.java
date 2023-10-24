package com.demo.webflux.app.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.demo.webflux.app.document.ProductDocument;

public interface ProductRepository extends ReactiveMongoRepository<ProductDocument, String> {

}
