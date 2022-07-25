package vn.trinhtung.service;

import java.util.List;
import org.springframework.data.domain.Page;

import vn.trinhtung.dto.UserDto;

public interface UserService {
	UserDto register(UserDto user);
	
	Page<UserDto> getAll(Integer page);
	
	void deleteById(Integer id);
	
	List<UserDto> getByIds(List<Integer> ids);
}
