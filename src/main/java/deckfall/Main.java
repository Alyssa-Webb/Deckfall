package deckfall;

import deckfall.Controller.GameController;
import deckfall.Entity.Slayer;
import deckfall.Game.Game;
import deckfall.Observer.ConsoleLogger;
import deckfall.Factory.TowerFactory;
import deckfall.Observer.GameEventObserver;
import deckfall.Observer.SwingGameView;
import deckfall.Tower.Difficulty;
import deckfall.Tower.Tower;

/**
 * arg[0] (interface)  -> --console or --gui
 * arg[1] (difficulty) ->
 * arg[2] (tower type) ->
 **/
public class Main {
    public static void main(String[] args) {
        printUsage();

        if (args.length < 3) { System.exit(1); }

        String interfaceArg  = args[0].toLowerCase();
        String difficultyArg = args[1].toLowerCase();
        String towerArg      = args[2].toLowerCase();


        GameEventObserver view = null;
        switch (interfaceArg) {
            case "console" -> view = new ConsoleLogger();
            case "gui"     -> view = new SwingGameView();
            default -> {
                System.err.println("Unknown interface: " + args[0]);
                System.exit(1);
                return;
            }
        }

        Difficulty difficulty = switch (difficultyArg) {
            case "easy"   -> Difficulty.EASY;
            case "medium" -> Difficulty.MEDIUM;
            case "hard"   -> Difficulty.HARD;
            default -> {
                System.err.println("Unknown difficulty: " + args[1] + " — defaulting to EASY");
                yield Difficulty.EASY;
            }
        };

        Tower tower = switch (towerArg) {
            case "standard" -> TowerFactory.createStandardTower(difficulty);
            case "small"    -> TowerFactory.createSmallTower(difficulty);
            case "tiny"     -> TowerFactory.createTinyTower(difficulty);
            default -> {
                System.err.println("Unknown tower: " + args[2] + " — defaulting to standard");
                yield TowerFactory.createStandardTower(difficulty);
            }
        };
        Game game = new Game(new Slayer(), tower);
        GameController controller = new GameController(game, view);
        controller.gameStart();
    }

    private static void printUsage() {
        System.out.println("╔═══════════════════════════════════════════════════════════════════╗");
        System.out.println("║                      WELCOME TO DECKFALL! :)                      ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════════╣");
        System.out.println("║ Usage: ./gradlew -q run --args='<interface> <difficulty> <tower>' ║");
        System.out.println("║ <interface> = 'console' OR 'gui'                                  ║");
        System.out.println("║ <difficulty> = 'easy' OR 'medium' OR 'hard'                       ║");
        System.out.println("║ <tower> = 'standard' OR 'small' OR 'tiny'                         ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════╝");
    }
}