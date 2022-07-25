package vn.trinhtung.dto;

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
public class CartItemDto {
	private Integer id;
	private Integer quantity;
	private Integer bookId;
	
	@JsonInclude(value = Include.NON_NULL)
	private BookDto book;
}
