package com.example.controllers;

import com.example.models.Game;
import com.example.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
public class GameController {
    @Autowired
    GameService gameService;

    @PostMapping("/createGame")
    @ResponseBody
    public Game createGame(){
        return gameService.createGame();
    }
}
