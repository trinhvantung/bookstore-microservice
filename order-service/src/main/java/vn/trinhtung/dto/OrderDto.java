package vn.trinhtung.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;

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
	
	@NotEmpty(message = "Họ tên không được để trống")
	private String fullname;

	@NotEmpty(message = "Số điện thoại không được để trống")
	private String phone;

	@NotEmpty(message = "Địa chỉ không được để trống")
	private String address;

	private String note;

	private Long totalPrice;
	
	private String email;
	
	private List<OrderItemDto> orderItems;

	private List<OrderTrackDto> orderTracks;

	private Date createdDate;
}
