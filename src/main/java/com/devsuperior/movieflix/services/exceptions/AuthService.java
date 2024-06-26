package com.devsuperior.movieflix.services.exceptions;

import com.devsuperior.movieflix.dto.UserDTO;
import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository repository) {
        this.userRepository = repository;
    }

    @Transactional(readOnly = true)
    public User authenticated() {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            return userRepository.findByEmail(username);
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid user");
        }
    }

    public void validateMemberOrVisitor(Long userId) {
        User user = authenticated();
        if(user.hasHole("ROLE_VISITOR")) {
            throw new ForbiddenException("Access denied");
        }

        if (!user.getId().equals(userId) && !user.hasHole("ROLE_MEMBER")) {
            throw new ForbiddenException("Access denied");
        }
    }

    public User findLoggedUser() {
        return authenticated();
    }
}
