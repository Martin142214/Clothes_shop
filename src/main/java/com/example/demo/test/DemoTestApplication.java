package com.example.demo.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class DemoTestApplication {

	public static void main(String[] args) {
		System.setProperty("images.path", "E:/git/Clothes_shop/src/main/resources/static/images/");
		System.setProperty("clothes-images.path", "E:/git/Clothes_shop/src/main/resources/static/images/clothes-Images/");
		SpringApplication.run(DemoTestApplication.class, args);
	}

}
