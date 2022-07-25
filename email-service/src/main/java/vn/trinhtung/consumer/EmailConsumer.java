package vn.trinhtung.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import vn.trinhtung.dto.OrderDto;
import vn.trinhtung.service.EmailService;

@Component
public class EmailConsumer {
	@Autowired
	private EmailService emailService;

	@KafkaListener(
			topics = "order-topic", containerFactory = "orderKafkaListenerContainerFactory", groupId = "email_group"
	)
	public void sendEmailOrder(OrderDto orderDto) {
		emailService.sendEmailOrder(orderDto);
	}
}
