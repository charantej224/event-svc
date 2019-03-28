package com.event.service.repository;

import com.event.service.domain.EventRecords;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomEventRepository extends EventRecordsRepository {

    Optional<List<EventRecords>> findByEmailIdOrderByCreatedDateAsc(String emailId);
}
