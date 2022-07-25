package vn.trinhtung.service;

import org.springframework.data.domain.Page;

import vn.trinhtung.dto.OrderDto;
import vn.trinhtung.dto.OrderTrackDto;

public interface OrderService {
	OrderDto create(OrderDto orderDto);
	
	Page<OrderDto> getAllByUserId(Integer page);
	
	Page<OrderDto> getAll(Integer page);
	
	OrderTrackDto updateOrderTrack(OrderTrackDto orderTrackDto);
	
	OrderDto getById(Integer id);
	
	OrderDto getByIdAndUser(Integer id);
}
