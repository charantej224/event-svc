package com.event.service.repository;

import com.event.service.domain.EventRecords;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the EventRecords entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventRecordsRepository extends JpaRepository<EventRecords, Long> {

}
