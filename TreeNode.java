import com.sun.source.tree.Tree;

import java.util.LinkedList;

public class TreeNode {


    double M;
    double V;
    LinkedList<VisitedMoveAndNode> visitedMovesAndNodes;
    LinkedList<Move[]> nonVisitedLegalMoves;
    GameState gs;
    TreeNode parent;

    public TreeNode(GameState gs){
        M = 0;
        V = 0;
        visitedMovesAndNodes = new LinkedList<>();
        nonVisitedLegalMoves = new LinkedList<>();
        this.gs = gs;
        parent = null;

        for (Move[] move : gs.getValidMoves()){
            nonVisitedLegalMoves.add(move);
        }
    }

    public boolean isMCTSLeafNode(){
        return nonVisitedLegalMoves.size() != 0;
    }

    public boolean isTerminalNode(){
        return nonVisitedLegalMoves.size() == 0 && visitedMovesAndNodes.size() == 0;
    }

}
