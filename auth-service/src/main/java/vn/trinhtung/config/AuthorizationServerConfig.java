package vn.trinhtung.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthenticationManager authenticationManager;

//	@Override
//	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//		// TODO Auto-generated method stub
//		super.configure(security);
//	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory().withClient("client").secret(passwordEncoder.encode("secret")).scopes("client")
				.authorizedGrantTypes("password", "refresh_token").accessTokenValiditySeconds(1000)
				.refreshTokenValiditySeconds(10000).and().withClient("server").secret(passwordEncoder.encode("secret"))
				.scopes("server").authorizedGrantTypes("client_credentials").accessTokenValiditySeconds(1000)
				.refreshTokenValiditySeconds(10000);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.userDetailsService(userDetailsService).authenticationManager(authenticationManager);
	}

}
