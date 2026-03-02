package com.schoolapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import com.schoolapp.repository.StateRepository;
import com.schoolapp.entity.State;
import java.util.Arrays;

@SpringBootApplication
public class SchoolAppApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SchoolAppApplication.class, args);
		System.out.println("*************welcome to school project*************");
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SchoolAppApplication.class);
	}

	@Bean
	public CommandLineRunner dataInitializer(StateRepository stateRepo) {
		return args -> {
			Arrays.asList("Maharashtra", "Gujrat", "Karnataka", "Madhya Pradesh").forEach(name -> {
				if (!stateRepo.existsByName(name)) {
					stateRepo.save(State.builder().name(name).build());
				}
			});
		};
	}
}
