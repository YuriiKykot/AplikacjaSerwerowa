package com.example.models;

import com.example.enums.Colors;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@NoArgsConstructor
@DiscriminatorValue("wolverine")
public class Wolverine extends Piece{

    public Wolverine(Colors color, int x, int y) {
        super(color, x, y);
    }

    @Override
    public boolean isValidMove(int x, int y, int endX, int endY) {
        int checkX = Math.abs(x - endX);
        int checkY = Math.abs(y - endY);

        if ((checkX >= 1 && checkX <= 3 && checkY == 0) ||
                (checkY >= 1 && checkY <= 3 && checkX == 0)) {
            return true;
        }

        return false;
    }

    @Override
    public boolean isValidShot(int x, int y, int endX, int endY) {
        return false;
    }
}
