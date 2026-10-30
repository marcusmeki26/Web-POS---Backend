package com.project.webpos.JWT;

import java.io.IOException;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.project.webpos.Service.Security.UserDetailService;
import com.project.webpos.Service.Shared.CacheManagementService;
import com.project.webpos.Service.Shared.UserBasedRateLimiter;

import io.jsonwebtoken.security.SignatureException;

import com.project.webpos.Entity.UserPrincipal;
import com.project.webpos.Exception.LimitReachedException;
import com.project.webpos.Exception.UnauthorizedAccessException;
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
    private final UserBasedRateLimiter userBasedRateLimiter;
    @Qualifier("handlerExceptionResolver")
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final CacheManagementService cacheManagementService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
        String authHeader = request.getHeader("Authorization"); // get the header about Authorization
        String token = null;
        String username = null;

        // Checks if there is an Authorization on the request
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7); // Used to skip the "Bearer " and fetch the JWT
            
            String oldAccessKeyJti = jwtService.extractJti(token);
            String oldAccessKeyJtiCache = (String) cacheManagementService.getValueOfKey(oldAccessKeyJti);
            if(oldAccessKeyJtiCache != null){
                UnauthorizedAccessException ex = new UnauthorizedAccessException("BAD_REQUEST", "Using of old access key is prohibited");
                handlerExceptionResolver.resolveException(request, response, null, ex);
                return;
            }

            try{
                username = jwtService.extractUsername(token); // Used to extract the username from the JWT
            }catch(SignatureException ex){
                handlerExceptionResolver.resolveException(request, response, null, ex);
                return;
            }
        
            if(username != null){
                UserDetails userDetails = applicationContext.getBean(UserDetailService.class).loadUserByUsername(username); // This code allows you to access Spring managed beans without injecting the object using constructor to avoid circular reference
                                                                                                        // Basically use are just accessing the loadUserByUsername method from the UserService Class
                UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                UserPrincipal loggedUser = (UserPrincipal) authentication.getPrincipal();

                if(loggedUser == null || authentication == null){
                    UnauthorizedAccessException ex = new UnauthorizedAccessException("BAD_REQUEST", "Invalid access.");
                    handlerExceptionResolver.resolveException(request, response, null, ex);
                    return;
                }
                
                try{
                    performTokenChecking(loggedUser, request, response);
                }catch(LimitReachedException ex){
                    handlerExceptionResolver.resolveException(request, response, null, ex);
                    return;
                }
                logUserAction(loggedUser, request);
            }else{
                UnauthorizedAccessException ex = new UnauthorizedAccessException("BAD_REQUEST", "Current user is not validated. Please login again");
                handlerExceptionResolver.resolveException(request, response, null, ex);
                return;
            }
        }
        MDC.put(ENDPOINT, request.getRequestURI());
        filterChain.doFilter(request, response); // this passed the request and response to the next filter chain.

        MDC.clear(); // clears the MDC to avoid memory leaks
    }

    // =======================================================
    // UTILITIES
    // =======================================================
    /**
     * Checks the available token a user can consume.
     * @param loggedUser an object of {@code UserPrincipal} of the currently logged/authenticated user
     * @param request an object of {@code HttpServletRequest} that contains the details of request
     * @param response an object of {@code HttpServletResponse} that contains the details of response 
     */
    private void performTokenChecking(UserPrincipal loggedUser, HttpServletRequest request, HttpServletResponse response) throws LimitReachedException{
        // rate-limit checker
        String userId = loggedUser.getUserId();
        String cacheKey = "rate_limit:user:" + userId;
        userBasedRateLimiter.checkToken(cacheKey, userId);
    }

    /**
     * Logs the action of the user which endpoint it accessed
     * @param loggedUser an object of {@code UserPrincipal} of the currently logged/authenticated user
     * @param request an object of {@code HttpServletRequest} that contains the details of request
     */
    private void logUserAction(UserPrincipal loggedUser, HttpServletRequest request){
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
