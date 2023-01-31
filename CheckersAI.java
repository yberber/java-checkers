import java.util.LinkedList;

public class CheckersAI {


//    Move[] nextMove;
//    Move[] nextCapture;

    Move[] nextMoveOrCapturingMove;

    int DEPTH;
    int counter;
    long currentTime;
    long startTime;
    int score;
    int moveCounter;
    boolean isWhite;



    Move[] findBestMoveMinMax(GameState gs, int depth){
        isWhite = gs.whiteToMove;
        moveCounter+=1;
        counter=0;
        score=0;
        DEPTH=depth;
        nextMoveOrCapturingMove=null;


        currentTime=System.currentTimeMillis();

        findMoveMinMaxAlphaBetaImproved(gs, depth, -255, 255);

        System.out.println("turn: " + (gs.whiteToMove ? "white": "black") + ", counter: " + counter + ", score: " +
                score + ". used time: " + (System.currentTimeMillis() - currentTime)/1000.0 + ", used total time: " +
                (System.currentTimeMillis() - this.startTime)/1000.0 + ", total generated move count: " + moveCounter);


        return nextMoveOrCapturingMove;

    }


//    Move[] findBestMoveMinMaxAndPerform(GameState gs, int depth){
//
//    }


        private int findMoveMinMaxAlphaBetaImproved(GameState gs, int depth, int alpha, int beta) {

        LinkedList<Move[]> possibleMovesCapturesExtended;

        if(depth==0){
            counter++;
            possibleMovesCapturesExtended = gs.getAllPossibleCaptures();
            if (possibleMovesCapturesExtended.size() > 0){
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
            possibleMovesCapturesExtended = gs.getAllPossibleCaptures();
            if(possibleMovesCapturesExtended.isEmpty()){
                possibleMovesCapturesExtended = gs.getAllPossibleMoves();
                if(possibleMovesCapturesExtended.isEmpty()){
                    counter++;
                    return gs.whiteToMove ? -100 : 100;
                }
            }
        }

        if (depth==DEPTH){
            if(possibleMovesCapturesExtended.size() == 1){
                counter++;
                nextMoveOrCapturingMove=possibleMovesCapturesExtended.get(0);
                return 0;
            }
        }

        if (gs.whiteToMove) {
            int maxScore = -255;
            for (Move[] moveOrCapturingMove : possibleMovesCapturesExtended) {
                gs.makeMoveExtended(moveOrCapturingMove);
                score = findMoveMinMaxAlphaBetaImproved(gs, depth - 1, alpha, beta);
                if (score > maxScore) {
                    maxScore = score;
                    alpha = Math.max(alpha, maxScore);
                    if (alpha >= beta) {
                        gs.undoMove();
                        break;
                    }
                    if (depth == DEPTH) {
                        nextMoveOrCapturingMove = moveOrCapturingMove;
                    }
                }
                gs.undoMove();
            }
            return maxScore;
        }


        else{
            int minScore = 255;
            for(Move[] moveOrCapturingMove : possibleMovesCapturesExtended) {
                gs.makeMoveExtended(moveOrCapturingMove);
                score = findMoveMinMaxAlphaBetaImproved(gs, depth - 1, alpha, beta);
                if (score < minScore) {
                    minScore = score;
                    beta = Math.min(beta, minScore);
                    if (alpha >= beta) {
                        gs.undoMove();
                        break;
                    }
                    if (depth == DEPTH) {
                        nextMoveOrCapturingMove = moveOrCapturingMove;
                    }
                }
                gs.undoMove();
            }
            return  minScore;

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
