package com.campalans.tuneguessr.adapter.out.persistence;

import com.campalans.tuneguessr.adapter.out.persistence.entity.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlayerJpaRepository extends JpaRepository<PlayerEntity, UUID> {

}
