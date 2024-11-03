package org.dfproductions.budgetingserver.backend.repositories;

import org.dfproductions.budgetingserver.backend.templates.Password;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordRepository extends CrudRepository<Password, Integer> {
    @Query("SELECT * FROM passwords WHERE ID = :id")
    Password findPasswordById(int id);

}
