package vn.trinhtung.dto;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.trinhtung.entity.OrderStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderTrackDto {
	private Integer id;

	@NotEmpty
	private OrderStatus status;

	private Date createdDate;
	
	@NotNull
	@JsonInclude(value = Include.NON_NULL)
	private Integer orderId;
}
