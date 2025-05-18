package com.example.clinic_management.config;

import com.example.clinic_management.security.jwt.JwtUntils;
import com.example.clinic_management.security.user.EShopUserDetailService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtUntils jwtUtils;

    private EShopUserDetailService userDetailsService;

    @Value("${FE.url}")
    private String frontendUrl;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket")
                .setAllowedOrigins(frontendUrl,
                        "http://localhost:5173")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                log.info("Configuring WebSocket channel interceptor");
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
                    List<String> authorization = accessor.getNativeHeader("Authorization");
                    if (authorization != null && !authorization.isEmpty()) {
                        String bearerToken = authorization.get(0);
                        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                            String token = bearerToken.substring(7);

                            try {
                                // Use your existing JwtUtils to validate the token
                                if (jwtUtils.validateToken(token)) {
                                    // Get username from token
                                    String username = jwtUtils.getUsernameFromToken(token);

                                    // Load user details - reusing your existing UserDetailsService
                                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                                    // Create authentication token
                                    UsernamePasswordAuthenticationToken authentication =
                                            new UsernamePasswordAuthenticationToken(
                                                    userDetails, null, userDetails.getAuthorities());

                                    // Set authentication in SecurityContext
                                    SecurityContextHolder.getContext().setAuthentication(authentication);
                                    accessor.setUser(authentication);
                                }
                            } catch (JwtException e) {
                                // Token validation failed, do not authenticate
                                log.error("Invalid JWT token: " + e.getMessage());
                                System.err.println("Invalid JWT token: " + e.getMessage());
                            }
                        }
                    }
                }
                return message;
            }
        });
    }
}
