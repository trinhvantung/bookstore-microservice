package vn.trinhtung.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import vn.trinhtung.dto.BookDto;
import vn.trinhtung.entity.Author;
import vn.trinhtung.entity.Book;
import vn.trinhtung.entity.Category;
import vn.trinhtung.exception.ResourceNotFoundException;
import vn.trinhtung.repository.BookRepository;
import vn.trinhtung.repository.CategoryRepository;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
	private final BookRepository bookRepository;
	private final CategoryRepository categoryRepository;
	private final ModelMapper modelMapper;
	private final CloudinaryService cloudinaryService;

	@Override
	public BookDto getBySlugAndEnable(String slug) {
		Book book = bookRepository.findBySlugAndEnableTrue(slug)
				.orElseThrow(() -> new ResourceNotFoundException("Sách không tồn tại"));

		return modelMapper.map(book, BookDto.class);
	}

	@Override
	public BookDto getBySlug(String slug) {
		Book book = bookRepository.findBySlug(slug)
				.orElseThrow(() -> new ResourceNotFoundException("Sách không tồn tại"));

		return modelMapper.map(book, BookDto.class);
	}

	@Override
	public Page<BookDto> getAll(Integer page) {
		Sort sort = Sort.by("id").descending();
		Pageable pageable = PageRequest.of(page - 1, 10, sort);

		return bookRepository.findAll(pageable).map(book -> modelMapper.map(book, BookDto.class));
	}

	@Override
	public Page<BookDto> getAllEnableByCategory(String categorySlug, Integer page) {
		Sort sort = Sort.by("id").descending();
		Pageable pageable = PageRequest.of(page - 1, 10, sort);

		return bookRepository.findAllByCategorySlugAndEnableTrue(categorySlug, pageable)
				.map(book -> modelMapper.map(book, BookDto.class));
	}

	@Override
	public Page<BookDto> getAllEnable(Integer page) {
		Sort sort = Sort.by("id").descending();
		Pageable pageable = PageRequest.of(page - 1, 10, sort);

		return bookRepository.findAllByEnableTrue(pageable)
				.map(book -> modelMapper.map(book, BookDto.class));
	}

	@Transactional
	@Override
	public BookDto save(BookDto bookDto, MultipartFile image) {
		Optional<Category> savedCategory = categoryRepository
				.findById(bookDto.getCategory().getId());
		if (savedCategory.isEmpty()) {
			throw new ResourceNotFoundException("Danh mục không tồn tại");
		}

		if (bookDto.getId() == null) {
			Book book = modelMapper.map(bookDto, Book.class);
			book.setThumbnail(cloudinaryService.upload("book", image));

			return modelMapper.map(bookRepository.save(book), BookDto.class);
		} else {
			Book book = bookRepository.findById(bookDto.getId())
					.orElseThrow(() -> new ResourceNotFoundException("Sách không tồn tại"));

			book.setName(bookDto.getName());
			book.setDescription(bookDto.getDescription());
			book.setEnable(bookDto.getEnable());
			book.setHeight(bookDto.getHeight());
			book.setLength(bookDto.getLength());
			book.setWeight(bookDto.getWeight());
			book.setWidth(bookDto.getWidth());
			book.setPrice(bookDto.getPrice());
			book.setPromotionPrice(bookDto.getPromotionPrice());
			book.setPublisher(bookDto.getPublisher());
			book.setPublishingYear(bookDto.getPublishingYear());
			book.setTotalPage(bookDto.getTotalPage());
			book.setAuthors(bookDto.getAuthors().stream()
					.map(author -> modelMapper.map(author, Author.class))
					.collect(Collectors.toList()));
			book.setCategory(modelMapper.map(bookDto.getCategory(), Category.class));

			if (image != null && !image.getOriginalFilename().isBlank()) {
				book.setThumbnail(cloudinaryService.upload("book", image));
			}

			return modelMapper.map(bookRepository.save(book), BookDto.class);
		}
	}

	@Transactional
	@Override
	public void delete(Integer id) {
		try {
			bookRepository.deleteById(id);
		} catch (Exception e) {
			throw new ResourceNotFoundException("Sách không tồn tại");
		}
	}

	@Override
	public List<BookDto> getAllByIds(List<Integer> ids) {
		return bookRepository.findAllById(ids).stream()
				.map(book -> BookDto.builder().id(book.getId()).name(book.getName())
						.price(book.getPrice()).promotionPrice(book.getPromotionPrice())
						.thumbnail(book.getThumbnail()).slug(book.getSlug()).build())
				.collect(Collectors.toList());
	}

}
