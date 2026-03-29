package pt.unl.fct.iadi.bookstore.security

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.annotations.security.SecuritySchemes
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@SecuritySchemes(
    SecurityScheme(
        name = "basicAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "basic",
        ),
        SecurityScheme(
            name = "apiToken",
            type = SecuritySchemeType.APIKEY,
            `in` = SecuritySchemeIn.HEADER,
            paramName = "X-Api-Token",
        ),
    )
@EnableMethodSecurity
class SecurityConfig(private val apiTokenFilter: ApiTokenFilter) {
    @Bean
    fun passwordEncoder(): PasswordEncoder =
        BCryptPasswordEncoder()

    @Bean
    fun userDetailsService(encoder: PasswordEncoder) = InMemoryUserDetailsManager(
        User.withUsername("editor1")
            .password(encoder.encode("editor1pass"))
            .roles("EDITOR")
            .build(),
        User.withUsername("admin")
            .password(encoder.encode("adminpass"))
            .roles("ADMIN")
            .build(),
        User.withUsername("editor2")
            .password(encoder.encode("editor2pass"))
            .roles("EDITOR")
            .build(),
    )

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf{ it.disable()}
            .addFilterBefore(apiTokenFilter,
                UsernamePasswordAuthenticationFilter::class.java)
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(HttpMethod.GET, "/books", "/books/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/books", "/books/*/reviews").authenticated()
                    .requestMatchers(HttpMethod.PUT, "/books/*", "/books/*/reviews/*").authenticated()
                    .requestMatchers(HttpMethod.PATCH, "/books/*", "/books/*/reviews/*").authenticated()
                    .requestMatchers(HttpMethod.DELETE, "/books/*", "/books/*/reviews/*").authenticated()
                    .anyRequest().authenticated()
            }
            .httpBasic {}
        return http.build()
    }
}