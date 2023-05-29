package com.example.models;

import com.example.enums.Colors;
import com.example.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table
@NoArgsConstructor
@DiscriminatorColumn(name = "piece")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Piece{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="piece_id")
    private Long piece_id;

    @Enumerated(EnumType.STRING)
    private Colors color;

    private int x;

    private int y;

    @Enumerated(EnumType.STRING)
    private Status status;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="board_id")
    private Board board;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @OneToMany(mappedBy = "piece",cascade=CascadeType.ALL)
    private List<Shot> shots;

    @OneToMany(mappedBy = "piece",cascade=CascadeType.ALL)
    private List<Move> moves;

    public Piece(Colors color, int x, int y) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.status = Status.WORK;
        this.shots = new ArrayList<>();
        this.moves = new ArrayList<>();

    }

    public abstract boolean isValidMove(int x, int y, int endX, int endY);

    public abstract boolean isValidShot(int x, int y, int endX, int endY);
}
