package com.castores.app.security;

import com.castores.app.security.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFiltro extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        System.out.println("Filtro JWT interceptó: " + request.getRequestURI());

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwtToken = authHeader.substring(7);

            try {
                String correo = jwtUtil.validarToken(jwtToken);
                System.out.println("Token válido para: " + correo);

                // ⬇️ Establecer el usuario autenticado en el contexto de Spring Security
                UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                        correo, null,
                        List.of(new SimpleGrantedAuthority("USER")) // Puedes cambiar el rol según tu lógica
                    );

                SecurityContextHolder.getContext().setAuthentication(auth);
                request.setAttribute("correo", correo);

            } catch (ExpiredJwtException e) {
                System.out.println("Token expirado");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token expirado");
                return;
            } catch (Exception e) {
                System.out.println("Token inválido: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token inválido");
                return;
            }

        } else {
            String path = request.getRequestURI();
            if (!path.equals("/api/login")) {
                System.out.println("⛔ Solicitud sin token para ruta protegida: " + path);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Falta token de autorización");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
