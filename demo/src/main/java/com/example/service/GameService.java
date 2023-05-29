package com.example.service;

import com.example.enums.Colors;
import com.example.enums.Types;
import com.example.models.*;
import com.example.repository.BoardRepository;
import com.example.repository.GameRepository;
import com.example.repository.PieceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final BoardRepository boardRepository;
    private final PieceRepository pieceRepository;

    public Game createGame(){
        int numOfTank = 2;
        int numOfWolverine = 3;
        int numOfArtillery = 1;
        int numOfRows = 4;
        int numOfColumns = 4;

        int boardAllCord = numOfRows * numOfColumns;
        int allPieces = (numOfTank + numOfWolverine + numOfArtillery) * 2;

        if(boardAllCord < allPieces){
            numOfRows = (int) Math.ceil(allPieces/2);
            numOfColumns = (int) Math.ceil(allPieces/2);
        }

        Game game = new Game();
        gameRepository.save(game);

        Board board = new Board(numOfRows,numOfColumns);
        boardRepository.save(board);

        board.setGame(game);
        game.setBoard(board);

        List<Piece> pieces = new ArrayList<>();
        pieces.addAll(createPiece(board,game,numOfTank,Colors.BLACK,Types.TANK, pieces));
        pieces.addAll(createPiece(board,game,numOfTank,Colors.WHITE,Types.TANK, pieces));
        pieces.addAll(createPiece(board,game,numOfWolverine,Colors.BLACK,Types.WOLVERINE, pieces));
        pieces.addAll(createPiece(board,game,numOfWolverine,Colors.WHITE,Types.WOLVERINE, pieces));
        pieces.addAll(createPiece(board,game,numOfArtillery,Colors.BLACK,Types.ARTILLERY, pieces));
        pieces.addAll(createPiece(board,game,numOfArtillery,Colors.WHITE,Types.ARTILLERY, pieces));

        game.setPieces(pieces);
        board.setPieces(pieces);
        boardRepository.save(board);
        gameRepository.save(game);

        return game;
    }

    public List<Piece> createPiece(Board board, Game game, int number, Colors color, Types type, List<Piece> pieces){
        for(int i = 0; i < number; i++) {
            Piece piece;
            if (type == Types.TANK) {
                piece = createCoordinates(new Tank(color, 0, 0), board, pieces);
            } else if (type == Types.WOLVERINE) {
                piece = createCoordinates(new Wolverine(color,0,0),board, pieces);
            } else {
                piece = createCoordinates(new Artillery(color,0,0),board, pieces);
            }
            piece.setBoard(board);
            piece.setGame(game);
            pieceRepository.save(piece);
            pieces.add(piece);
        }
        return pieces;
    }

    public Piece createCoordinates(Piece piece, Board board, List<Piece> pieces){
        Random random = new Random();
        int pieceX = random.nextInt(board.getNumRows());
        int pieceY = random.nextInt(board.getNumColumns());

        while(true){
            boolean coordinatesOccupied = false;
            for(Piece p: pieces){
                if(p.getX() == pieceX && p.getY() == pieceY){
                    coordinatesOccupied = true;
                    break;
                }
            }
            if (coordinatesOccupied) {
                pieceX = random.nextInt(board.getNumRows());
                pieceY = random.nextInt(board.getNumColumns());
            }else {
                break;
            }
        }
        piece.setX(pieceX);
        piece.setY(pieceY);
        pieces.add(piece);
        return piece;
    }
}
