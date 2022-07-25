package vn.trinhtung.service;

import java.util.List;

import vn.trinhtung.dto.CartItemDto;

public interface CartService {
	CartItemDto add(CartItemDto cartItemDto);
	
	void delete(Integer id);
	
	List<CartItemDto> getAllByUser();
	
	CartItemDto update(Integer id, Integer quantity);
}
