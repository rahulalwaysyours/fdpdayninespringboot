package com.example.firstspringbootbyrahul;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class FirstspringbootbyrahulApplication {

	public static void main(String[] args) {
		SpringApplication.run(FirstspringbootbyrahulApplication.class, args);
	}
}

@Component
class User{
	@Autowired
	Order order;

	public User(){
		System.out.println("User Constructor invoked");
	}
}

@Component
class Order{
	public Order(){
		System.out.println("Order Constructor invoked");
	}
}