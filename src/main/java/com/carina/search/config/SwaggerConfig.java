package com.carina.search.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
/**
 * Configure swagger docs at http://localhost:8080/swagger-ui.html
 * 
 * @author Karen Rawlinson
 */
@Configuration
public class SwaggerConfig {
	
	public OpenAPI openAPI() {
		return new OpenAPI().info(new Info().title("Search API").description("Search API Documentation"));		
	}
}
