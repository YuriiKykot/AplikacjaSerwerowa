package com.example.demo;

import com.example.enums.Colors;
import com.example.enums.Types;
import com.example.models.Board;
import com.example.models.Game;
import com.example.models.Piece;
import com.example.models.Tank;
import com.example.repository.GameRepository;
import com.example.service.GameService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GameServiceTest {

    @Autowired
    GameService gameService;

    @Mock
    GameRepository gameRepository;

    @Test
    void createGame_ShouldReturnGame(){
        assertNotNull(gameService.createGame());
    }

    @Test
    void createPiece_ShouldReturnListOfPieces(){
        Long gameId = 1L;
        Long boardId = 1L;

        Game game = new Game();
        Board board = new Board(8,8);

        game.setGame_id(gameId);
        board.setId(boardId);

        List<Piece> pieces = new ArrayList<>();

        List<Piece> result = gameService.createPiece(board,game,2, Colors.WHITE, Types.ARTILLERY, pieces);

        assertFalse(result.isEmpty());
    }

    @Test
    void createCoordinate_ShouldReturnPiece(){
        Long gameId = 1L;
        Long boardId = 1L;

        Game game = new Game();
        Board board = new Board(8,8);

        game.setGame_id(gameId);
        board.setId(boardId);

        Piece piece = new Tank();
        piece.setPiece_id(1L);
        piece.setGame(game);
        piece.setBoard(board);
        piece.setX(0);
        piece.setY(0);
        piece.setColor(Colors.WHITE);

        List<Piece> pieces = new ArrayList<>();

        Piece result = gameService.createCoordinates(piece,board,pieces);

        assertNotNull(result);
    }
}
