package com.demo.webflux.app.service.impl;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;

import com.demo.webflux.app.document.ProductDocument;
import com.demo.webflux.app.repository.ProductRepository;
import com.demo.webflux.app.service.ProductService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

	@Value("${config.upload.folder}")
	private String folder;

	private ProductRepository productRepository;

	private ProductServiceImpl(ProductRepository productRepository) {
		super();
		this.productRepository = productRepository;
	}

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

	@Override
	public Mono<ProductDocument> createProduct(FilePart file, ProductDocument product) {
		String pathFileName = this.getPathFileName(file.filename());
		product.setCreateAt(LocalDateTime.now());
		return file.transferTo(Path.of(pathFileName)).then(this.productRepository.save(product));
	}

	private String getPathFileName(String fileName) {
		String userDirectory = System.getProperty("user.dir");
		String pathFile = String.format("%s/%s", userDirectory, this.folder);
		File pathFolder = Paths.get(pathFile).toFile();
		if (!pathFolder.isDirectory()) {
			pathFolder.mkdirs();
		}
		return String.format("%s/%s", pathFile, fileName);
	}

}
