package com.example.APIGateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

@SpringBootApplication
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}
	
	@Bean
	 public RouteLocator customRouteLocator(RouteLocatorBuilder builder)  {
	 return builder.routes()
	  .route("path_route", r -> r.path("/books/**").and().method("POST", "PUT", "DELETE")
	  .uri("http://localhost:8090"))
	  .route("path_route", r -> r.path("/books/**").and().method("GET")
	  .uri("http://localhost:8080"))
	  .build();
	 }

}
