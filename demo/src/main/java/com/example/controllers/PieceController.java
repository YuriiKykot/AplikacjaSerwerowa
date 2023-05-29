package com.example.controllers;

import com.example.enums.Colors;
import com.example.models.*;
import com.example.request.MoveRequest;
import com.example.request.ShotRequest;
import com.example.service.PieceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@EnableScheduling
@RestController
@RequestMapping("/pieces")
public class PieceController {

    @Autowired
    PieceService pieceService;

    private Colors black = Colors.BLACK;
    private Colors white = Colors.WHITE;
    private boolean isGameAvailableWhite = true;
    private boolean isGameAvailableBlack = true;

    @GetMapping("/black")
    @ResponseBody
    public List<Piece> getPiecesByColorBlack(){
        return pieceService.getAllPiece(black);
    }

    @GetMapping("/white")
    @ResponseBody
    public List<Piece> getPiecesByColorWhite(){
        return pieceService.getAllPiece(white);
    }

    @PostMapping("/black/{pieceId}/move")
    @ResponseBody
    public Move movePieceBlack(@PathVariable("pieceId") Long pieceId,
                               @RequestBody MoveRequest moveRequest){
        if(isGameAvailableBlack){
            isGameAvailableBlack = false;
            scheduleGameAvailabilityForMove(pieceId,black);
            return pieceService.makeMove(pieceId, moveRequest.getEndX(), moveRequest.getEndY(),black);
        }else{
            return null;
        }
    }

    @PostMapping("/white/{pieceId}/move")
    @ResponseBody
    public Move movePieceWhite(@PathVariable("pieceId") Long pieceId,
                                            @RequestBody MoveRequest moveRequest){
        if(isGameAvailableWhite){
            isGameAvailableWhite = false;
            scheduleGameAvailabilityForMove(pieceId,white);
            return pieceService.makeMove(pieceId, moveRequest.getEndX(), moveRequest.getEndY(),white);
        }
        return null;
    }

    @PostMapping("/black/{pieceId}/shot")
    @ResponseBody
    public List<Shot> shotPieceBlack(@PathVariable("pieceId") Long pieceId,
                                     @RequestBody ShotRequest shotRequest){
        if(isGameAvailableBlack){
            isGameAvailableBlack = false;
            scheduleGameAvailabilityForShot(pieceId,black);
            return pieceService.makeShot(pieceId,shotRequest.getEndX(),shotRequest.getEndY(),shotRequest.getNumberOfShots(),black);
        }
        return null;
    }

    @PostMapping("/white/{pieceId}/shot")
    @ResponseBody
    public List<Shot> shotPieceWhite(@PathVariable("pieceId") Long pieceId,
                                     @RequestBody ShotRequest shotRequest){
        if(isGameAvailableWhite){
            isGameAvailableWhite = false;
            scheduleGameAvailabilityForShot(pieceId,white);
            return pieceService.makeShot(pieceId,shotRequest.getEndX(),shotRequest.getEndY(),shotRequest.getNumberOfShots(),white);
        }
        return null;
    }

    @PostMapping("/black/{pieceId}/randomMove")
    @ResponseBody
    public Move moveRandomBlack(@PathVariable("pieceId") Long pieceId){
        if(isGameAvailableBlack){
            isGameAvailableBlack = false;
            scheduleGameAvailabilityForMove(pieceId,black);
            return pieceService.moveRandom(pieceId,black);
        }
        return null;
    }

    @PostMapping("/white/{pieceId}/randomMove")
    @ResponseBody
    public Move moveRandomWhite(@PathVariable("pieceId") Long pieceId){
        if(isGameAvailableWhite){
            isGameAvailableWhite = false;
            scheduleGameAvailabilityForMove(pieceId,white);
            return pieceService.moveRandom(pieceId,white);
        }
        return null;
    }

    ////////////////////////////SCHEDULE////////////////////////////////////////
    private void scheduleGameAvailabilityForMove(Long pieceId, Colors color) {
        Piece piece = pieceService.getPieceById(pieceId);
        long delay = 0;
        if (color == piece.getColor()) {
            if (color == Colors.WHITE) {
                if (piece instanceof Tank) {
                    delay = 5;
                } else if (piece instanceof Wolverine) {
                    delay = 7;
                } else {
                    return;
                }
            } else {
                if (piece instanceof Tank) {
                    delay = 5;
                } else if (piece instanceof Wolverine) {
                    delay = 7;
                } else {
                    return;
                }
            }
        }

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(() -> {
            isGameAvailableWhite = true;
            isGameAvailableBlack = true;
        }, delay, TimeUnit.SECONDS);
        scheduler.shutdown();
    }

    private void scheduleGameAvailabilityForShot(Long pieceId, Colors color) {
        Piece piece = pieceService.getPieceById(pieceId);
        long delay = 0;
        if (color == piece.getColor()) {
            if (color == Colors.WHITE) {
                if (piece instanceof Tank) {
                    delay = 10;
                } else if (piece instanceof Artillery) {
                    delay = 13;
                } else {
                    return;
                }
            } else {
                if (piece instanceof Tank) {
                    delay = 10;
                } else if (piece instanceof Artillery) {
                    delay = 13;
                } else {
                    return;
                }
            }
        }

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(() -> {
            isGameAvailableWhite = true;
            isGameAvailableBlack = true;
        }, delay, TimeUnit.SECONDS);
        scheduler.shutdown();
    }
}
