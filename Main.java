import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    String name;
    int age;

    public static void printGameState(String[][] board){
        Arrays.stream(board).forEach(row ->
        {
            Arrays.stream(row).forEach(col -> System.out.print(col + " "));
            System.out.println();
        });
    }
    public static void main(String[] args) {




        GameState gs = new GameState();
        printGameState(gs.board);
        CheckersAI ai = new CheckersAI();

        ai.startTime = System.currentTimeMillis();

        while(true){
            ai.findBestMoveMinMax(gs, 9);
            gs.makeMoveExtended(ai.nextMoveOrCapturingMove);


            if(gs.isGameOver()){
                break;
            }

        }


    }
}