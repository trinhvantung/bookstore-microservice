package vn.trinhtung;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import vn.trinhtung.dto.BookDto;
import vn.trinhtung.dto.OrderDto;
import vn.trinhtung.dto.OrderItemDto;
import vn.trinhtung.service.EmailService;

@Controller
public class EmailController {
	@Autowired
	private EmailService emailService;
	
	@GetMapping
	public String email(Model model) {
		BookDto book1 = BookDto.builder().name("Sách Vật Lý 12").build();
		BookDto book2 = BookDto.builder().name("Sách Toán 12").build();
		
		List<OrderItemDto> orderItemDtos = new ArrayList<>();
		orderItemDtos.add(OrderItemDto.builder().book(book1).price(12000).quantity(5).build());
		orderItemDtos.add(OrderItemDto.builder().book(book2).price(17000).quantity(2).build());
		
		OrderDto orderDto = OrderDto.builder()
				.address("Hưng Yên")
				.fullname("Trịnh Văn Tùng")
				.phone("123456789")
				.orderItems(orderItemDtos)
				.createdDate(new Date())
				.totalPrice(135000l)
				.email("tungvlhy@gmail.com")
				.build();
		
		model.addAttribute("order", orderDto);
		
		emailService.sendEmailOrder(orderDto);
		return "order";
	}
}
