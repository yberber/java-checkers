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

    public static int[][] getMoveDirections(String pieceSign){
        if (pieceSign.charAt(1) == 'm'){
            if (pieceSign.charAt(0) == 'w'){
                return new int [][]{{-1, -1},{-1, 1}};
            }
            else if (pieceSign.charAt(0) == 'b'){
                return new int [][]{{1, -1},{1, 1}};
            }
            else{
                return new int [][]{{}};
            }
        }
        else if(pieceSign.charAt(1) == 'k'){
            return new int [][]{{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
        }
        else{
            return new int [][]{{}};
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


