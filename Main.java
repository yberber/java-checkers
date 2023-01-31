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

        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        ai.startTime = System.currentTimeMillis();


//        System.out.println(gs.getValidMovesFromPlayerPerspective());
//        System.out.println(gs.getAllPossibleMoves());
//        System.out.println(gs.getAllPossibleCaptures());
//        System.out.println(gs.validMoves);
//        System.out.println(gs.validCaptureMoves);
//        System.out.println(gs.singleValidMoves);





        boolean runWhile=false;
        while(true){
            ai.findBestMoveMinMax(gs, 9);
//                System.out.println("best capture: " + ai.nextCapture);
//                System.out.println("best move: " + ai.nextMove);
            gs.makeMoveExtended(ai.nextMove, ai.nextCapture);

//            if(false){
//                ai.findBestMoveMinMax(gs, 7);
////                System.out.println("best capture: " + ai.nextCapture);
////                System.out.println("best move: " + ai.nextMove);
//                gs.makeMoveExtended(ai.nextMove, ai.nextCapture);
//                printGameState(gs.board);
//
//                if(ai.nextMove!=null){
//                    System.out.println(ai.nextMove);
//                }else{
//                    System.out.println(ai.nextCapture);
//
//                }
//
//
//            }
//
//
//            else{
//                System.out.println("please write your move: ");
//                int input = Integer.parseInt(myObj.nextLine());
//                Move move = new Move(input/1000, (input%1000)/100, (input%100)/10, input%10, gs.board, gs.whiteToMove);
//                System.out.println(move);
//
//
//                LinkedList<LinkedList<Move>> possibleCapturesExtended = gs.getAllPossibleCaptures();
//                LinkedList<Move> possibleMoveExtended = gs.getAllPossibleMoves();
//                if(possibleCapturesExtended.contains(move) || possibleMoveExtended.contains(move)){
//                    System.out.println(move);
//                    gs.makeMove(move);
//                }else{
//                    continue;
//                }
//
//            }

            if(gs.isGameOver()){
                break;
            }

//            printGameState(gs.board);
//            System.out.println("*******************************");


        }



//        ai.findBestMoveMinMax(gs, 7, System.currentTimeMillis());
//        System.out.println("best capture: " + ai.nextCapture);
//        System.out.println("best move: " + ai.nextMove);
//        while(true){
//
//
//            break;
//        }

    }
}