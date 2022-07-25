package vn.trinhtung.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vn.trinhtung.dto.BookDto;

@FeignClient("catalog-service")
public interface BookFeignClient {
	@GetMapping("/catalog-service/api/book/ids")
	List<BookDto> getAllByIds(@RequestParam List<Integer> ids);
}
