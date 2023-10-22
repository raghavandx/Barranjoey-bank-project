package com.barrenjoey.java.psr;

import com.barrenjoey.java.psr.model.Item;
import com.barrenjoey.java.psr.model.Move;
import com.barrenjoey.java.psr.model.Player;
import com.barrenjoey.java.psr.model.Result;

import java.util.*;
import java.util.stream.Collectors;

public class RockPepperScissorsGamePlayService implements GenericRockPaperScissorService {
    private final Map<Item, List<Item>> winningMoves;
    public RockPepperScissorsGamePlayService() {
        winningMoves = setUpWinningMoves();
    }
    public Result playGame(List<Move> moves) {
        Set<Item> uniqueMoves = moves.stream().map(Move::getItem).collect(Collectors.toSet());
        if(uniqueMoves.size() != 2) {
            return Result.tieResult();
        }
        else {
            for(Map.Entry<Item, List<Item>> entry : winningMoves.entrySet()) {
                if(entry.getValue().containsAll(uniqueMoves)) {
                    Result winner = new Result();
                    List<Move> winningMoves = moves.stream().filter(m -> m.getItem().equals(entry.getKey())).collect(Collectors.toList());
                    List<Player> players = winningMoves.stream().map(wm -> wm.getPlayer()).collect(Collectors.toList());
                    if(players.size() > 1) {
                        winner.setIndecisive(true);
                    }
                    else {
                        winner.setTie(false);
                    }
                    winner.setPlayer(players);
                    return winner;
                }
            }
        }
        return Result.tieResult();
    }

    public Map<Item, List<Item>> setUpWinningMoves() {
        Map<Item, List<Item>> winningMoves = new HashMap<>();
        //This can also be defined in configuration files
        //insert the winning combination, eg for Rock -> Rock can smash scissor
        //Paper -> Paper is the winner, as paper covers the rock
        //Scissor -> Scissor cuts paper
        //Extension
        //Lizard -> Lizard eats paper
        //Rock -> Rock kills Lizard
        winningMoves.put(Item.Rock, Arrays.asList(Item.Rock, Item.Scissor));
        winningMoves.put(Item.Paper, Arrays.asList(Item.Paper, Item.Rock));
        winningMoves.put(Item.Scissor, Arrays.asList(Item.Scissor, Item.Paper));
        winningMoves.put(Item.Lizard, Arrays.asList(Item.Lizard, Item.Paper));
        winningMoves.put(Item.Rock, Arrays.asList(Item.Rock, Item.Lizard));
        winningMoves.put(Item.Scissor, Arrays.asList(Item.Scissor, Item.Lizard));
        return winningMoves;
    }
}
