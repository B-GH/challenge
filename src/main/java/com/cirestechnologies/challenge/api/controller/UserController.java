package com.cirestechnologies.challenge.api.controller;

import com.cirestechnologies.challenge.api.model.User;
import com.cirestechnologies.challenge.api.security.jwt.JwtUtils;
import com.cirestechnologies.challenge.api.service.UserService;
import com.github.javafaker.Faker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/generate")
    public ResponseEntity<byte[]> generateUsers(@RequestParam(name = "count", required = false, defaultValue = "10") int count) {
        List<User> users = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Faker faker = new Faker(new Locale("fr"));
            users.add(userService.buildUser(faker));
        }

        String customerJsonString = userService.exportUsers(users);

        byte[] usersJsonBytes = customerJsonString.getBytes();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=users.json")
                .contentType(MediaType.APPLICATION_JSON)
                .contentLength(usersJsonBytes.length)
                .body(usersJsonBytes);
    }

    @RequestMapping(value = "/batch", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }, method = RequestMethod.POST)
    public ResponseEntity<?> saveUser(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        return userService.storeUsersFromJson(multipartFile);
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping("/me")
    public User getProfile(HttpServletRequest request) throws UsernameNotFoundException {
        User user = null;
        String jwt = jwtUtils.parseJwt(request);
        if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            user = userService.getProfile(username);

            if (user != null) {
                return user;
            }
        }

        throw new UsernameNotFoundException("User Not Found with for given JWT");
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping("/{username}")
    @PreAuthorize("hasAuthority('admin')")
    public User getUser(@PathVariable("username") String username) {
        return userService.getProfile(username);
    }
}
