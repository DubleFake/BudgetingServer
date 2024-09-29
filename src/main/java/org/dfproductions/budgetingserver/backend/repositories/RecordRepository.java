package org.dfproductions.budgetingserver.backend.repositories;

import org.dfproductions.budgetingserver.backend.templates.Record;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository extends CrudRepository<Record, Integer> {
}
