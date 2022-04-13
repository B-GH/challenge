package com.cirestechnologies.challenge.api.repository;

import com.cirestechnologies.challenge.api.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer>
{
    Optional <User> findByUsername(String username);
    Optional <User> findByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
