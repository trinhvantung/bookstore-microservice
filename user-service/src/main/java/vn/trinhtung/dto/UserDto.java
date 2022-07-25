package vn.trinhtung.dto;

import java.util.Set;

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
public class UserDto {
	private Integer id;
	private String email;
	private String fullname;
	private boolean enable;
	private boolean nonLocked;

	@JsonInclude(value = Include.NON_EMPTY)
	private String password;
	private Set<RoleDto> roles;
}
