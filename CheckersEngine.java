import java.util.LinkedList;

public class CheckersEngine {

    public static int getPieceId(String pieceName){
        switch(pieceName){
            case "wm":
                return 1;
            case "bm":
                return 2;
            case "wk":
                return 3;
            case "bk":
                return 4;
            default:
                return 0;
        }
    }

    public static int[][] getMoveDirections(byte pieceSign){
        if (Math.abs(pieceSign) == 1){
            if (pieceSign>0){
                return new int [][]{{-1, -1},{-1, 1}};
            }
            else
                return new int [][]{{1, -1},{1, 1}};
            }
//            else{
//                return new int [][]{{}};
//            }
//        }
//        else if(Math.abs(pieceSign) == 2){
//            return new int [][]{{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
//        }
        else{
            return new int [][]{{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
        }
    }


    public static int[][] getCaptureDirections(){
        return new int[][] {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
    }

    public static boolean isOnBoard(int row, int col){
        return row >= 0 && row < 10 && col >= 0 && col < 10;
    }

    public static void addInMovesWithCaptures(boolean anyFound, LinkedList<Move> foundCaptureList, LinkedList<Move[]> movesWithCaptures){
        if(!anyFound && foundCaptureList.size() > 0){
            if(movesWithCaptures.size() == 0){
                movesWithCaptures.add(foundCaptureList.toArray(new Move[foundCaptureList.size()]).clone());
            }
            else if (movesWithCaptures.getFirst().length < foundCaptureList.size()){
                movesWithCaptures.clear();
                movesWithCaptures.add(foundCaptureList.toArray(new Move[foundCaptureList.size()]).clone());
            }
            else if (movesWithCaptures.getFirst().length == foundCaptureList.size()){
                movesWithCaptures.add(foundCaptureList.toArray(new Move[foundCaptureList.size()]).clone());
            }
        }

    }


}


