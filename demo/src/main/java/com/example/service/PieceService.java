package com.example.service;

import com.example.enums.Colors;
import com.example.enums.Status;
import com.example.models.*;
import com.example.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Transactional
@RequiredArgsConstructor
public class PieceService {

    private final PieceRepository pieceRepository;
    private final ShotRepository shotRepository;
    private final MoveRepository moveRepository;

    //////////////////////////////RANDOM_MOVE/////////////////////////////////////////////
    public Move moveRandom(Long pieceId, Colors color){
        Piece piece = getPieceById(pieceId);

        if(piece == null){
            return null;
        }

        if(piece.getColor() != color){
            return null;
        }

        Random random = new Random();
        Board board = piece.getBoard();

        int numRows = board.getNumRows();
        int numColumns = board.getNumColumns();
        Long board_id = board.getId();

        int endX = random.nextInt(numRows);
        int endY = random.nextInt(numColumns);
        int startX = piece.getX();
        int startY = piece.getY();

        while(true){
            if(!piece.isValidMove(startX,startY,endX,endY)){
                endX = random.nextInt(numRows);
                endY = random.nextInt(numColumns);
            }else{
                break;
            }
        }

        return check(endX,endY,board_id,piece);
    }

    //////////////////////////////////MOVE////////////////////////////////////////////////
    public Move check(int endX, int endY, Long board_id, Piece piece){
        if(isPieceAtPosition(endX, endY, board_id) && piece instanceof Tank && piece.getColor() != getColor(endX,endY,piece.getBoard().getId())){
            Piece pieceForDestroy = getPiece(endX,endY,board_id);
            destroyPiece(pieceForDestroy);
            return movePiece(piece,endX,endY);
        }else if(!isPieceAtPosition(endX, endY,board_id)) {
            return movePiece(piece, endX, endY);
        }else{
            return null;
        }
    }
    public Move movePiece(Piece piece,int endX, int endY){
        int startX = piece.getX();
        int startY = piece.getY();

        Move move = new Move(startX,startY,endX,endY);
        move.setPiece(piece);

        piece.setX(endX);
        piece.setY(endY);

        pieceRepository.save(piece);
        moveRepository.save(move);

        return move;
    }
    public Move makeMove(Long pieceId, int endX, int endY, Colors color){
        Piece piece = getPieceById(pieceId);

        if(piece == null){
            return null;
        }

        if(piece.getColor() != color){
            return null;
        }

        if(endX < 0 || endY < 0){
            return null;
        }

        if(!(endX < piece.getBoard().getNumRows() && endY < piece.getBoard().getNumColumns())){
            return null;
        }

        if(!piece.isValidMove(piece.getX(),piece.getY(),endX,endY)){
            return null;
        }

        Long board_id = piece.getBoard().getId();

        return check(endX,endY,board_id,piece);
    }

    //////////////////////////////////SHOT////////////////////////////////////////////////
    public List<Shot> makeShot(Long pieceId, int endX, int endY, int numberOfShots,Colors color){
        Piece piece = getPieceById(pieceId);

        if(piece == null){
            return Collections.emptyList();
        }

        if(piece.getColor() != color){
            return Collections.emptyList();
        }

        if(endX < 0 || endY < 0){
            return Collections.emptyList();
        }

        if(numberOfShots <= 0){
            return Collections.emptyList();
        }

        if(!(endX < piece.getBoard().getNumRows() && endY < piece.getBoard().getNumColumns())){
            return Collections.emptyList();
        }

        if(!piece.isValidShot(piece.getX(),piece.getY(),endX,endY)){
            return Collections.emptyList();
        }

        Long board_id = piece.getBoard().getId();

        List<Shot> shots = IntStream.range(0, numberOfShots)
                .mapToObj(i -> {
                    Shot shot = new Shot(piece.getX(), piece.getY(), endX, endY);
                    shot = moveShot(shot, board_id);
                    shot.setPiece(piece);
                    return shot;
                })
                .collect(Collectors.toList());

        pieceRepository.save(piece);
        shotRepository.saveAll(shots);

        return shots;
    }

    public Shot moveShot(Shot shot, Long board_id) {
        int startX = shot.getStartX();
        int startY = shot.getStartY();
        int endX = shot.getEndX();
        int endY = shot.getEndY();

        int currentX = startX;
        int currentY = startY;

        Piece pieceWhoFire = getPiece(currentX, currentY, board_id);
        boolean destroyedPiece = false;

        while (currentX != endX || currentY != endY) {
            Piece targetPiece = getPiece(currentX, currentY, board_id);
            if (targetPiece != null && targetPiece != pieceWhoFire) {
                destroyPiece(targetPiece);
                shot.setEndX(endX);
                shot.setEndY(endY);
                destroyedPiece = true;
                break;
            }
            currentX = calculateNextCoordinate(currentX, endX);
            currentY = calculateNextCoordinate(currentY, endY);
        }
        if(!destroyedPiece){
            Piece targetPiece = getPiece(endX, endY, board_id);
            if(targetPiece != null) {
                destroyPiece(targetPiece);
            }
        }
        return shot;
    }

    private int calculateNextCoordinate(int currentCoordinate, int targetCoordinate) {
        if (currentCoordinate < targetCoordinate) {
            return currentCoordinate + 1;
        } else if (currentCoordinate > targetCoordinate) {
            return currentCoordinate - 1;
        } else {
            return currentCoordinate;
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////
    public void destroyPiece(Piece piece){
        piece.setStatus(Status.DESTROYED);
        piece.setX(-1);
        piece.setY(-1);
        pieceRepository.save(piece);
    }

    public List<Piece> getAllPiece(Colors color){
        List<Piece> pieces = pieceRepository.findAll();
        List<Piece> filtered = new ArrayList<>();

        for(Piece p: pieces){
            if(p.getColor() == color){
                filtered.add(p);
            }
        }

        return filtered;
    }

    public Piece getPieceById(Long pieceId) {
        Optional<Piece> optionalPiece = pieceRepository.findById(pieceId);
        return optionalPiece.orElse(null);
    }

    public boolean isPieceAtPosition(int x, int y, Long board_id) {
        List<Piece> pieces = pieceRepository.findAllByBoard_id(board_id);
        for (Piece piece : pieces) {
            if (piece.getX() == x && piece.getY() == y) {
                return true;
            }
        }
        return false;
    }

    public Piece getPiece(int x, int y, Long board_id){
        List<Piece> pieces = pieceRepository.findAllByBoard_id(board_id);
        for (Piece piece : pieces) {
            if (piece.getX() == x && piece.getY() == y) {
                return piece;
            }
        }
        return null;
    }

    public Colors getColor(int x, int y, Long board_id){
        List<Piece> pieces = pieceRepository.findAllByBoard_id(board_id);
        Colors colors = null;
        for (Piece piece : pieces) {
            if (piece.getX() == x && piece.getY() == y) {
                colors = piece.getColor();
            }
        }
        return colors;
    }
}
