package vn.trinhtung.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vn.trinhtung.dto.UserDto;

@FeignClient("user-service")
public interface UserFeignClient {
	
	@GetMapping("/user-service/api/user/ids")
	List<UserDto> getUserByIds(@RequestParam List<Integer> ids);
}
