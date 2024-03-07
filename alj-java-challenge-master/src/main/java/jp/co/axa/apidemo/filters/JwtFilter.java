package jp.co.axa.apidemo.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jp.co.axa.apidemo.services.CustomUserDetailsService;
import jp.co.axa.apidemo.util.JWTUtility;

@Component
public class JwtFilter extends OncePerRequestFilter{
	
	@Autowired
	private JWTUtility jwtUtility;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		 final String authorizationHeader = request.getHeader("Authorization");

		    String username = null;
		    String jwtToken = null;

		    if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
		        jwtToken = authorizationHeader.substring(7);
		        username = jwtUtility.getUsernameFromToken(jwtToken);
		    }

		    if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
		        UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);
		        if(jwtUtility.validateToken(jwtToken, userDetails)){
		            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
		                    userDetails, null, userDetails.getAuthorities()
		            );
		            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		        }
		    }
		    filterChain.doFilter(request, response);
	}

}
