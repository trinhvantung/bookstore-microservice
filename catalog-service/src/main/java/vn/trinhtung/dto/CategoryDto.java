package vn.trinhtung.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {
	private Integer id;

	@JsonInclude(value = Include.NON_EMPTY)
	@NotEmpty(message = "Tên danh mục không được để trống")
	private String name;

	@JsonInclude(value = Include.NON_EMPTY)
	@NotEmpty(message = "Danh mục không được để trống")
	private String slug;

//	@JsonIgnore
	@JsonInclude(value = Include.NON_EMPTY)
	private List<CategoryDto> children;


	@JsonInclude(value = Include.NON_NULL)
	private CategoryDto parent;
}
