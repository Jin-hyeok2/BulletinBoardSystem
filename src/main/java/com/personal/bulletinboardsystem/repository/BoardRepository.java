package com.personal.bulletinboardsystem.repository;

import org.springframework.stereotype.Repository;

import com.personal.bulletinboardsystem.entity.Board;

import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    
}
