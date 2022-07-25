package vn.trinhtung.dto;

import java.util.Map;

import org.hibernate.validator.constraints.Length;

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
public class ReviewDto {
	@JsonInclude(value = Include.NON_EMPTY)
	private Integer id;

	@JsonInclude(value = Include.NON_EMPTY)
	private String content;

	@JsonInclude(value = Include.NON_EMPTY)
	@Length(min = 1, max = 4)
	private Byte star;

	@JsonInclude(value = Include.NON_EMPTY)
	private Integer bookId;

	@JsonInclude(value = Include.NON_NULL)
	private UserDto user;

	@JsonInclude(value = Include.NON_NULL)
	private Map<Byte, Long> details;
}
