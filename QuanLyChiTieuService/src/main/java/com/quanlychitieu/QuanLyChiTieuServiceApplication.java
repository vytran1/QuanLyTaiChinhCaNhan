package com.quanlychitieu;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.quanlychitieu.common.user.User;

@SpringBootApplication
public class QuanLyChiTieuServiceApplication {
    
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(QuanLyChiTieuServiceApplication.class, args);
	}
}
