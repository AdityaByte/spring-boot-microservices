package com.microservice.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.order.dto.OrderLineItemsDto;
import com.microservice.order.dto.OrderRequest;
import com.microservice.order.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;

import java.math.BigDecimal;
import java.util.List;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
class OrderServiceApplicationTests {

	@Autowired
	private MySQLContainer<?> mySQLContainer;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	void cleanDatabase() {
		orderRepository.deleteAll();
	}

	@Test
	void shouldCreateAnOrder() throws Exception {
		OrderRequest orderRequest = getOrderRequest();
		String orderRequestString = objectMapper.writeValueAsString(orderRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/order")
				.contentType(MediaType.APPLICATION_JSON)
				.content(orderRequestString))
				.andExpect(MockMvcResultMatchers.status().isCreated());

		Assertions.assertEquals(1, orderRepository.findAll().size());
	}

	private OrderRequest getOrderRequest() {
		OrderRequest orderRequest = new OrderRequest();
		OrderLineItemsDto orderLineItemsDto = new OrderLineItemsDto();
		orderLineItemsDto.setSkuCode("iphone-13");
		orderLineItemsDto.setPrice(BigDecimal.valueOf(1200));
		orderLineItemsDto.setQuantity(1);
		orderRequest.setOrderLineItemsDtoList(List.of(orderLineItemsDto));
		return orderRequest;
	}

}
