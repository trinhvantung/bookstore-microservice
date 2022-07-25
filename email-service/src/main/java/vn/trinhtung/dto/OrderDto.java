package vn.trinhtung.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
public class OrderDto {
	private Integer id;
	
	private String fullname;

	private String phone;

	private String address;

	private String note;
	
	@JsonInclude(value = Include.NON_EMPTY)
	private String email;

	private Long totalPrice;
	
	private Integer userId;
	
	private List<OrderItemDto> orderItems;

	private Date createdDate;
	
	public String getCreatedDate() {
		return new SimpleDateFormat("hh:mm:ss dd-MM-yyyy").format(createdDate);
	}
}
