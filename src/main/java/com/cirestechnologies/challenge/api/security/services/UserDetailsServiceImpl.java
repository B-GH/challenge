package com.cirestechnologies.challenge.api.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cirestechnologies.challenge.api.model.User;
import com.cirestechnologies.challenge.api.repository.UserRepository;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> user = userRepository.findByUsername(username);

    if (!user.isPresent()) {
      user = userRepository.findByEmail(username);
    }

    if (!user.isPresent()) {
      throw new UsernameNotFoundException("User Not Found with username / email: " + username);
    }


    return UserDetailsImpl.build(user.get());
  }

}
