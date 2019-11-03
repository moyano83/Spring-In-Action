package com.moyajor.springinaction.data;

import com.moyajor.springinaction.model.db.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username) throws UsernameNotFoundException;
}
