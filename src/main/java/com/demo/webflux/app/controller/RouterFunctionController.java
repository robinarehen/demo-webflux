package com.demo.webflux.app.controller;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterFunctionController {

	private static final String REQUEST_MAPPING = "/api/v2/products";

	/**
	 * route -> se deja de forma static.
	 * 
	 * GET, POST, PUT, DELETE -> se dejan de forma static.
	 * 
	 * ver lineas 3 y 4 de esta clase.
	 * 
	 * @param productHandler
	 * @return
	 */
	@Bean
	RouterFunction<ServerResponse> routesProduct(ProductHandlerController productHandler) {
		String getById = String.format("%s/{id}", REQUEST_MAPPING);
		return route(GET(REQUEST_MAPPING), productHandler::getAll)
				.andRoute(GET(getById), productHandler::getById)
				.andRoute(POST(REQUEST_MAPPING), productHandler::create);
	}
}
