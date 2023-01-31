import java.util.ArrayList;
import java.util.LinkedList;

public class GameState {

    LinkedList<Move> validMoves;
    LinkedList<LinkedList<Move>> validCaptureMoves;

    LinkedList<Move> singleValidMoves;
    String[][] board;
    boolean whiteToMove;
    LinkedList<Move> moveLog;
    boolean isCapturing;
    int captureIndex;


    public GameState(){
        validMoves = new LinkedList<>();
        validCaptureMoves = new LinkedList<>();
        singleValidMoves = new LinkedList<>();


        board = new String[][]{
                {"--", "bm", "--", "bm", "--", "bm", "--", "bm", "--", "bm"},
                {"bm", "--", "bm", "--", "bm", "--", "bm", "--", "bm", "--"},
                {"--", "bm", "--", "bm", "--", "bm", "--", "bm", "--", "bm"},
                {"bm", "--", "bm", "--", "bm", "--", "bm", "--", "bm", "--"},

                {"--", "--", "--", "--", "--", "--", "--", "--", "--", "--"},
                {"--", "--", "--", "--", "--", "--", "--", "--", "--", "--"},

                {"--", "wm", "--", "wm", "--", "wm", "--", "wm", "--", "wm"},
                {"wm", "--", "wm", "--", "wm", "--", "wm", "--", "wm", "--"},
                {"--", "wm", "--", "wm", "--", "wm", "--", "wm", "--", "wm"},
                {"wm", "--", "wm", "--", "wm", "--", "wm", "--", "wm", "--"}
        };



        whiteToMove = true;
        moveLog = new LinkedList<>();
        isCapturing = false;
        captureIndex = 0;
    }

    void makeMove(Move move) {
        makeMove(move, false);
    }

    void makeMove(Move move, boolean searchingMode){
        board[move.startRow][move.startCol] = "--";
        board[move.endRow][move.endCol] = move.pieceMoved;
        if (!move.capturedPiece.equals("--")){
            int row = move.capturedPieceRow;
            int col = move.capturedPieceCol;
            board[row][col] = "--";
        }
        moveLog.add(move);

        if (!searchingMode){
            if (!isCapturing){
                changeTurn();
            }
            else{
                if(captureIndex >= validCaptureMoves.get(0).size()){
                    changeTurn();
                }
                else{
                    for (int index = validCaptureMoves.size()-1; index >= 0; index--){
                        if (moveLog.getLast() != validCaptureMoves.get(index).get(captureIndex-1))
                            validCaptureMoves.remove(index);
                    }
                }
            }
        }
    }

    void makeMoveExtended(Move singleMove){
        makeMove(singleMove, true);
        changeTurn();
    }

    void makeMoveExtended(LinkedList<Move> multipleMove){
        for (Move move: multipleMove) {
            makeMove(move, true);
        }
        changeTurn();
    }

    void makeMoveExtended(Move singleMove, LinkedList<Move> multipleMove){
        if(singleMove != null){
            makeMoveExtended(singleMove);
        }
        else if(multipleMove != null){
            makeMoveExtended(multipleMove);

        }

    }


