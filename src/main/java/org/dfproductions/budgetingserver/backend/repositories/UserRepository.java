package org.dfproductions.budgetingserver.backend.repositories;

import org.dfproductions.budgetingserver.backend.templates.User;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    @Query("SELECT * FROM users WHERE email = :email")
    User findByEmail(String email);
}


