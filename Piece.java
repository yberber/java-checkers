import java.util.HashMap;

public enum Piece {
        BLACK_KING((byte)-3),
        BLACK_MAN((byte)-1),
        WHITE_MAN((byte)1),
        WHITE_KING((byte)3),
        EMPTY((byte)0);

        public final byte value;
        Piece(byte value){
            this.value = value;
        }



//    static HashMap<Piece, Byte> pieceToByte = new HashMap<>();

//    enum Piece  {
//        BLACK_KING,
//        BLACK_MAN,
//        WHITE_MAN,
//        WHITE_KING,
//        EMPTY
//    }
}
