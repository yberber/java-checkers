import javax.swing.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {

    public final static boolean isWhiteHuman = false;
    public final static boolean isBlackHuman = false;

    public static boolean isRunning = false;
    String name;
    int age;

    public static void printGameState(byte[][] board){
        Arrays.asList(board).forEach(row->{
            for(byte col : row){
                System.out.print(CheckersEngine.getPieceShortName(col) + " ");
            }
            System.out.println();


        });
    }
    public static void main(String[] args) throws IOException {




        GameState gs = new GameState();
        CheckersAI ai = new CheckersAI();

        ai.startTime = System.currentTimeMillis();


        CheckersUI frame = new CheckersUI("Checkers", gs, ai);
        frame.setVisible(true);

        frame.UpdateFrame();

        while(!gs.isGameOver()) {




            System.out.print("");

            if (!isRunning && (gs.whiteToMove && !isWhiteHuman || !gs.whiteToMove && !isBlackHuman)) {
                isRunning = true;
                ai.findBestMoveMinMax(gs, 7);
                Arrays.stream(ai.nextMoveOrCapturingMove).toList().forEach(m->System.out.print(m + "  "));
                System.out.println("");
                gs.makeMoveExtended(ai.nextMoveOrCapturingMove, true);
                frame.UpdateFrame();
                isRunning = false;
            }
        }




    }
}