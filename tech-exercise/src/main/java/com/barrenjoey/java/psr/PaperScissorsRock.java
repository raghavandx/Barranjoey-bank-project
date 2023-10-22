package com.barrenjoey.java.psr;

import com.barrenjoey.java.psr.model.Item;
import com.barrenjoey.java.psr.model.Move;
import com.barrenjoey.java.psr.model.Player;
import com.barrenjoey.java.psr.model.Result;

import java.util.*;

public class PaperScissorsRock {

    public static int playTheGame() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Player1: Choose (p)aper, (s)cissors, (r)ock");
        String player_1_move = scanner.nextLine();
        System.out.println("Player2: Choose (p)aper, (s)cissors, (r)ock");
        String player_2_move = scanner.nextLine();
        switch (player_1_move.trim().toLowerCase()) {
            case "p":
                if ("p".equals(player_2_move)) {
                    System.out.println("It's a tie!");
                    return 0;
                }
                if ("s".equals(player_2_move)) {
                    System.out.println("Player 2 wins");
                    return 2;
                } else if ("r".equals(player_2_move)) {
                    System.out.println("Player 1 wins");
                    return 1;
                }
                break;

            case "s":
                if ("p".equals(player_2_move)) {
                    System.out.println("Player 1 wins");
                    return 1;
                }
                if ("s".equals(player_2_move)) {
                    System.out.println("It's a tie!");
                    return 0;
                } else if ("r".equals(player_2_move)) {
                    System.out.println("Player 2 wins");
                    return 2;
                }
                break;

            case "r":
                if ("p".equals(player_2_move)) {
                    System.out.println("Player 2 wins");
                    return 1;
                }
                if ("s".equals(player_2_move)) {
                    System.out.println("Player 1 wins");
                    return 0;
                } else if ("r".equals(player_2_move)) {
                    System.out.println("It's a tie!");
                    return 2;
                }
                break;
        }
        return 0;
    }


    private static void runGame(int rounds, List<Player> players, Scanner scanner, Map<Integer, Result> results) {
        GenericRockPaperScissorService gamePlayService = new RockPepperScissorsGamePlayService();
        for(int r = 1; r <= rounds; r++) {
            Result result = null;
            List<Move> moves = new ArrayList<>();
            for(int p = 1; p <= players.size(); p++) {

                System.out.println("Round " + r + " and Player " + p + " move R/P/S");
                String playerMoveItem = scanner.nextLine();
                Move move = new Move();
                move.setItem(Item.fromString(playerMoveItem));
                Player p1 = new Player();
                p1.setName(""+p);
                move.setPlayer(p1);
                moves.add(move);
            }

            result = gamePlayService.playGame(moves);
            if(result.isIndecisive()) {
                System.out.println("Play sub round for tied winners");
                runGame(1, result.getPlayer(), scanner, results);
            }
            results.putIfAbsent(r, result);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("How many rounds to play?");
        int rounds = Integer.parseInt(scanner.nextLine());
        System.out.println("How many players are playing");
        int players = Integer.parseInt(scanner.nextLine());
        List<Player> playerList = new ArrayList<>();
        for(int p=1; p<= players; p++) {
            Player pl = new Player();
            pl.setName(""+p);
            playerList.add(pl);
        }
        Map<Integer, Result> results = new HashMap<>();
        runGame(rounds, playerList, scanner, results);


        for(Map.Entry<Integer, Result> entry : results.entrySet()) {
            if(entry.getValue().isTie()) {
                System.out.println("Round " + entry.getKey() + " is a tie!");
            }
            else {
                System.out.println("Round " + entry.getKey() + " won by player " + entry.getValue().getPlayer().get(0).getName());
            }
        }
    }
}
