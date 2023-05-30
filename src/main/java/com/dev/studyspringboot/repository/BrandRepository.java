package com.dev.studyspringboot.repository;

import com.dev.studyspringboot.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    @Modifying
    @Query("UPDATE Brand b SET b.deletedAt = CURRENT_TIMESTAMP WHERE b.id = :brandId")
    void softDeleteById(@Param("brandId") Long brandId);

    List<Brand> findAllByDeletedAtIsNull();
    Brand findByIdAndDeletedAtIsNull(Long brandId);
}
