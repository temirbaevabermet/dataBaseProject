package com.example.phones_repair.repositories;

import com.example.phones_repair.entities.AvailableDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvailableDetailsRepository extends JpaRepository<AvailableDetails, Long> {
}
