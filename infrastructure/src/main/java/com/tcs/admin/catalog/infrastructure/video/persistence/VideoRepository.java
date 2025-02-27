package com.tcs.admin.catalog.infrastructure.video.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VideoRepository extends JpaRepository<VideoRepository, UUID> {
}
