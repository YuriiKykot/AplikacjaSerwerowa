package com.example.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table
@NoArgsConstructor
@DiscriminatorColumn(name = "shot")
public class Shot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shot_id")
    private Long shot_id;

    private int startX;

    private int startY;

    private int endX;

    private int endY;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "piece_id")
    private Piece piece;
    public Shot(int startX, int startY, int endX, int endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }
}
