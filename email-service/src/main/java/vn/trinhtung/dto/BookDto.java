package vn.trinhtung.dto;

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
	private String name;
	private String slug;
	private String thumbnail;
	private Integer price;
	private Integer promotionPrice;
}
