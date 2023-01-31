import java.util.LinkedList;

public class CheckersAI {


    Move nextMove;
    LinkedList<Move> nextCapture;

    int DEPTH;
    int counter;
    long currentTime;
    long startTime;
    int score;
    int moveCounter;
    boolean isWhite;



    Move findBestMoveMinMax(GameState gs, int depth){
        isWhite = gs.whiteToMove;
        moveCounter+=1;
        counter=0;
        score=0;
        DEPTH=depth;
        nextMove=null;
        nextCapture=null;

        currentTime=System.currentTimeMillis();

        findMoveMinMaxAlphaBetaImproved(gs, depth, -255, 255);

        System.out.println("turn: " + (gs.whiteToMove ? "white": "black") + ", counter: " + counter + ", score: " +
                score + ". used time: " + (System.currentTimeMillis() - currentTime)/1000.0 + ", used total time: " +
                (System.currentTimeMillis() - this.startTime)/1000.0 + ", total generated move count: " + moveCounter);


        return nextMove;

    }

    private int findMoveMinMaxAlphaBetaImproved(GameState gs, int depth, int alpha, int beta) {

        boolean isCapturing;
        LinkedList<LinkedList<Move>> possibleCapturesExtended = new LinkedList<>();
        LinkedList<Move> possibleMoveExtended = new LinkedList<>();
        if(depth==0){
            counter++;
            possibleCapturesExtended = gs.getAllPossibleCaptures();
            if (possibleCapturesExtended.size() > 0){
                isCapturing=true;
                depth++;
            }
            else{
                if(isWhite){
                    return scoreMaterial(gs.board);
                }
                else{
                    return scoreMaterial(gs.board);
                }
            }
        }
        else{
            possibleCapturesExtended = gs.getAllPossibleCaptures();
            possibleMoveExtended = gs.getAllPossibleMoves();
            if(possibleCapturesExtended.size() == 0 && possibleMoveExtended.size() == 0){
                counter++;
                return gs.whiteToMove ? -100 : 100;
            }

            if(possibleCapturesExtended.size() > 0){
                isCapturing=true;
            }else{
                isCapturing=false;
            }
        }

        if (depth==DEPTH){
            if(isCapturing && possibleCapturesExtended.size() == 1){
                counter++;
                nextCapture=possibleCapturesExtended.get(0);
                return 0;
            }
        }

        if (gs.whiteToMove){
            int maxScore = -255;
            if(isCapturing){
                for(LinkedList<Move> capturingMove : possibleCapturesExtended) {
                    gs.makeMoveExtended(capturingMove);
                    score = findMoveMinMaxAlphaBetaImproved(gs, depth - 1, alpha, beta);
                    if (score > maxScore) {
                        maxScore = score;
                        alpha = Math.max(alpha, maxScore);
                        if (alpha >= beta) {
                            gs.undoMove();
                            break;
                        }
                        if (depth == DEPTH) {
                            nextCapture = capturingMove;
                        }
                    }
                    gs.undoMove();
                }
                return  maxScore;
            }
            else{
                for(Move singleMove : possibleMoveExtended) {
                    gs.makeMoveExtended(singleMove);
                    score = findMoveMinMaxAlphaBetaImproved(gs, depth - 1, alpha, beta);
                    if (score > maxScore) {
                        maxScore = score;
                        alpha = Math.max(alpha, maxScore);
                        if (alpha >= beta) {
                            gs.undoMove();
                            break;
                        }
                        if (depth == DEPTH) {
                            nextMove = singleMove;
                        }
                    }
                    gs.undoMove();
                }
                return  maxScore;
            }
        }


        else{
            int minScore = 255;
            if(isCapturing){
                for(LinkedList<Move> capturingMove : possibleCapturesExtended) {
                    gs.makeMoveExtended(capturingMove);
                    score = findMoveMinMaxAlphaBetaImproved(gs, depth - 1, alpha, beta);
                    if (score < minScore) {
                        minScore = score;
                        beta = Math.min(beta, minScore);
                        if (alpha >= beta) {
                            gs.undoMove();
                            break;
                        }
                        if (depth == DEPTH) {
                            nextCapture = capturingMove;
                        }
                    }
                    gs.undoMove();
                }
                return  minScore;
            }
            else{
                for(Move singleMove : possibleMoveExtended) {
                    gs.makeMoveExtended(singleMove);
                    score = findMoveMinMaxAlphaBetaImproved(gs, depth - 1, alpha, beta);
                    if (score < minScore) {
                        minScore = score;
                        beta = Math.min(beta, minScore);
                        if (alpha >= beta) {
                            gs.undoMove();
                            break;
                        }
                        if (depth == DEPTH) {
                            nextMove = singleMove;
                        }
                    }
                    gs.undoMove();
                }
                return  minScore;
            }
        }
    }


    int scoreMaterial(String[][] board){
        int score = 0;
        for (String[] row : board){
            for(String square : row){
                if (square.equals("wm")){
                    score+=1;
                }
                else if (square.equals("wk")){
                    score+=3;
                }
                else if (square.equals("bm")){
                    score-=1;
                }
                else if (square.equals("bk")){
                    score-=3;
                }
            }
        }
        return score;
    }
}
