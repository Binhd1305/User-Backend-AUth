package com.example.demo.Mycoffe.config;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

//This is Validation Process

// HTTP request has to go through this filter first to check JWT token


@Component
@RequiredArgsConstructor
// JWT token stands for Jason web token,  for securely transmitting information, known as "claims," between two parties.
// 3 part: Header Payload Verify Signature
//Header: type load, algorithm
// Payload: contain claims, which are statements of fact about a user or entity, formatted as a JSON object
// 3 Type of claims: register, public and private
//register: predefine claims which are not mandatory but not recommended
// Public claim:  an assertion about a subject that is intended to be publicly visible and can be registered with the
// Internet Assigned Numbers Authority (IANA) or defined with a collision-resistant URI
//Private claims:  custom data field created by an application to share information that is not standardized and is
// only intended for use between specific parties
//Verify Signature:the process of confirming that a JSON Web Token (JWT) has not
// been tampered with and originates from a trusted source
public class JwtAuthentificationFilter extends OncePerRequestFilter {

    private final jwtService jwtService;
    private UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(
           @NonNull HttpServletRequest request,
           @NonNull HttpServletResponse response, // cant intercept
           @NonNull  FilterChain filterChain)
            throws ServletException, IOException {
            final String authHeader = request.getHeader("Authorization");//when pass JWT token within the header
            final String jwtToken;
            final String userName;
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            jwtToken = authHeader.substring(7); // count the word Bearer, the count is 7
            userName = jwtService.extractUsername(jwtToken); //extract the userEmail from JWT Token
            //make sure user is not connected yet
            if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
                if (jwtService.isTokenValid(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                            null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken); //update security context
                    filterChain.doFilter(request, response);

                }

            }
        }
    }

