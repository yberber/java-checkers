import javax.swing.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
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

        while(true) {




//            if (gs.whiteToMove && !frame.isRunning) {

                ai.findBestMoveMinMax(gs, 7);
                Arrays.stream(ai.nextMoveOrCapturingMove).toList().forEach(m->System.out.print(m + "  "));
                System.out.println("");
                gs.makeMoveExtended(ai.nextMoveOrCapturingMove);
                frame.UpdateFrame();

//
//            }
        }
//            else{
//                System.out.println("Valid moves: " + gs.getValidMovesFromPlayerPerspective());
//                System.out.print("What is your input: ");
//                int input = Integer.parseInt(scanner.nextLine());
//                gs.makeMoveWithUserInput(new Move(input/1000, (input%1000)/100, (input%100)/10, (input%10), gs.board, gs.whiteToMove));
//            }
//
//            printGameState(gs.board);
//
//            if(gs.isGameOver()){
//                break;
//            }
//
//        }

//        System.out.println("Passed time to find moves: " + gs.passedTimeGetAllPossibleMoves);
//        System.out.println("Passed time to find captures: " + gs.passedTimeGetAllPossibleCaptures);


    }
}