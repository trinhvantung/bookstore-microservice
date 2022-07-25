package vn.trinhtung.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.RoutingKafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.trinhtung.dto.BookDto;
import vn.trinhtung.dto.OrderDto;
import vn.trinhtung.dto.OrderTrackDto;
import vn.trinhtung.entity.CartItem;
import vn.trinhtung.entity.Order;
import vn.trinhtung.entity.OrderItem;
import vn.trinhtung.entity.OrderStatus;
import vn.trinhtung.entity.OrderTrack;
import vn.trinhtung.exception.ResourceNotFoundException;
import vn.trinhtung.feign.BookFeignClient;
import vn.trinhtung.repository.CartRepository;
import vn.trinhtung.repository.OrderRepository;
import vn.trinhtung.repository.OrderTrackRepository;
import vn.trinhtung.utils.AuthenticationUtil;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
	private final OrderRepository orderRepository;
	private final CartRepository cartRepository;
	private final BookFeignClient bookFeignClient;
	private final ModelMapper modelMapper;
	private final OrderTrackRepository orderTrackRepository;
	private final RoutingKafkaTemplate kafkaTemplate;

	@Transactional
	@Override
	public OrderDto create(OrderDto orderDto) {
		Integer userId = (Integer) AuthenticationUtil.getUser().get("id");
		List<CartItem> cartItems = cartRepository.findAllByUserId(userId);
		List<OrderItem> orderItems = new ArrayList<>();
		List<Integer> bookIds = cartItems.stream().map(item -> item.getBookId())
				.collect(Collectors.toList());
		List<BookDto> bookDtos = bookFeignClient.getAllByIds(bookIds);
		Long totalPrice = 0l;
		Order order = new Order();

		if (cartItems.size() == 0) {
			throw new ResourceNotFoundException("Giỏ hàng không có sản phẩm");
		}

		for (CartItem item : cartItems) {
			BookDto book = bookDtos.stream().filter(bookDto -> bookDto.getId() == item.getBookId())
					.findFirst().get();
			Integer price = book.getPromotionPrice() == null
					|| book.getPromotionPrice() >= book.getPrice() ? book.getPrice()
							: book.getPromotionPrice();
			OrderItem orderItem = OrderItem.builder().bookId(item.getBookId())
					.quantity(item.getQuantity()).price(price).order(order).build();

			totalPrice += price * item.getQuantity();

			orderItems.add(orderItem);
		}
		System.out.println("Order Item: " + orderItems.size());

		List<OrderTrack> orderTracks = Collections
				.singletonList(OrderTrack.builder().status(OrderStatus.NEW).order(order).build());

		order.setUserId(userId);
		order.setTotalPrice(totalPrice);
		order.setPhone(orderDto.getPhone());
		order.setAddress(orderDto.getAddress());
		order.setFullname(orderDto.getFullname());
		order.setNote(orderDto.getNote());
		order.setOrderItems(orderItems);
		order.setOrderTracks(orderTracks);

		OrderDto result = modelMapper.map(orderRepository.save(order), OrderDto.class);
		cartRepository.deleteAllByUserId(userId);
		
		result.setEmail((String) AuthenticationUtil.getUser().get("email"));

		result.getOrderItems().forEach(item -> {
			BookDto book = bookDtos.stream().filter(bookDto -> bookDto.getId() == item.getBookId())
					.findFirst().get();
			item.setBook(book);
		});

		kafkaTemplate.send("email-topic", result);
		
		return result;
	}

	@Override
	public Page<OrderDto> getAllByUserId(Integer page) {
		Integer userId = (Integer) AuthenticationUtil.getUser().get("id");
		Sort sort = Sort.by("id").descending();
		Pageable pageable = PageRequest.of(page - 1, 10, sort);

		return orderRepository.findAllByUserId(userId, pageable)
				.map(order -> OrderDto.builder().id(order.getId()).fullname(order.getFullname())
						.address(order.getAddress()).phone(order.getPhone())
						.totalPrice(order.getTotalPrice()).build());
	}

	@Override
	public Page<OrderDto> getAll(Integer page) {
		Sort sort = Sort.by("id").descending();
		Pageable pageable = PageRequest.of(page - 1, 10, sort);

		return orderRepository.findAll(pageable)
				.map(order -> OrderDto.builder().id(order.getId()).fullname(order.getFullname())
						.address(order.getAddress()).phone(order.getPhone())
						.totalPrice(order.getTotalPrice()).build());
	}

	@Override
	public OrderTrackDto updateOrderTrack(OrderTrackDto orderTrackDto) {
		orderRepository.findById(orderTrackDto.getOrderId())
				.orElseThrow(() -> new ResourceNotFoundException("Đơn hàng không tồn tại"));

		OrderTrack saved = orderTrackRepository.save(
				OrderTrack.builder().order(Order.builder().id(orderTrackDto.getOrderId()).build())
						.status(orderTrackDto.getStatus()).build());

		return OrderTrackDto.builder().id(saved.getId()).orderId(saved.getOrder().getId())
				.status(saved.getStatus()).createdDate(saved.getCreatedDate()).build();
	}

	@Override
	public OrderDto getById(Integer id) {
		Order order = orderRepository.findByIdJoinFetchOrderItemsAndOrderTracks(id)
				.orElseThrow(() -> new ResourceNotFoundException("Đơn hàng không tồn tại"));
		List<Integer> bookIds = order.getOrderItems().stream().map(item -> item.getBookId())
				.collect(Collectors.toList());
		List<BookDto> bookDtos = bookFeignClient.getAllByIds(bookIds);

		OrderDto result = modelMapper.map(order, OrderDto.class);

		result.getOrderItems().forEach(item -> {
			BookDto book = bookDtos.stream().filter(bookDto -> bookDto.getId() == item.getBookId())
					.findFirst().get();
			item.setBook(book);
		});

		return result;
	}

	@Override
	public OrderDto getByIdAndUser(Integer id) {
		Integer userId = (Integer) AuthenticationUtil.getUser().get("id");
		Order order = orderRepository.findByIdAndUserId(id, userId)
				.orElseThrow(() -> new ResourceNotFoundException("Đơn hàng không tồn tại"));
		List<Integer> bookIds = order.getOrderItems().stream().map(item -> item.getBookId())
				.collect(Collectors.toList());
		List<BookDto> bookDtos = bookFeignClient.getAllByIds(bookIds);

		OrderDto result = modelMapper.map(order, OrderDto.class);

		result.getOrderItems().forEach(item -> {
			BookDto book = bookDtos.stream().filter(bookDto -> bookDto.getId() == item.getBookId())
					.findFirst().get();
			item.setBook(book);
		});

		return result;
	}

}
