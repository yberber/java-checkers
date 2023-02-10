import java.util.LinkedList;

public class VisitedMoveAndNode {

    public Move[] move;
    public TreeNode child;

    public VisitedMoveAndNode(Move[] move, TreeNode child) {
        this.move = move;
        this.child = child;
    }
}
