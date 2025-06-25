package com.microservice.product;

import com.microservice.product.dto.ProductRequest;
import com.microservice.product.model.Product;
import com.microservice.product.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MongoDBContainer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

	@Autowired
	private MongoDBContainer mongoDBContainer;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ProductRepository productRepository;

	@BeforeEach
	void cleanDatabase() {
		productRepository.deleteAll();
	}

	@Test
	void shouldCreateProduct() throws Exception {

		ProductRequest productRequest = getProductRequest();
		String productRequestString = objectMapper.writeValueAsString(productRequest);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
				.contentType(MediaType.APPLICATION_JSON)
				.content(productRequestString))
				.andExpect(MockMvcResultMatchers.status().isCreated());

		Assertions.assertEquals(1, productRepository.findAll().size());
	}

	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
				.name("IPHONE 13")
				.description("Iphone 13")
				.price(BigDecimal.valueOf(1200))
				.build();
	}

	@Test
	void shouldFetchProduct() throws Exception {
		Product product = getProduct();
		productRepository.save(product);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/product")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("google-buds"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("buds for seamless throughput"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].price").value(600));
	}

	private Product getProduct() {
		return Product.builder()
				.name("google-buds")
				.description("buds for seamless throughput")
				.price(BigDecimal.valueOf(600))
				.build();
	}

}
