package com.example.repository;

import com.example.models.Piece;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PieceRepository extends JpaRepository<Piece,Long> {
    List<Piece> findAllByBoard_id(Long id);
}
