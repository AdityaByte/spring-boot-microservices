package com.microservice.inventory;

import com.microservice.inventory.model.Inventory;
import com.microservice.inventory.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
		return args -> {
			Inventory inventory = Inventory.builder()
					.skuCode("iphone-13")
					.quantity(100)
					.build();

			Inventory inventory1 = Inventory.builder()
					.skuCode("iphone-13-red")
					.quantity(0)
					.build();

			inventoryRepository.save(inventory);
			inventoryRepository.save(inventory1);
		};
	}

}
