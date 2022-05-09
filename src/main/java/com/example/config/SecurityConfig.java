package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	// 1. 직접 만든 detailService 객체 가져오기
	@Autowired
	UserDetailsServiceImpl detailsService;

	// 회원가입시 암호화 했던 방법의 객체생성
	// 2. 암호화 방법 객체 생성, @Bean은 서버 구동시 자동으로 객체 생성됨
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// 3. 직접만든 detailsService에 암호화 방법 적용
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(detailsService)
				.passwordEncoder(bCryptPasswordEncoder());
	}

	// 기존기능을 제거한 후 필요한 것을 추가
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// 부모에서 오는 메소드를 호출(부모의 기능을 전부 사용)
		// super.configure(http);

		// 페이지별 접근 권한 설정
		http.authorizeRequests()
				.antMatchers("/admin", "/admin/**")
				.hasAuthority("ADMIN")
				// .antMatchers("/seller","/seller/**")
				// .hasAnyAuthority("ADMIN", "SELLER")
				// .antMatchers("/member", "/member/**")
				// .hasAnyAuthority("MEMBER", "ADMIN")
				.anyRequest().permitAll();

		// 로그인 페이지 설정, 단 POST는 직접 만들지 않음
		http.formLogin()
				.loginPage("/member/login")
				.loginProcessingUrl("/member/loginaction")
				.usernameParameter("memail")
				.passwordParameter("mpw")
				.defaultSuccessUrl("/home")
				.permitAll();

		// 로그아웃 페이지 설정, url에 맞게 POST로 호출하면 됨.
		http.logout()
				.logoutUrl("/member/logout")
				.logoutSuccessUrl("/home")
				// .logoutSuccessHandler(new MyLogoutSuccessHandler())
				.invalidateHttpSession(true)
				.clearAuthentication(true)
				.permitAll();

		// 접근권한불가 403
		http.exceptionHandling().accessDeniedPage("/page403");

		// h2-console을 사용하기 위해서
		http.csrf().ignoringAntMatchers("/h2-console/**");
		http.headers().frameOptions().sameOrigin();

		// rest controller 사용
		http.csrf().ignoringAntMatchers("/api/**");
	}

}
