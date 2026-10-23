package com.project.webpos.Security;

import java.io.IOException;

import org.slf4j.MDC;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.project.webpos.Service.Security.JWTService;
import com.project.webpos.Service.Security.UserDetailService;

import com.project.webpos.Entity.UserPrincipal;
import com.project.webpos.Logging.LoggerFormat;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component 
@RequiredArgsConstructor 
@Slf4j
// =================================================================================================================
// Extends to a class with filter capability. Extended with OncePerRequestFilter to check token per HTTP request.
// =================================================================================================================
public class JWTCheckerPerRequest extends OncePerRequestFilter{
  public static final String USER_ID = "userId"; 
  public static final String ENDPOINT = "endpoint"; 

  private final JWTService jwtService;
  private final ApplicationContext applicationContext;


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
     String authHeader = request.getHeader("Authorization"); // get the header about Authorization
        String token = null;
        String username = null;

        // Checks if there is an Authorization on the request
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7); // Used to skip the "Bearer " and fetch the JWT
            username = jwtService.extractUsername(token); // Used to extract the username from the JWT
        }

        // Checks if there is a token with it.
        if(username != null){
            UserDetails userDetails = applicationContext.getBean(UserDetailService.class).loadUserByUsername(username); // This code allows you to access Spring managed beans without injecting the object using constructor to avoid circular reference
                                                                                                       // Basically use are just accessing the loadUserByUsername method from the UserService Class
            // check if the token and userDetails is matching create a new authentication object and send it to context.
            if(jwtService.validateToken(token, userDetails)){
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); // This creates the authentication token with the user's identity and role.
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // attaches the IP address to the token.
                SecurityContextHolder.getContext().setAuthentication(authToken); // you are adding the token into the chain. Meaning the user is already authenticated. This sets the user's authentication object(username, roles, and etc.)
                
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                UserPrincipal loggedUser = (UserPrincipal) authentication.getPrincipal();

                MDC.put(USER_ID, loggedUser.getUserId().toString());
                
                if(!request.getMethod().toLowerCase().equals("get")){
                    LoggerFormat loggerFormat = LoggerFormat.builder()
                        .level("INFO")
                        .action("user access endpoint request")
                        .actorId(MDC.get(USER_ID))
                        .endpoint(request.getRequestURI())
                        .build();
    
                    log.atInfo()
                        .setMessage("Accessing endpoint")
                        .addKeyValue("action_info", loggerFormat)
                        .log();
                }
            }
        }
        MDC.put(ENDPOINT, request.getRequestURI());
        filterChain.doFilter(request, response); // this passed the request and response to the next filter chain.
  
        MDC.clear(); // clears the MDC to avoid memory leaks
    }
}
