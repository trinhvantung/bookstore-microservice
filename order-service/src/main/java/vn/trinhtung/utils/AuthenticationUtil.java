package vn.trinhtung.utils;

import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

public class AuthenticationUtil {

	@SuppressWarnings("unchecked")
	public static final Map<String, Object> getUser() {
		if (SecurityContextHolder.getContext().getAuthentication() != null) {
			OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder
					.getContext().getAuthentication();
			Map<String, Object> details = (Map<String, Object>) authentication
					.getUserAuthentication().getDetails();

			Map<String, Object> user = (Map<String, Object>) ((Map<String, Object>) details
					.get("principal")).get("user");
			return user;
		}
		return null;
	}
}
