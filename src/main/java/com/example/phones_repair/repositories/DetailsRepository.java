package com.example.phones_repair.repositories;

import com.example.phones_repair.entities.Details;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailsRepository extends JpaRepository<Details, Long> {
    @Query("SELECT d FROM Details d WHERE d.availableDetails.supplier.id = :supplierId AND d.status = :status")
    List<Details> findByStatus(@Param("supplierId") Long supplierId, @Param("status") String status);

    @Query("SELECT d FROM Details d " +
            "WHERE d.availableDetails.supplier.id = :supplierId")
    List<Details> findDetailsBySupplierId(@Param("supplierId") Long supplierId);
}
