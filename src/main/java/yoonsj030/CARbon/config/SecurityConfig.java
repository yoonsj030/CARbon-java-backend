package yoonsj030.CARbon.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import yoonsj030.CARbon.security.CustomUsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable();          // CSFR 공격에 대한 방어 해제

        httpSecurity
                .authorizeRequests()          // 요청에 대한 접근 제어
                .antMatchers("/api/**").authenticated()          // 인증된 사용자만 접근 가능
                .antMatchers("/", "**").permitAll()
                .anyRequest().permitAll();          // 나머지 모든 요청에 대해서는 접근 허용

        /**
         * 로그인과 로그아웃 프로세스 구성
         *
         * '/login' URL에서 로그인 폼을 제공 & 로그인 실패 시 리다이렉트 되는 URL
         * '/api/users/login' URL로 로그인 폼 전송 -> UsernamePasswordAuthenticationFilter 거쳐 로그인
         *                                     -> UserServiceImpl의 loadUserByUsername() 자동 실행
         * '/' 로그인 성공 시 리다이렉트 되는 URL
         */
        httpSecurity
//                .formLogin()
//                .loginPage("/login")
//                .usernameParameter("realId")
//                .passwordParameter("password")
//                .loginProcessingUrl("/loginProc")
//                .failureUrl("/login")
//                .defaultSuccessUrl("/")
//                .and()
//                .logout()
//                .logoutUrl("/logout")
//                .logoutSuccessUrl("/home");
            .formLogin().disable();

        httpSecurity.addFilterAt(getAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    protected CustomUsernamePasswordAuthenticationFilter getAuthenticationFilter() {
        CustomUsernamePasswordAuthenticationFilter authFilter = new CustomUsernamePasswordAuthenticationFilter();

        try{
            authFilter.setFilterProcessesUrl("/loginProc");
            authFilter.setUsernameParameter("username");
            authFilter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
            authFilter.setPasswordParameter("password");
        } catch (Exception e){
            e.printStackTrace();
        }

        return authFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        //configuration.addAllowedOrigin("http://54.180.119.61:3000");
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
