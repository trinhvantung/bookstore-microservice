package vn.trinhtung.config;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import vn.trinhtung.utils.AuthenticationUtil;

@Component
public class AuditorAwareConfig implements AuditorAware<Integer> {

	@Override
	public Optional<Integer> getCurrentAuditor() {
		if (SecurityContextHolder.getContext().getAuthentication() != null) {
			

			Map<String, Object> user = AuthenticationUtil.getUser();
			
			return Optional.of((Integer) user.get("id"));
		}
		return Optional.empty();
	}

}
