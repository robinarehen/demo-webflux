package com.demo.webflux.app;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.demo.webflux.app.document.ProductDocument;
import com.demo.webflux.app.repository.ProductRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@SpringBootApplication
@Slf4j
public class DemoWebfluxApplication implements CommandLineRunner {

	@Autowired
	private ProductRepository productRepository;

	public static void main(String[] args) {
		SpringApplication.run(DemoWebfluxApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		this.productRepository.deleteAll().subscribe();

		Flux.just(
				new ProductDocument("TV", 250.00), 
				new ProductDocument("Camara", 150.00),
				new ProductDocument("Celular", 350.00), 
				new ProductDocument("Portatil", 450.00)
				)
				.flatMap(product -> {
					product.setCreateAt(LocalDateTime.now());
					return this.productRepository.save(product);
				})
				.subscribe(product -> log.info("insert: {}", product));
	}

}
