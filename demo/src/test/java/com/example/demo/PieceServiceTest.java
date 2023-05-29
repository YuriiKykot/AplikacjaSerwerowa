package com.example.demo;

import com.example.enums.Colors;
import com.example.models.*;
import com.example.repository.MoveRepository;
import com.example.repository.PieceRepository;
import com.example.repository.ShotRepository;
import com.example.service.PieceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PieceServiceTest {

    @Autowired
    PieceService pieceService;

    @Mock
    private PieceRepository pieceRepository;

    @Mock
    private ShotRepository shotRepository;

    @Mock
    private MoveRepository moveRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void makeMove_shouldReturnNullIfPieceNotFound(){
        Long pieceId = 1L;
        Colors color = Colors.BLACK;

        Move result = pieceService.makeMove(pieceId,1,2,color);

        assertNull(result);
    }

    @Test
    void makeMove_shouldReturnNullIColorsAreDifferent(){
        Long pieceId = 2L;

        Piece piece = new Tank();

        piece.setPiece_id(pieceId);
        piece.setColor(Colors.BLACK);
        piece.setX(0);
        piece.setY(0);

        when(pieceRepository.findById(pieceId)).thenReturn(Optional.of(piece));

        Move result = pieceService.makeMove(pieceId,1,2,Colors.WHITE);

        assertNull(result);
    }

    @Test
    void makeMove_shouldReturnNullIfEndXEndYLowerThanZero(){
        Long pieceId = 3L;
        Colors color = Colors.BLACK;

        Piece piece = new Tank();

        piece.setPiece_id(pieceId);
        piece.setColor(color);
        piece.setX(0);
        piece.setY(0);

        when(pieceRepository.findById(pieceId)).thenReturn(Optional.of(piece));

        Move result = pieceService.makeMove(pieceId,-1,-1,color);

        assertNull(result);
    }

    @Test
    void makeMove_shouldReturnNullIfEndXEndYOutOfBoard(){
        Long pieceId = 4L;
        Colors color = Colors.BLACK;

        Piece piece = new Tank();
        Board board = new Board(10,10);

        piece.setPiece_id(pieceId);
        piece.setColor(color);
        piece.setX(0);
        piece.setY(0);
        piece.setBoard(board);

        when(pieceRepository.findById(pieceId)).thenReturn(Optional.of(piece));

        Move result = pieceService.makeMove(pieceId,100,100,color);

        assertNull(result);
    }

    @Test
    void makeMove_shouldReturnNullIfValidMove(){
        Long pieceId = 20L;
        Colors color = Colors.BLACK;
        Piece piece = new Tank();
        Board board = new Board(10,10);

        piece.setPiece_id(pieceId);
        piece.setColor(color);
        piece.setX(0);
        piece.setY(0);
        piece.setBoard(board);

        when(pieceRepository.findById(pieceId)).thenReturn(Optional.of(piece));

        Move result = pieceService.makeMove(pieceId,4,4,color);

        assertNull(result);
    }

    @Test
    void check_shouldReturnNullIfOnPositionSomeone(){
        Long pieceIdIn = 6L;
        Long pieceIdOn = 7L;
        Long boardId = 3L;
        Colors color = Colors.BLACK;

        Piece inputPiece = new Tank();
        Piece pieceOnPos= new Tank();

        Board board = new Board(10,10);
        board.setId(boardId);

        inputPiece.setPiece_id(pieceIdIn);
        inputPiece.setColor(color);
        inputPiece.setX(0);
        inputPiece.setY(0);
        inputPiece.setBoard(board);

        pieceOnPos.setPiece_id(pieceIdOn);
        pieceOnPos.setColor(color);
        pieceOnPos.setX(0);
        pieceOnPos.setY(1);
        pieceOnPos.setBoard(board);

        when(pieceRepository.findById(pieceIdIn)).thenReturn(Optional.of(inputPiece));
        when(pieceRepository.findById(pieceIdOn)).thenReturn(Optional.of(pieceOnPos));

        Move result = pieceService.check(0,1,boardId,inputPiece);

        assertNull(result);
    }

    @Test
    void check_shouldReturnMoveObject(){
        Long pieceId = 1L;
        Long boardId = 1L;
        Colors color = Colors.BLACK;

        Piece piece = new Tank();
        Board board = new Board(10,10);
        board.setId(boardId);

        piece.setPiece_id(pieceId);
        piece.setColor(color);
        piece.setX(5);
        piece.setY(5);
        piece.setBoard(board);

        when(pieceRepository.findById(pieceId)).thenReturn(Optional.of(piece));

        Move result = pieceService.check(5,6,boardId,piece);

        assertNotNull(result);
    }

    @Test
    void moveRandom_shouldReturnNullIfPieceNotFound(){
        Long pieceId = 9L;
        Colors color = Colors.BLACK;

        Move result = pieceService.makeMove(pieceId,1,2,color);

        assertNull(result);
    }

    @Test
    void moveRandom_shouldReturnNullIColorsAreDifferent(){
        Long pieceId = 2L;

        Piece piece = new Tank();

        piece.setPiece_id(pieceId);
        piece.setColor(Colors.BLACK);
        piece.setX(0);
        piece.setY(0);

        when(pieceRepository.findById(pieceId)).thenReturn(Optional.of(piece));

        Move result = pieceService.makeMove(pieceId,1,2,Colors.WHITE);

        assertNull(result);
    }

    @Test
    void movePiece_shouldReturnMove(){
        Long pieceId = 8L;
        Piece piece = new Tank();

        piece.setPiece_id(pieceId);
        piece.setColor(Colors.BLACK);
        piece.setX(0);
        piece.setY(0);

        when(pieceRepository.findById(pieceId)).thenReturn(Optional.of(piece));

        Move result = pieceService.movePiece(piece,0,1);

        assertNotNull(result);
    }

    @Test
    void makeShot_shouldReturnNullIfPieceIsNull(){
        Long pieceId = 10L;
        Colors color = Colors.BLACK;

        List<Shot> result = pieceService.makeShot(pieceId,2,2,1,color);

        assertTrue(result.isEmpty());
    }

    @Test
    void makeShot_ShouldReturnNullIfColorsAreDifferent(){
        Long pieceId = 11L;

        Piece piece = new Tank();

        piece.setPiece_id(pieceId);
        piece.setColor(Colors.BLACK);
        piece.setX(0);
        piece.setY(0);

        when(pieceRepository.findById(pieceId)).thenReturn(Optional.of(piece));

        List<Shot> result = pieceService.makeShot(pieceId,2,2,1,Colors.WHITE);

        assertTrue(result.isEmpty());
    }

    @Test
    void makeShot_ShouldReturnNullIfOutOfBoundOfBoard(){
        Long pieceId = 12L;
        Colors color = Colors.BLACK;

        Piece piece = new Tank();
        Board board = new Board(10,10);

        piece.setPiece_id(pieceId);
        piece.setColor(color);
        piece.setX(0);
        piece.setY(0);
        piece.setBoard(board);

        when(pieceRepository.findById(pieceId)).thenReturn(Optional.of(piece));

        List<Shot> result = pieceService.makeShot(pieceId,100,100,1,Colors.WHITE);

        assertTrue(result.isEmpty());
    }

    @Test
    void makeShot_ShouldReturnNullIfNumberOfShotZeroOrLess(){
        Long pieceId = 13L;
        Colors color = Colors.BLACK;
        Piece piece = new Tank();

        Board board = new Board(10,10);
        piece.setPiece_id(pieceId);
        piece.setColor(color);
        piece.setX(0);
        piece.setY(0);
        piece.setBoard(board);

        when(pieceRepository.findById(pieceId)).thenReturn(Optional.of(piece));

        List<Shot> result = pieceService.makeShot(pieceId,5,5,0,Colors.WHITE);

        assertTrue(result.isEmpty());
    }

    @Test
    void makeShot_ShouldReturnNullIfendXendYareLessThenZero(){
        Long pieceId = 13L;
        Colors color = Colors.BLACK;
        Piece piece = new Tank();

        Board board = new Board(10,10);
        piece.setPiece_id(pieceId);
        piece.setColor(color);
        piece.setX(0);
        piece.setY(0);
        piece.setBoard(board);

        when(pieceRepository.findById(pieceId)).thenReturn(Optional.of(piece));

        List<Shot> result = pieceService.makeShot(pieceId,-1,-1,1,Colors.WHITE);

        assertTrue(result.isEmpty());
    }

    @Test
    void makeShot_shouldReturnNullIfValidShot(){
        Long pieceId = 14L;
        Colors color = Colors.BLACK;
        Piece piece = new Tank();
        Board board = new Board(10,10);

        piece.setPiece_id(pieceId);
        piece.setColor(color);
        piece.setX(0);
        piece.setY(0);
        piece.setBoard(board);

        when(pieceRepository.findById(pieceId)).thenReturn(Optional.of(piece));

        List<Shot> result = pieceService.makeShot(pieceId,9,4,1, color);

        assertTrue(result.isEmpty());
    }

    @Test
    void moveShot_shouldReturnShot() {
        Shot shot = new Shot(0,0,1,1);
        Board board = new Board(10, 10);

        Shot result = pieceService.moveShot(shot,board.getId());

        assertNotNull(result);
    }

    @Test
    void getAllPiece_shouldByColorReturnAllPieces(){
        List<Piece> result = pieceService.getAllPiece(Colors.WHITE);

        assertFalse(result.isEmpty());
    }

    @Test
    void getPieceById_shouldReturnByIdPiece(){
        assertNotNull(pieceService.getPieceById(1L));
    }

    @Test
    void isPieceAtPosition_ShouldReturnPiece(){
        int x = 0;
        int y = 0;
        Long pieceId = 25L;
        Long boardId = 1L;
        Piece piece = new Tank();
        Board board = new Board(3,3);

        piece.setX(x);
        piece.setY(y);
        piece.setBoard(board);
        piece.setColor(Colors.WHITE);

        board.setId(boardId);

        List<Piece> pieces = new ArrayList<>();
        pieces.add(piece);
        board.setPieces(pieces);

        Boolean result = pieceService.isPieceAtPosition(x,y,1L);
        assertTrue(result);
    }

    @Test
    void isPieceAtPosition_ShouldReturnFalse(){
        assertFalse(pieceService.isPieceAtPosition(3,3,1L));
    }

    @Test
    void getPiece_ShouldReturnPiece(){
        assertNotNull(pieceService.getPiece(0,0,1L));
    }

    @Test
    void getPiece_ShouldReturnNull(){
        assertNull(pieceService.getPiece(3,3,1L));
    }

    @Test
    void getColor_ShouldReturnColor(){
        assertNotNull(pieceService.getPiece(0,0,1L));
    }

    @Test
    void getColor_ShouldReturnNull(){
        assertNull(pieceService.getPiece(3,3,1L));
    }
}
