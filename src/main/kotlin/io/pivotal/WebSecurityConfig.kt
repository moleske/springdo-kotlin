package io.pivotal

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.csrf.CsrfFilter
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.security.web.csrf.CsrfTokenRepository
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.WebUtils
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Configuration
@EnableWebSecurity
open class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/fonts/**").permitAll()
                .antMatchers("/who/").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/credentials.html").permitAll()
                .and()
                .logout().permitAll()
                .and()
                .csrf()
                .csrfTokenRepository(csrfTokenRepository())
                .and()
                .addFilterAfter(csrfHeaderFilter(), CsrfFilter::class.java)
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(myUserManager())
    }

    @Bean
    open fun myUserManager(): UserManager {
        return UserManager()
    }

    private fun csrfHeaderFilter(): Filter {
        return object : OncePerRequestFilter() {

            override fun doFilterInternal(httpServletRequest: HttpServletRequest, httpServletResponse: HttpServletResponse, filterChain: FilterChain) {
                val csrfToken: CsrfToken? = httpServletRequest.getAttribute(CsrfToken::class.java.name) as CsrfToken
                var cookie: Cookie? = WebUtils.getCookie(httpServletRequest, "XSRF-TOKEN")
                val token = csrfToken?.token
                if (token != cookie?.value) {
                    cookie = Cookie("XSRF-TOKEN", token)
                    cookie.path = "/"  //probably don't want that normally
                    httpServletResponse.addCookie(cookie)
                }
                filterChain.doFilter(httpServletRequest, httpServletResponse)
            }
        }
    }

    private fun csrfTokenRepository(): CsrfTokenRepository {
        val repository = HttpSessionCsrfTokenRepository()
        repository.setHeaderName("X-XSRF-TOKEN")
        return repository
    }
}