package team.yama.yama.controller;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super("User: " + userId + " not found.");
    }
}
