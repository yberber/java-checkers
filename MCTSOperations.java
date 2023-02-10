

public class MCTSOperations {

    public static double uctValue(TreeNode none, TreeNode parent){
        double value = none.M + 1.1412 * Math.sqrt(Math.log(parent.V) / none.V);
        return value;
    }

    public static TreeNode select (TreeNode node) throws Exception {
        if(node.isMCTSLeafNode() || node.isTerminalNode()){
            return node;
        }
        else{
            TreeNode maxUctChild = null;
            double maxUctValue = Double.MIN_VALUE;
            for (VisitedMoveAndNode moveAndNode : node.visitedMovesAndNodes){
                double uctValChild = uctValue(moveAndNode.child, node);
                if(uctValChild > maxUctValue){
                    maxUctChild = moveAndNode.child;
                    maxUctValue = uctValChild;
                }
            }
            if(maxUctChild == null){
                throw new Exception("Could not identify child with best uct value");
            }
            else{
                return select(maxUctChild);
            }
        }
    }


    public static TreeNode expand(TreeNode node){
        Move[] moveToExpand = node.nonVisitedLegalMoves.removeLast();
        int[][] board = node.gs.board.clone();
        node.gs.makeMoveExtended(moveToExpand);
        TreeNode childNode = new TreeNode(node.gs);
        childNode.parent = node;
        node.visitedMovesAndNodes.add((new VisitedMoveAndNode(moveToExpand, childNode)));
        return childNode;
    }


    public static void backpropagate(TreeNode node, double payout){
        node.M = ((node.M * node.V) + payout) / (node.V + 1);
        node.V = node.V + 1;
        if(node.parent != null){
            backpropagate(node.parent, payout);
        }
    }
}
