package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.service.AppUserDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	// Password Encoding
	@Bean
	 PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	UserDetailsService userDetailService() {
		return new AppUserDetailService();
	}


	@Bean
	 DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.formLogin((form) -> form.loginPage("/login").usernameParameter("email").permitAll().defaultSuccessUrl("/home", true));
		http.logout((logout) -> logout.logoutUrl("/logout").permitAll());
		http.authorizeHttpRequests(
				(requests) -> requests.requestMatchers("/","/home").permitAll()
				.requestMatchers("/admin/home","/category/**","/subcategory/**","/brand/**","/product/**","/paymenttype/**","/account/**").hasAuthority("ADMIN")
				.requestMatchers("/user/account/**").hasAuthority("USER")
				.requestMatchers("/shop/**","/contact/**","/cart").permitAll()
				.requestMatchers("/login","/auth/**").permitAll()
				.requestMatchers("/signup").permitAll()
				.requestMatchers("/client/**").permitAll()
				.requestMatchers("/app/**").permitAll()
				.requestMatchers("/uploads/**").permitAll()
				.requestMatchers("/api/**").permitAll()
				.requestMatchers("/admin/css/**","/admin/js/**","/admin/images/**").permitAll()
				.anyRequest().authenticated());
			
		return http.build();
	}
	
	
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

}

//@Bean
//// authentication
//InMemoryUserDetailsManager userDetailsService(PasswordEncoder encoder) {
//	UserDetails user = User.builder().username("user@gmail.com").password(encoder.encode("user")).roles("USER")
//			.build();
//
//	UserDetails admin = User.builder().username("admin@gmail.com").password(encoder.encode("admin")).roles("ADMIN")
//			.build();
//	return new InMemoryUserDetailsManager(admin, user);
//}


