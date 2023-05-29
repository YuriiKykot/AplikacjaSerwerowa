package com.example.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Table
@NoArgsConstructor
@DiscriminatorColumn(name = "board")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private int numRows;
    private int numColumns;

    @OneToOne(mappedBy = "board")
    private Game game;

    @OneToMany(mappedBy = "board",cascade=CascadeType.ALL)
    private List<Piece> pieces;

    public Board(int numRows, int numColumns){
        this.numRows = numRows;
        this.numColumns = numColumns;
    }
}
