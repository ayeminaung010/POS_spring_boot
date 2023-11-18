package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
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
				.requestMatchers("/admin/home").permitAll()
				.requestMatchers("/user/**").permitAll()
				.requestMatchers("/admin/**").permitAll()
				.requestMatchers("/login").permitAll()
				.requestMatchers("/signup").permitAll()
				.anyRequest().authenticated());
			
		return http.build();
	}
	
	public void configure(WebSecurity web)  throws Exception {
		web.ignoring().requestMatchers("/images/**","/js/**");
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

//Image Save
//String product_image_path=null;
//if(multipartFile !=null && !multipartFile.isEmpty()){
//
//    //image type hoke lar ma hoke lar ko server backend mar htat sit tar
//    if (!multipartFile.getContentType().startsWith("image/")) {
//        // Invalid file type
//        return "redirect:/product/productCreate"; // Return a view to display an error message
//    }
//    product_image_path = saveImage(multipartFile,request);
//
//}

