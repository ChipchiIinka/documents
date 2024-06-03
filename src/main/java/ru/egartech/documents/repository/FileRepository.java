package ru.egartech.documents.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.egartech.documents.entity.FileEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, UUID> {
    @Query("SELECT f FROM FileEntity f WHERE " +
            "LOWER(f.name) LIKE LOWER(CONCAT('%', :searchString, '%')) OR " +
            "LOWER(f.contentType) LIKE LOWER(CONCAT('%', :searchString, '%')) OR " +
            "LOWER(f.description) LIKE LOWER(CONCAT('%', :searchString, '%'))")
    List<FileEntity> findBySearchRequest(@Param("searchString") String searchString, Sort sort);

    List<FileEntity> findAllByLastModifiedBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);

    Optional<FileEntity> findByName(String name);
}