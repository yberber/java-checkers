import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Timer;
import java.util.stream.Collectors;

public class GameState {

    LinkedList<Move[]> validMoves;
    LinkedList<Move[]> validCaptureMoves;

    LinkedList<Move> foundCaptureList = new LinkedList<>();

    LinkedList<Move> singleValidMoves;
    byte[][] board;
    boolean whiteToMove;
    LinkedList<Move> moveLog;
    boolean isCapturing;
    int captureIndex;

    LinkedList<Move[]> possibleCapturesOfBlack;
    LinkedList<Move[]> possibleCapturesOfWhite;


    public GameState(){
        validMoves = new LinkedList<>();
        validCaptureMoves = new LinkedList<>();
        singleValidMoves = new LinkedList<>();

        possibleCapturesOfBlack = new LinkedList<>();
        possibleCapturesOfWhite = new LinkedList<>();


        board = new byte[][]{
                {0, -1, 0, -1, 0, -1, 0, -1, 0, -1},
                {-1, 0, -1, 0, -1, 0, -1, 0, -1, 0},
                {0, -1, 0, -1, 0, -1, 0, -1, 0, -1},
                {-1, 0, -1, 0, -1, 0, -1, 0, -1, 0},

                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},

                {0, 1, 0, 1, 0, 1, 0, 1, 0, 1},
                {1, 0, 1, 0, 1, 0, 1, 0, 1, 0},
                {0, 1, 0, 1, 0, 1, 0, 1, 0, 1},
                {1, 0, 1, 0, 1, 0, 1, 0, 1, 0}
        };



        whiteToMove = true;
        moveLog = new LinkedList<>();
        captureIndex = 0;
    }

    void makeMove(Move move) {
        makeMove(move, false);
    }

    void makeMove(Move move, boolean searchingMode){
        board[move.startRow][move.startCol] = 0;
        board[move.endRow][move.endCol] = move.pieceMoved;
        if (move.capturedPiece!=0){
            int row = move.capturedPieceRow;
            int col = move.capturedPieceCol;
            board[row][col] = 0;
        }
        moveLog.add(move);

        if (!searchingMode){
            if (!isCapturing){
                changeTurn();
            }
            else{
                if(captureIndex+1 >= validCaptureMoves.get(0).length){
                    isCapturing = false;
                    changeTurn();
                }
                else{
                    for (int index = validCaptureMoves.size()-1; index >= 0; index--){
                        if (moveLog.getLast() != validCaptureMoves.get(index)[captureIndex])
                            validCaptureMoves.remove(index);
                    }
                }
            }
        }
    }

