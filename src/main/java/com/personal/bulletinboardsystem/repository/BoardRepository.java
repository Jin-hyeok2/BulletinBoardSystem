package com.personal.bulletinboardsystem.repository;

import org.springframework.stereotype.Repository;

import com.personal.bulletinboardsystem.entity.Board;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findAllByCreatedAtBetween(Pageable pageable, LocalDateTime start, LocalDateTime end);
}
