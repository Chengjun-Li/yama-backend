package team.yama.yama.controller;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super("User: " + username + " not found.");
    }
}