//    void makeMoveExtended(Move singleMove){
//        makeMove(singleMove, true);
//        changeTurn();
//    }

    static int count = 0;
    void makeMoveExtended(Move[] multipleMove){
        for (Move move: multipleMove) {
            makeMove(move, true);
        }
//        adjustCaptureLists();
        changeTurn();
    }

    void makeMoveExtended(Move[] multipleMove, boolean asd){
        for (Move move: multipleMove) {
            makeMove(move, true);
        }
//        adjustCaptureLists();
        changeTurn();
    }

    void adjustCaptureLists(){
        String piece = this.whiteToMove ? "white" : "black";
        if(moveLog.size() >= 2){
            if(moveLog.get(moveLog.size() - 2).isWhite == whiteToMove){

                if(whiteToMove){
                    System.out.println(piece + " captured 2 or more pieces");
                }else{
                    System.out.println(piece + " captured 2 or more pieces");
                }
            }else if(moveLog.getLast().capturedPiece != 0){
                if(whiteToMove){
                    System.out.println(piece + " captured 1 piece");
                }else{
                    System.out.println(piece + " captured 1 piece");
                }
            }
            else{
                System.out.println(piece + " made a simple move");

//                calculateAndAssignPossibleCapturesAfterSingleMove();
            }

            printCaptureLists();

        }
    }


    void printCaptureLists(){
        System.out.println("********* WHITE CAPTURES *********");
        possibleCapturesOfWhite.forEach(moveArray ->
                {
                    Arrays.stream(moveArray).toList().forEach(m->System.out.print(m + "  "));
                    System.out.println("");
                }
        );
        System.out.println("********* BLACK CAPTURES *********");
        possibleCapturesOfBlack.forEach(moveArray ->
                {
                    Arrays.stream(moveArray).toList().forEach(m->System.out.print(m + "  "));
                    System.out.println("");
                }
        );
        System.out.println("************************************");

    }
    void calculateAndAssignPossibleCapturesAfterSingleMove(){
        Move move = this.moveLog.getLast();
        System.out.println(move.getCheckersRowColNotation());
        if(whiteToMove){
            possibleCapturesOfWhite = getAllPossibleCaptures();
        }else{
            possibleCapturesOfBlack = getAllPossibleCaptures();
        }
    }


    boolean isTherePossibleCapturesOnGivenPosition(LinkedList<Move[]> possibleCaptureList, boolean isWhiteAttacker, byte row, byte col){

        int[][] directions = CheckersEngine.getCaptureDirections();
        boolean isWhiteLastPlayed = !isWhiteAttacker;

        for (int[] d : directions){
            int endRow = row + d[0];
            int endCol = col + d[1];

            if(CheckersEngine.isOnBoard(endRow, endCol)) {
                byte piece = board[endRow][endCol];
                if (piece > 0 && isWhiteLastPlayed || piece < 0 && !isWhiteLastPlayed) {
                    for (int j = 2; j < 8; j++) {
                        int attackerRow = row + d[0] * j;
                        int attackerCol = col + d[1] * j;
                        if (CheckersEngine.isOnBoard(attackerRow, attackerCol)) {
                            byte attackerPiece = board[endRow][endCol];
                            if (attackerPiece > 0 && isWhiteAttacker || attackerPiece < 0 && !isWhiteAttacker) {
//                                Move move = new Move();
//                                possibleCaptureList.add()
                            }
                        } else {
                            break;
                        }
                    }
                } else {
                    break;
                }


            }
            else{
                break;
            }
        }
        return false;
    }

    void makeMoveWithUserInput(Move singleMove){

        for(Move move : singleValidMoves){
            if(move.equals(singleMove)){
                makeMove(move, false);
                return;
            }
        }

    }


//    void makeMoveExtended(Move singleMove, LinkedList<Move> multipleMove){
//        if(singleMove != null){
//            makeMoveExtended(singleMove);
//        }
//        else if(multipleMove != null){
//            makeMoveExtended(multipleMove);
//
//        }
//
//    }


    void undoMove(){
        undoMove(false);
    }
    void undoMove(boolean onlyOne){
        if (moveLog.size() > 0){
            while(true){
                Move move = moveLog.removeLast();
                board[move.startRow][move.startCol] = move.pieceMoved;
                board[move.endRow][move.endCol] = 0;
                if (move.capturedPiece != 0){
                    board[move.capturedPieceRow][move.capturedPieceCol] = move.capturedPiece;
                }
                if (onlyOne)
                    return;

                if (moveLog.size() <= 0 || move.isWhite != moveLog.getLast().isWhite)
                    break;
            }
            if (captureIndex > 1){
                whiteToMove = !whiteToMove;
            }
            changeTurn();

        }
    }

    LinkedList<Move> getValidMovesFromPlayerPerspective(){
//        if (!isCapturing)
//            validCaptureMoves = getAllPossibleCaptures();

        singleValidMoves.clear();

        if(isCapturing && captureIndex-1 >= validCaptureMoves.size()){
            captureIndex+=1;
            for(Move[] move : validCaptureMoves){
                singleValidMoves.add(move[captureIndex]);
            }
            return singleValidMoves;
        }
        captureIndex = 0;
        isCapturing=false;

        validCaptureMoves = getAllPossibleCaptures();

        if (!validCaptureMoves.isEmpty()){
            isCapturing = true;
            captureIndex = 0;
            for(Move[] move : validCaptureMoves){
                singleValidMoves.add(move[captureIndex]);
            }
        }
        else{
            validMoves = getAllPossibleMoves();
            singleValidMoves = validMoves.stream().map(m -> m[0]).collect(Collectors.toCollection(LinkedList::new));
        }
        return singleValidMoves;
    }

    LinkedList<Move> getValidMovesFromPlayerPerspectiveForSelectedPiece(int row, int col){
        LinkedList<Move> validMovesForSelectedPiece = new LinkedList<>();
        for (Move move : singleValidMoves){
            if (row == move.startRow && col == move.startCol){
                validMovesForSelectedPiece.add(move);
            }
        }
        return validMovesForSelectedPiece;
    }


