package com.example.Mission_shop.exception;

import com.example.Mission_shop.entity.CustomUserDetails;
import com.example.Mission_shop.entity.UserEntity;
import com.example.Mission_shop.service.JpaUserDetailsManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {
    public Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public UserEntity extractUser() {
        CustomUserDetails userDetails
                = (CustomUserDetails) getAuth().getPrincipal();
        return userDetails.getEntity();
    }
}
