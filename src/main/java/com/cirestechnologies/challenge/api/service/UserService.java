package com.cirestechnologies.challenge.api.service;

import com.cirestechnologies.challenge.api.model.User;
import com.cirestechnologies.challenge.api.payload.response.BatchResponse;
import com.cirestechnologies.challenge.api.repository.UserRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    /**
     *
     * @param username, string username
     * @return user or throw not found exception
     */
    public User getProfile(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.orElse(null);
    }

    /**
     * build user from faker service
     *
     * @param faker, Faker service
     * @return User
     */
    public User buildUser(Faker faker) {
        User user = new User();
        user.setFirstName(faker.name().firstName());
        user.setLastName(faker.name().lastName());
        user.setBirthDate(faker.date().birthday().toString());
        user.setCity(faker.address().city());
        user.setCountry(faker.address().countryCode());
        user.setAvatar(faker.avatar().image());
        user.setCompany(faker.company().name());
        user.setJobPosition(faker.job().position());
        user.setMobile(faker.phoneNumber().cellPhone());
        user.setUsername(faker.name().username());
        user.setEmail(faker.internet().emailAddress());
        user.setPassword(faker.internet().password(6, 10));
        user.setRole(faker.bool().bool() ? "admin" : "user");

        return user;
    }

    /**
     * exportUsers
     * @param users, users list
     * @return json list of users
     */
    public String exportUsers(List<User> users) {
        Gson gson = new Gson();
        return gson.toJson(users);
    }

    /**
     * storeUsersFromJson
     * @return Json response
     */
    public ResponseEntity<?> storeUsersFromJson(MultipartFile multipartFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<User>> mapType = new TypeReference<List<User>>() {};
        InputStream is = multipartFile.getInputStream();
        try {
            List<User> usersList = mapper.readValue(is, mapType);
            return saveUsersList(usersList);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * saveUsersList
     * @param usersList, users list
     * @return status
     */
    private ResponseEntity<?> saveUsersList(List<User> usersList) {
        final AtomicInteger NbInserted = new AtomicInteger( 0 ) ;
        final AtomicInteger NbRejected = new AtomicInteger( 0 ) ;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        usersList.forEach((user) -> {
            if (userRepository.existsByUsername(user.getUsername()) || userRepository.existsByEmail(user.getEmail())) {
                NbRejected.incrementAndGet();
            } else {
                user.setPassword(encoder.encode(user.getPassword()));
                userRepository.save(user);
                NbInserted.incrementAndGet();
            }
        });

       return ResponseEntity.ok(new BatchResponse(usersList.size(), NbInserted.get(), NbRejected.get()));
    }
}
