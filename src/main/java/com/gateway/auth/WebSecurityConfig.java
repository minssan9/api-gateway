package com.gateway.auth;

import com.gateway.auth.service.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity
@EnableOAuth2Client
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    @Autowired    UserDetailServiceImpl userDetailService;
    @Autowired    PasswordEncoderCustom passwordEncoder;
    @Autowired    AuthServerProvider authServerProvider;
    @Autowired    AuthenticatedServerList authenticatedServerList;

    //    @Bean
//    public PasswordEncoderCustom passwordEncoder() {
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//    }
    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }


    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authServerProvider);
        auth.inMemoryAuthentication().withUser("homs").password("homs").roles("USER", "INV");
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .httpBasic().disable()
            .csrf().disable()
            .anonymous().disable();

//        for (String ip : authenticatedServerList.getIpList()){
//            http
//                .authorizeRequests()
//                .antMatchers("/**").hasIpAddress(ip);
//        }

        http
//            .anonymous().disable()
            .authorizeRequests()
            .antMatchers("/**", "/**").hasRole("USER")
//            .antMatchers("/**", "/**").hasRole("INV")
            .antMatchers("/**").permitAll()
            .antMatchers("/oauth/**","/oauth/token","/oauth2/callback").permitAll()
            .antMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .logout()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")
            .deleteCookies("JSESSIONID")
            .invalidateHttpSession(true)
            .permitAll();

    }

    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry
            .addMapping("/**")
            .allowedHeaders("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE");
//            .allowedOrigins("http://localhost:8081")
//            .allowedMethods("GET", "POST", "PUT", "DELETE")
//            .allowedHeaders("*")
//            .allowCredentials(true);
    }

    @Override
    public void configure(WebSecurity web) {
        // swagger security 적용 무시
        web
            .ignoring()
            .antMatchers("/v2/api-docs",
                "/v3/api-docs",
                "/configuration/ui",
                "/swagger-resources",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**",
                "/swagger-ui/**");
    }
}
