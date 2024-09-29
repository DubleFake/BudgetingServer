package org.dfproductions.budgetingserver.backend.repositories;

import org.dfproductions.budgetingserver.backend.templates.Password;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordRepository extends CrudRepository<Password, Integer> {
}
