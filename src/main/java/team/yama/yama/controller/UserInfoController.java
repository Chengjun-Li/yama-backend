package team.yama.yama.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import team.yama.yama.domain.User;

import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.ResponseEntity.ok;

@RestController()
public class UserInfoController {

    @SuppressWarnings("rawtypes")
    @GetMapping("/user")
    public ResponseEntity currentUser(@AuthenticationPrincipal UserDetails userDetails){
        Map<Object, Object> model = new HashMap<>();
        User user = (User) userDetails;
        model.put("id", user.getId());
        model.put("username", user.getUsername());
        model.put("email", user.getEmail());
        model.put("lastName",user.getLastName());
        model.put("firstName", user.getFirstName());
        model.put("unit", user.getUnit());
        model.put("phoneNumber", user.getPhoneNumber());
        model.put("userType", user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(toList())
        );
        return ok(model);
    }
}