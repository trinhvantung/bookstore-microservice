package vn.trinhtung.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDto {
	private Integer id;

	@NotNull(message = "Tên sách không được để trống")
	private String name;

	@NotNull(message = "Sách không được để trống")
	private String slug;
	private String thumbnail;

	@NotNull(message = "Giá không được để trống")
	private Integer price;
	private Integer promotionPrice;

	@NotNull(message = "Số lượng không được để trống")
	private Integer quantity;
	private String description;
	private Integer totalPage;
	private String publisher;
	private Float weight;
	private Float length;
	private Float width;
	private Float height;
	private Boolean enable;
	private String publishingYear;

	@NotEmpty(message = "Tác giả không được để trống")
	private List<AuthorDto> authors;

	@JsonIgnore
	@NotNull(message = "Danh mục không được để trống")
	private CategoryDto category;
}
