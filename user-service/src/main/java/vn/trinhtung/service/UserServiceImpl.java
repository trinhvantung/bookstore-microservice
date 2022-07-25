package vn.trinhtung.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.trinhtung.dto.RoleDto;
import vn.trinhtung.dto.UserDto;
import vn.trinhtung.entity.User;
import vn.trinhtung.exception.ResourceNotFoundException;
import vn.trinhtung.repository.RoleRepository;
import vn.trinhtung.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final RoleRepository roleRepository;

	@Transactional
	@Override
	public UserDto register(UserDto userDto) {
		User user = User.builder().email(userDto.getEmail()).fullname(userDto.getFullname())
				.password(passwordEncoder.encode(userDto.getPassword()))
				.roles(Collections.singleton(roleRepository.findByName("USER").get())).build();

		User saved = userRepository.save(user);
		return UserDto.builder().email(saved.getEmail()).fullname(saved.getFullname())
				.id(saved.getId())
				.roles(saved.getRoles().stream()
						.map(role -> new RoleDto(role.getId(), role.getName()))
						.collect(Collectors.toSet()))
				.build();
	}

	@Override
	public Page<UserDto> getAll(Integer page) {
		Sort sort = Sort.by("id").descending();
		Pageable pageable = PageRequest.of(page - 1, 10, sort);

		return userRepository.findAll(pageable)
				.map(user -> UserDto.builder().email(user.getEmail()).fullname(user.getFullname())
						.roles(user.getRoles().stream()
								.map(role -> new RoleDto(role.getId(), role.getName()))
								.collect(Collectors.toSet()))
						.id(user.getId()).build());
	}

	@Transactional
	@Override
	public void deleteById(Integer id) {
		try {
			userRepository.deleteById(id);
		} catch (IllegalArgumentException e) {
			throw new ResourceNotFoundException("Tài khoản không tồn tại");
		}
	}

	@Override
	public List<UserDto> getByIds(List<Integer> ids) {

		return userRepository.findAllById(ids).stream().map(
				user -> UserDto.builder().id(user.getId()).fullname(user.getFullname()).build())
				.collect(Collectors.toList());
	}

}
