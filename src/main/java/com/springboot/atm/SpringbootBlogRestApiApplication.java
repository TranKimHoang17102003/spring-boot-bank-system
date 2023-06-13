package com.springboot.atm;

import com.springboot.atm.entity.Money;
import com.springboot.atm.entity.Role;
import com.springboot.atm.entity.User;
import com.springboot.atm.repository.RoleRepository;
import com.springboot.atm.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringbootBlogRestApiApplication implements CommandLineRunner {

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringbootBlogRestApiApplication.class, args);
	}

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public void run(String... args) throws Exception {
		if(!roleRepository.existsByName("ROLE_USER")) {
			Role role = new Role(1L, "ROLE_USER");
			roleRepository.save(role);
		}
		if(!roleRepository.existsByName("ROLE_ADMIN")) {
			Role role = new Role(2L, "ROLE_ADMIN");
			roleRepository.save(role);
		}
	}
}
