package vn.trinhtung.service;

import vn.trinhtung.dto.OrderDto;

public interface EmailService {
	void sendEmailOrder(OrderDto orderDto);
}
