package com.example.models;

import com.example.enums.Colors;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@DiscriminatorValue("artillery")
public class Artillery extends Piece{

    public Artillery(Colors color, int x, int y) {
        super(color, x, y);
    }

    @Override
    public boolean isValidMove(int x, int y, int endX, int endY){
        return false;
    }

    @Override
    public boolean isValidShot(int x, int y, int endX, int endY) {
        int checkX = Math.abs(x - endX);
        int checkY = Math.abs(y - endY);

        if ((checkX > 0 && checkY == 0) ||
                (checkY > 0 && checkX == 0) ||
                (checkX > 0 && checkY > 0 && checkX == checkY)) {
            return true;
        }
        return false;
    }
}
