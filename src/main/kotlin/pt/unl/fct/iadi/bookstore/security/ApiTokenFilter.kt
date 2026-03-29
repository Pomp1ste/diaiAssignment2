package pt.unl.fct.iadi.bookstore.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class ApiTokenFilter(private val registry: ApiTokenRegistry) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val token = request.getHeader("X-Api-Token")

        if (!token.isNullOrBlank() && SecurityContextHolder.getContext().authentication == null) {
            val appName = registry.tokenToApp[token]

            if (appName == null) {
                response.status = HttpServletResponse.SC_UNAUTHORIZED
                response.setHeader(HttpHeaders.WWW_AUTHENTICATE, "ApiToken")
                return
            }

            val auth = UsernamePasswordAuthenticationToken(
                appName,
                null,
                listOf(SimpleGrantedAuthority("ROLE_EDITOR")),
            )
            SecurityContextHolder.getContext().authentication = auth
        }

        filterChain.doFilter(request, response)
    }
}