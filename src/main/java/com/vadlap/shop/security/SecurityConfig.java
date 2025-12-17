package com.vadlap.shop.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth
                        // Разрешаем доступ только для ADMIN или EDITOR для создания новости
                        .requestMatchers("/api/v1/news/createNews").hasAnyRole("ADMIN", "EDITOR")

                        // Эндпоинт регистрации/логина должен быть разрешен всем (permitAll)
                        // Если регистрация в микросервисе, то здесь нужно разрешить только логин,
                        // если он обрабатывается этим сервисом.
                        // Пример: .requestMatchers("/api/v1/auth/**").permitAll()

                        // Все остальные запросы к новостям требуют аутентификации (ADMIN, EDITOR, USER)
                        .requestMatchers("/api/v1/news/**").authenticated()

                        // Любые другие запросы требуют аутентификации (если не разрешены явно)
                        .anyRequest().authenticated()
                )

                //Тут нужно будет настроить JWT-токен
                .httpBasic(httpBasic -> {})

                // 4. Настройка сессий: делаем stateless (без сохранения состояния сессии)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCryptPasswordEncoder является стандартным и безопасным выбором
        return new BCryptPasswordEncoder();
    }

    // ВАЖНО: Вам также потребуется настроить AuthenticationManager и UserDetailsService,
    // чтобы Spring Security знал, как загружать пользователя по имени и проверять пароль.
}