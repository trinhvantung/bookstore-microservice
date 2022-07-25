package vn.trinhtung;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import vn.trinhtung.entity.Role;
import vn.trinhtung.entity.User;
import vn.trinhtung.repository.RoleRepository;
import vn.trinhtung.repository.UserRepository;

@SpringBootApplication
public class UserServiceApplication implements CommandLineRunner {
	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Transactional
	@Override
	public void run(String... args) throws Exception {
		if (roleRepository.findAll().isEmpty()) {
			List<Role> roles = new ArrayList<>();
			roles.add(new Role(null, "ADMIN"));
			roles.add(new Role(null, "USER"));

			roleRepository.saveAll(roles);
		}

		if (userRepository.findAll().isEmpty()) {
			List<User> users = new ArrayList<>();
			users.add(User.builder().email("tungvlhy@gmail.com").password(passwordEncoder.encode("12345"))
					.fullname("Trinh Van Tung").enable(true).nonLocked(true)
					.roles(Collections.singleton(roleRepository.findByName("ADMIN").get())).build());

			users.add(User.builder().email("tvtung07122001@gmail.com").password(passwordEncoder.encode("12345"))
					.fullname("Trinh Tung").enable(true).nonLocked(true)
					.roles(Collections.singleton(roleRepository.findByName("USER").get())).build());

			userRepository.saveAll(users);
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
