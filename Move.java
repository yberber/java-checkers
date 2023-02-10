import java.lang.reflect.Array;
import java.util.Objects;

public class Move {


    int dimension = 10;
    int startRow;
    int startCol;
    int endRow;
    int endCol;
    int pieceMoved;
    int capturedPiece;
    int capturedPieceRow;
    int capturedPieceCol;
    boolean isPawnPromotion;
    boolean isWhite;
    int moveId;


    public Move(int startRow, int startCol, int endRow, int endCol, int[][] board, boolean isWhite){
        this(startRow, startCol, endRow, endCol, board, isWhite, (int) 0, -1, -1);
    }

    public Move(int startRow, int startCol, int endRow, int endCol, int[][] board, boolean isWhite, int capturedPiece, int capturedPieceRow, int capturedPieceCol){
        this.startRow = startRow;
        this.startCol = startCol;
        this.endRow = endRow;
        this.endCol = endCol;
        this.isWhite = isWhite;
        this.pieceMoved = board[startRow][startCol];
        this.isPawnPromotion = this.pieceMoved==1 && this.endRow == 0 ||
                this.pieceMoved==-1 && this.endRow == 9;
        this.capturedPiece = capturedPiece;
        this.capturedPieceCol = capturedPieceCol;
        this.capturedPieceRow = capturedPieceRow;
//        moveId = CheckersEngine.getPieceId(this.capturedPiece) * 10000;
        moveId = this.startRow * 1000 + this.startCol * 100 + endRow * 10 + endCol;

    }

    public String getCheckersRowColNotation(){
        String sign = this.capturedPiece != 0 ? "x" : "-" ;
        return String.valueOf(startRow) + startCol + sign + endRow + endCol;
    }

    public String getNotationWhileCapturing(){
        return "x" + endRow +  endCol;
    }

    @Override
    public String toString() {
//        String resultString = String.format("(%d, %d) -> (%d, %d), type: %s", startRow, startCol, endRow, endCol, capturedPiece!="--"?"capture":"move" );
        String resultString = String.format("(%d, %d) -> (%d, %d)", startRow, startCol, endRow, endCol);
        if(capturedPiece!=0){
            resultString += String.format(", x(%s)", capturedPiece);
        }

        return resultString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return startRow == move.startRow && startCol == move.startCol && endRow == move.endRow && endCol == move.endCol ;

//        return startRow == move.startRow && startCol == move.startCol && endRow == move.endRow && endCol == move.endCol && Objects.equals(capturedPiece, move.capturedPiece);

    }

    @Override
    public int hashCode() {
        return Objects.hash(startRow, startCol, endRow, endCol, capturedPiece);
    }
}