//    long getAllPossibleMovesStartTime;
//    long passedTimeGetAllPossibleMoves = 0;
    LinkedList<Move[]> getAllPossibleMoves(){

//        getAllPossibleMovesStartTime = System.currentTimeMillis();

        LinkedList<Move[]> moves = new LinkedList<>();
        for (int rowIndex = 0; rowIndex < board.length; rowIndex++) {
            for (int colIndex = (rowIndex+1)%2; colIndex < board[rowIndex].length; colIndex+=2) {
                byte piece = board[rowIndex][colIndex];
                if (piece > 0 && whiteToMove || piece < 0 && !whiteToMove) {
                    boolean isPawn = Math.abs(board[rowIndex][colIndex]) == 1;
                    callMoveFunction(isPawn, rowIndex, colIndex, moves);

                }
            }
        }
//        passedTimeGetAllPossibleMoves += System.currentTimeMillis() - getAllPossibleMovesStartTime;

        return moves;
    }

//    long getAllPossibleCapturesStartTime;
//    long passedTimeGetAllPossibleCaptures = 0;
    LinkedList<Move[]> getAllPossibleCaptures(){
//        getAllPossibleCapturesStartTime = System.currentTimeMillis();

        LinkedList<Move[]> movesWithCaptures = new LinkedList<>();
        for (int rowIndex = 0; rowIndex < board.length; rowIndex++) {
            for (int colIndex = (rowIndex+1)%2; colIndex < board[rowIndex].length; colIndex+=2) {
                byte piece = board[rowIndex][colIndex];
                if (piece > 0 && whiteToMove || piece < 0 && !whiteToMove) {
                    boolean isPawn = Math.abs(board[rowIndex][colIndex]) == 1;
                    callCaptureFunction(isPawn, rowIndex, colIndex, movesWithCaptures);
                }
            }
        }
//        passedTimeGetAllPossibleCaptures += System.currentTimeMillis() - getAllPossibleCapturesStartTime;
        return movesWithCaptures;
    }

    boolean isGameOver(){



        LinkedList<Move[]> moves = getAllPossibleMoves();
        LinkedList<Move[]> movesWithCaptures = getAllPossibleCaptures();

        if (!moves.isEmpty() || !movesWithCaptures.isEmpty()){
            return false;
        }
        return true;
    }

    void callMoveFunction(boolean isPawn, int row, int col, LinkedList<Move[]> moves){
        if(isPawn){
            getManMoves(row, col, moves);
        }
        else {
            getKingMoves(row, col, moves);
        }
    }

    void callCaptureFunction(boolean isPawn, int row, int col, LinkedList<Move[]> movesWithCaptures){
        foundCaptureList.clear();
        if(isPawn){
            getManCaptures(row, col, movesWithCaptures);
        }
        else {
            getKingCaptures(row, col, movesWithCaptures);
        }
    }

    void getManMoves(int row, int col, LinkedList<Move[]> moves){
        int[][] directions = CheckersEngine.getMoveDirections(board[row][col]);
        for (int[] d : directions){
            int endRow = row + d[0];
            int endCol = col + d[1];
            if (CheckersEngine.isOnBoard(endRow, endCol) && board[endRow][endCol] == 0){
                moves.add(new Move[]{new Move(row, col, endRow, endCol, board, whiteToMove)});
            }
        }
    }

    void getKingMoves(int row, int col, LinkedList<Move[]> moves){
        int[][] directions = CheckersEngine.getMoveDirections(board[row][col]);
        for (int[] d : directions){
            for (int i = 1; i < 10; i++){
                int endRow = row + d[0] * i;
                int endCol = col + d[1] * i;
                if (CheckersEngine.isOnBoard(endRow, endCol)){
                    byte endPiece = board[endRow][endCol];
                    if(endPiece==0){
                        moves.add(new Move[]{new Move(row, col, endRow, endCol, board, whiteToMove)});
                    }else{
                        break;
                    }
                }else{
                    break;
                }
            }
        }

    }

