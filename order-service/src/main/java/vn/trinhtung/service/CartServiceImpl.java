package vn.trinhtung.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.trinhtung.dto.BookDto;
import vn.trinhtung.dto.CartItemDto;
import vn.trinhtung.entity.CartItem;
import vn.trinhtung.exception.ResourceNotFoundException;
import vn.trinhtung.feign.BookFeignClient;
import vn.trinhtung.repository.CartRepository;
import vn.trinhtung.utils.AuthenticationUtil;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
	private final CartRepository cartRepository;
	private final BookFeignClient bookFeignClient;
	private final ModelMapper modelMapper;
	private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;

	@Override
	public CartItemDto add(CartItemDto cartItemDto) {
		Integer userId = (Integer) AuthenticationUtil.getUser().get("id");

		Optional<CartItem> optional = cartRepository.findByUserIdAndBookId(userId,
				cartItemDto.getBookId());

		CartItem cartItem = null;

		if (optional.isPresent()) {
			cartItem = optional.get();
			cartItem.setQuantity(cartItem.getQuantity() + cartItemDto.getQuantity());
		} else {
			cartItem = CartItem.builder().bookId(cartItemDto.getBookId())
					.quantity(cartItemDto.getQuantity()).userId(userId).build();
		}

		CartItem saved = cartRepository.save(cartItem);
		return CartItemDto.builder().id(saved.getId()).bookId(saved.getBookId())
				.quantity(saved.getQuantity()).build();
	}

	@Transactional
	@Override
	public void delete(Integer id) {
		Integer userId = (Integer) AuthenticationUtil.getUser().get("id");
		cartRepository.deleteByIdAndUserId(id, userId);
	}

	@Override
	public List<CartItemDto> getAllByUser() {
		Integer userId = (Integer) AuthenticationUtil.getUser().get("id");
		List<Integer> bookIds = new ArrayList<>();

		List<CartItemDto> result = cartRepository.findAllByUserId(userId).stream().map(cart -> {
			bookIds.add(cart.getBookId());

			return CartItemDto.builder().id(cart.getId()).quantity(cart.getQuantity())
					.bookId(cart.getBookId()).build();
		}).collect(Collectors.toList());

//		List<BookDto> books = bookFeignClient.getAllByIds(bookIds);
		CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");

		List<BookDto> books = circuitBreaker.run(() -> bookFeignClient.getAllByIds(bookIds),
				throwable -> Arrays.asList());

		result.forEach(cart -> {
			cart.setBook(books.stream().filter(book -> book.getId() == cart.getBookId()).findFirst()
					.orElse(null));
		});

		return result;
	}

	@Override
	public CartItemDto update(Integer id, Integer quantity) {
		Integer userId = (Integer) AuthenticationUtil.getUser().get("id");
		quantity = quantity < 1 ? 1 : quantity;
		CartItem cartItem = cartRepository.findByIdAndUserId(id, userId)
				.orElseThrow(() -> new ResourceNotFoundException("Mặt hàng không có trong giỏ"));
		cartItem.setQuantity(quantity);

		return modelMapper.map(cartRepository.save(cartItem), CartItemDto.class);
	}

}
