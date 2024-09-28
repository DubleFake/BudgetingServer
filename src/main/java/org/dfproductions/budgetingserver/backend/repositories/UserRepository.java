package org.dfproductions.budgetingserver.backend.repositories;

import org.dfproductions.budgetingserver.backend.templates.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}


