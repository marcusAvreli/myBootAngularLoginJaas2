package myBootAngularLoginJaas2.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.jaas.AbstractJaasAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


import myBootAngularLoginJaas2.myCustomJaas.DefaultKyloJaasAuthenticationProvider;







//@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(
		// securedEnabled = true,
		// jsr250Enabled = true,
		prePostEnabled = true)
public class SecurityConfig extends BaseWebSecurityConfigurer {
	
	private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
	
    /**
     * Defining these beens in an embedded configuration to ensure they are all constructed
     * before being used by the logout filter.
     */


	@Bean
	public AuthenticationProvider defaultKyloJaasAutehnticationProvider() {
		DefaultKyloJaasAuthenticationProvider provider=new DefaultKyloJaasAuthenticationProvider();
		//provider.setAuthorityGranters(authorityGranters);
		//DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

		//provider.setPasswordEncoder(passwordEncoder);
		//provider.setUserDetailsService(hibernateUserDetailsService);
		return provider;
	}



	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(defaultKyloJaasAutehnticationProvider());
    }
   

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		logger.info("*********SECURITY_CONFIG_LOGGER.INFO************");
		http.cors().and().csrf().disable()
			.exceptionHandling().and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.authorizeRequests().antMatchers( "/api/auth/**").permitAll()
			.antMatchers("/api/test/**").permitAll()
			.antMatchers("/api/rest/**").permitAll()
			.anyRequest().authenticated();
		http.addFilterBefore(jaasFilter(), BasicAuthenticationFilter.class);

		//http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}
	
}