//    long getManCapturesStartTime;
//    long passedTimeManCaptures = 0;
    void getManCaptures(int row, int col, LinkedList<Move[]> movesWithCaptures){
//        getManCapturesStartTime = System.currentTimeMillis();
        int[][] directions = CheckersEngine.getCaptureDirections();
        boolean anyFound = false;
        for (int[] d : directions){
            int endRow = row + 2 * d[0];
            int endCol = col + 2 * d[1];
            if(CheckersEngine.isOnBoard(endRow, endCol) && board[endRow][endCol] == 0){
                int nextRow = row + d[0];
                int nextCol = col + d[1];
                byte piece = board[nextRow][nextCol];
                if(piece<0 && whiteToMove || piece>0 && !whiteToMove){
                    anyFound = true;
                    Move tmpMove = new Move(row, col, endRow, endCol, board, whiteToMove, piece, nextRow, nextCol);
                    foundCaptureList.add(tmpMove);
                    makeMove(tmpMove, true);
                    getManCaptures(endRow, endCol, movesWithCaptures);
                    foundCaptureList.removeLast();
                    undoMove(true);
                }
            }
        }
        CheckersEngine.addInMovesWithCaptures(anyFound, foundCaptureList, movesWithCaptures);
//        passedTimeManCaptures += System.currentTimeMillis() - getManCapturesStartTime;
    }


//    long getKingCapturesStartTime;
//    long passedTimeKingCaptures = 0;
    void getKingCaptures(int row, int col, LinkedList<Move[]> movesWithCaptures){

//        getKingCapturesStartTime = System.currentTimeMillis();
        int[][] directions = CheckersEngine.getCaptureDirections();
        boolean anyFound = false;

        for (int[] d : directions){
            for (int i=1; i<9; i++){
                int capturedPieceRow = row + d[0] * i;
                int capturedPieceCol = col + d[1] * i;
                if (CheckersEngine.isOnBoard(capturedPieceRow, capturedPieceCol)){
                    byte piece = board[capturedPieceRow][capturedPieceCol];
                    if (piece==0)
                        continue;
                    else if(piece < 0 && whiteToMove || piece > 0 && !whiteToMove){
                        for (int j = i+1; j<10; j++){
                            int endRow = row + d[0] * j;
                            int endCol = col + d[1] * j;
                            if (CheckersEngine.isOnBoard(endRow, endCol) && board[endRow][endCol]==0){
                                anyFound = true;
                                Move tmpMove = new Move(row, col, endRow, endCol, board, whiteToMove, piece, capturedPieceRow, capturedPieceCol);
                                foundCaptureList.add(tmpMove);
                                makeMove(tmpMove, true);
                                getKingCaptures(endRow, endCol, movesWithCaptures);
                                foundCaptureList.removeLast();
                                undoMove(true);
                            }
                            else{
                                break;
                            }
                        }
                        break;
                    }
                    else{
                        break;
                    }
                }
                else{
                    break;
                }
            }
        }

        CheckersEngine.addInMovesWithCaptures(anyFound, foundCaptureList, movesWithCaptures);
//        passedTimeKingCaptures += System.currentTimeMillis() - getKingCapturesStartTime;
    }


    void changeTurn(){
        captureIndex = 0;
        validMoves.clear();
        validCaptureMoves.clear();
//        isCapturing = false;
        whiteToMove = !whiteToMove;

        if (moveLog.size() > 0){
            Move lastMove = moveLog.getLast();
            if (lastMove.isPawnPromotion){
                board[lastMove.endRow][lastMove.endCol] = (byte) (lastMove.pieceMoved > 0 ? 3 : -3);
            }
        }

    }




}
