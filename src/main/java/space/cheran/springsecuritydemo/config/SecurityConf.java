package space.cheran.springsecuritydemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import space.cheran.springsecuritydemo.model.Permissions;
import space.cheran.springsecuritydemo.model.Role;

@Configuration
@EnableMethodSecurity
public class SecurityConf {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf((csrf) -> csrf
                        .ignoringRequestMatchers("/api/**"))
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers("/*").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/**").hasAuthority(Permissions.DEVELOPERS_READ.getPermission())
                        .requestMatchers(HttpMethod.POST, "/api/v1/developers").hasAuthority(Permissions.DEVELOPERS_WRITE.getPermission())
                        .requestMatchers(HttpMethod.DELETE, "/api/**").hasAnyAuthority(Permissions.DEVELOPERS_WRITE.getPermission())
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())
                .logout(Customizer.withDefaults())
               .build();
    }

    @Bean
    protected UserDetailsService userDetailsService(){
        return new InMemoryUserDetailsManager(
                User.builder()
                        .username("admin")
                        .password(passwordEncoder().encode("admin"))
                        .authorities(Role.ADMIN.getAuthorities())
                        .build(),
                User.builder()
                        .username("user")
                        .password(passwordEncoder().encode("user"))
                        .authorities(Role.USER.getAuthorities())
                        .build()
        );
    }
    @Bean
    protected PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }
}
