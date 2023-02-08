import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

public class CheckersAI {


//    Move[] nextMove;
//    Move[] nextCapture;

    Move[] nextMoveOrCapturingMove;

    int DEPTH;
    int counter;
    long currentTime;
    long startTime;
    float score;
    int moveCounter;
    boolean isWhite;
    boolean running = false;

    Move[] findBestMoveMinMax(GameState gs, int depth){
        isWhite = gs.whiteToMove;
        moveCounter+=1;
        counter=0;
        score=0;
        DEPTH=depth;
        nextMoveOrCapturingMove=null;

        currentTime=System.currentTimeMillis();
        running = true;
        findMoveMinMaxAlphaBetaImproved(gs, depth, -255, 255);
        running=false;
        System.out.println("turn: " + (gs.whiteToMove ? "white": "black") + ", counter: " + counter + ", score: " +
                score + ". used time: " + (System.currentTimeMillis() - currentTime)/1000.0 + ", used total time: " +
                (System.currentTimeMillis() - this.startTime)/1000.0 + ", total generated move count: " + moveCounter);


        return nextMoveOrCapturingMove;

    }



        private float findMoveMinMaxAlphaBetaImproved(GameState gs, int depth, float alpha, float beta) {

        LinkedList<Move[]> possibleMovesCapturesExtended;

        if(depth==0){
//                        counter++;
//
//            return scoreMaterial(gs.board);

            counter++;
//            return scoreMaterial(gs.board);

            possibleMovesCapturesExtended = gs.getAllPossibleCaptures();
            if (possibleMovesCapturesExtended.size() > 0){
                depth++;
            }
            else{
                return scoreMaterial(gs.board);
//                if(isWhite)
//                    return scoreMaterialHeuristic(gs.board);
//                else
//                    return scoreMaterialHeuristic(gs.board);
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

//        if (depth==DEPTH){
//            if(possibleMovesCapturesExtended.size() == 1){
//                counter++;
//                nextMoveOrCapturingMove=possibleMovesCapturesExtended.get(0);
//                return;
//            }
//            Collections.shuffle(possibleMovesCapturesExtended);
//        }

        if (gs.whiteToMove) {
            float maxScore = -255;
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
            float minScore = 255;
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


    int scoreMaterial(byte[][] board){
        int score = 0;
        for (byte[] row : board){
            for(byte square : row){
                score += square;
//                if (square.equals("wm")){
//                    score+=1;
//                }
//                else if (square.equals("wk")){
//                    score+=3;
//                }
//                else if (square.equals("bm")){
//                    score-=1;
//                }
//                else if (square.equals("bk")){
//                    score-=3;
//                }
            }
        }
        return score;
    }

    float scoreMaterialHeuristic(byte[][] board){
        float score = 0;
        byte scoreOfPiece;
        for (int row = 0; row < board.length; row++){
            for(int col = 0; col < board[row].length ; col++){
                scoreOfPiece = board[row][col];
                if (scoreOfPiece==-1){
                    score -= row / 16.0f ;
                }
                else if (scoreOfPiece==1){
                    score += (9-row)/16.0f;
                }

                score += scoreOfPiece;
            }
        }
        return score;
    }
}
