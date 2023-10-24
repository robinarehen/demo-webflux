package com.demo.webflux.app.document;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Document(collection = "products")
public class ProductDocument {

	@Id
	private String id;

	@NonNull
	private String name;
	@NonNull
	private Double value;

	private LocalDateTime createAt;

}