    void undoMove(){
        undoMove(false);
    }
    void undoMove(boolean onlyOne){
        if (moveLog.size() > 0){
            while(true){
                Move move = moveLog.removeLast();
                board[move.startRow][move.startCol] = move.pieceMoved;
                board[move.endRow][move.endCol] = "--";
                if (move.capturedPiece != "--"){
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
        if (!isCapturing)
            validCaptureMoves = getAllPossibleCaptures();

        singleValidMoves.clear();
        if (isCapturing){
            for(LinkedList<Move> moveSquence : validCaptureMoves){
                singleValidMoves.add((moveSquence.get(captureIndex)));
            }
            captureIndex++;
        }
        else{
            validMoves = getAllPossibleMoves();
            singleValidMoves = validMoves;
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


    LinkedList<Move> getAllPossibleMoves(){
        LinkedList<Move> moves = new LinkedList<>();
        for (int rowIndex = 0; rowIndex < board.length; rowIndex++) {
            for (int colIndex = 0; colIndex < board[rowIndex].length; colIndex++) {
                char turnSign = board[rowIndex][colIndex].charAt(0);
                if (turnSign == 'w' && whiteToMove || turnSign == 'b' && !whiteToMove) {
                    boolean isPawn = board[rowIndex][colIndex].charAt(1) == 'm';
                    callMoveFunction(isPawn, rowIndex, colIndex, moves);

                }
            }
        }
        return moves;
    }

    LinkedList<LinkedList<Move>> getAllPossibleCaptures(){
        LinkedList<LinkedList<Move>> movesWithCaptures = new LinkedList<>();
        for (int rowIndex = 0; rowIndex < board.length; rowIndex++) {
            for (int colIndex = 0; colIndex < board[rowIndex].length; colIndex++) {
                char turnSign = board[rowIndex][colIndex].charAt(0);
                if (turnSign == 'w' && whiteToMove || turnSign == 'b' && !whiteToMove) {
                    boolean isPawn = board[rowIndex][colIndex].charAt(1) == 'm';
                    callCaptureFunction(isPawn, rowIndex, colIndex, movesWithCaptures);
                }
            }
        }

        return movesWithCaptures;
    }

    boolean isGameOver(){
        LinkedList<Move> moves = new LinkedList<>();
        LinkedList<LinkedList<Move>> movesWithCaptures = new LinkedList<>();

        for(int row = 0; row < board.length; row++){
            for (int col = 0; col < board[row].length; col++){
                char turnSign = board[row][col].charAt(0);
                if (turnSign == 'w' && whiteToMove || turnSign == 'b' && !whiteToMove){
                    boolean isPawn = board[row][col].charAt(1) == 'm';
                    callMoveFunction(isPawn, row, col, moves);
                    callCaptureFunction(isPawn, row, col, movesWithCaptures);

                    if (!moves.isEmpty() || !movesWithCaptures.isEmpty())
                        return false;
                }
            }
        }
        return true;
    }

    void callMoveFunction(boolean isPawn, int row, int col, LinkedList<Move> moves){
        if(isPawn){
            getManMoves(row, col, moves);
        }
        else {
            getKingMoves(row, col, moves);
        }
    }

    void callCaptureFunction(boolean isPawn, int row, int col, LinkedList<LinkedList<Move>> movesWithCaptures){
        if(isPawn){
            getManCaptures(row, col, movesWithCaptures);
        }
        else {
            getKingCaptures(row, col, movesWithCaptures);
        }
    }

    void getManMoves(int row, int col, LinkedList<Move> moves){
        int[][] directions = CheckersEngine.getMoveDirections(board[row][col]);
        for (int[] d : directions){
            int endRow = row + d[0];
            int endCol = col + d[1];
            if (CheckersEngine.isOnBoard(endRow, endCol) && board[endRow][endCol] == "--"){
                moves.add(new Move(row, col, endRow, endCol, board, whiteToMove));
            }
        }
    }

    void getKingMoves(int row, int col, LinkedList<Move> moves){
        int[][] directions = CheckersEngine.getMoveDirections(board[row][col]);
        for (int[] d : directions){
            for (int i = 1; i < 10; i++){
                int endRow = row + d[0] * i;
                int endCol = col + d[1] * i;
                if (CheckersEngine.isOnBoard(endRow, endCol)){
                    String endPiece = board[endRow][endCol];
                    if(endPiece.equals("--")){
                        moves.add(new Move(row, col, endRow, endCol, board, whiteToMove));
                    }else{
                        break;
                    }
                }else{
                    break;
                }
            }
        }

    }

    void getManCaptures(int row, int col, LinkedList<LinkedList<Move>> movesWithCaptures){
        char enemyColor = whiteToMove ? 'b' : 'w';
        getManCaptures(row, col, movesWithCaptures, enemyColor);
    }
    void getManCaptures(int row, int col, LinkedList<LinkedList<Move>> movesWithCaptures, char enemyColor){
        int[][] directions = CheckersEngine.getCaptureDirections();
        boolean anyFound = false;
        LinkedList<Move> foundCaptureList = new LinkedList<>();
        for (int[] d : directions){
            int endRow = row + 2 * d[0];
            int endCol = col + 2 * d[1];
            if(CheckersEngine.isOnBoard(endRow, endCol) && board[endRow][endCol] == "--"){
                int nextRow = row + d[0];
                int nextCol = col + d[1];
                String piece = board[nextRow][nextCol];
                if(piece.charAt(0) == enemyColor){
                    anyFound = true;
                    isCapturing = true;
                    Move tmpMove = new Move(row, col, endRow, endCol, board, whiteToMove, piece, nextRow, nextCol);
                    foundCaptureList.add(tmpMove);
                    makeMove(tmpMove, true);
                    getManCaptures(endRow, endCol, movesWithCaptures, enemyColor, foundCaptureList);
                    foundCaptureList.removeLast();
                    undoMove(true);
                }
            }
        }
        CheckersEngine.addInMovesWithCaptures(anyFound, foundCaptureList, movesWithCaptures);
    }
    void getManCaptures(int row, int col, LinkedList<LinkedList<Move>> movesWithCaptures, char enemyColor, LinkedList<Move> foundCaptureList){
        int[][] directions = CheckersEngine.getCaptureDirections();
        boolean anyFound = false;
        for (int[] d : directions){
            int endRow = row + 2 * d[0];
            int endCol = col + 2 * d[1];
            if(CheckersEngine.isOnBoard(endRow, endCol) && board[endRow][endCol] == "--"){
                int nextRow = row + d[0];
                int nextCol = col + d[1];
                String piece = board[nextRow][nextCol];
                if(piece.charAt(0) == enemyColor){
                    anyFound = true;
                    isCapturing = true;
                    Move tmpMove = new Move(row, col, endRow, endCol, board, whiteToMove, piece, nextRow, nextCol);
                    foundCaptureList.add(tmpMove);
                    makeMove(tmpMove, true);
                    getManCaptures(endRow, endCol, movesWithCaptures, enemyColor, foundCaptureList);
                    foundCaptureList.removeLast();
                    undoMove(true);
                }
            }
        }
        CheckersEngine.addInMovesWithCaptures(anyFound, foundCaptureList, movesWithCaptures);
    }

    void getKingCaptures(int row, int col, LinkedList<LinkedList<Move>> movesWithCaptures){
        char enemyColor = whiteToMove ? 'b' : 'w';
        getKingCaptures(row, col, movesWithCaptures, enemyColor);
    }
    void getKingCaptures(int row, int col, LinkedList<LinkedList<Move>> movesWithCaptures, char enemyColor){
        int[][] directions = CheckersEngine.getCaptureDirections();
        boolean anyFound = false;
        LinkedList<Move> foundCaptureList = new LinkedList<>();
        for (int[] d : directions){
            for (int i=1; i<9; i++){
                int capturedPieceRow = row + d[0] * i;
                int capturedPieceCol = col + d[1] * i;
                if (CheckersEngine.isOnBoard(capturedPieceRow, capturedPieceCol)){
                    String piece = board[capturedPieceRow][capturedPieceCol];
                    if (piece.equals("--"))
                        continue;
                    else if(piece.charAt(0) == enemyColor){
                        for (int j = i+1; j<10; j++){
                            int endRow = row + d[0] * j;
                            int endCol = col + d[1] * j;
                            if (CheckersEngine.isOnBoard(endRow, endCol) && board[endRow][endCol].equals("--")){
                                anyFound = true;
                                isCapturing = true;
                                Move tmpMove = new Move(row, col, endRow, endCol, board, whiteToMove, piece, capturedPieceRow, capturedPieceCol);
                                foundCaptureList.add(tmpMove);
                                makeMove(tmpMove, true);
                                getKingCaptures(endRow, endCol, movesWithCaptures, enemyColor, foundCaptureList);
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

    }
    void getKingCaptures(int row, int col, LinkedList<LinkedList<Move>> movesWithCaptures, char enemyColor, LinkedList<Move> foundCaptureList){

        int[][] directions = CheckersEngine.getCaptureDirections();
        boolean anyFound = false;
        for (int[] d : directions){
            for (int i=1; i<9; i++){
                int capturedPieceRow = row + d[0] * i;
                int capturedPieceCol = col + d[1] * i;
                if (CheckersEngine.isOnBoard(capturedPieceRow, capturedPieceCol)){
                    String piece = board[capturedPieceRow][capturedPieceCol];
                    if (piece.equals("--"))
                        continue;
                    else if(piece.charAt(0) == enemyColor){
                        for (int j = i+1; j<10; j++){
                            int endRow = row + d[0] * j;
                            int endCol = col + d[1] * j;
                            if (CheckersEngine.isOnBoard(endRow, endCol) && board[endRow][endCol].equals("--")){
                                anyFound = true;
                                isCapturing = true;
                                Move tmpMove = new Move(row, col, endRow, endCol, board, whiteToMove, piece, capturedPieceRow, capturedPieceCol);
                                foundCaptureList.add(tmpMove);
                                makeMove(tmpMove, true);
                                getKingCaptures(endRow, endCol, movesWithCaptures, enemyColor, foundCaptureList);
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
    }


    void changeTurn(){
        captureIndex = 0;
        validMoves.clear();
        validCaptureMoves.clear();
        isCapturing = false;
        whiteToMove = !whiteToMove;

        if (moveLog.size() > 0){
            Move lastMove = moveLog.getLast();
            if (lastMove.isPawnPromotion){
                board[lastMove.endRow][lastMove.endCol] = lastMove.pieceMoved.substring(0,1) + "k";
            }
        }

    }




}
