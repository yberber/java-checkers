import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CheckersUI extends JFrame implements MouseListener {
    private JPanel pan;
    Container mainPanel;
    GridLayout outerLayout = new GridLayout(10, 10);
    Container contentPane;

    Image blackMan;
    Image whiteMan;
    Image blackKing;
    Image whiteKing;

    JLabel[][] pieceSquares;

    GameState gs;

    boolean isRunning = false;

    CheckersAI ai ;


    int selection;
    int[] selectionSequence = new int[2];
    int selectionCounter = 0;

    private void readImages() throws IOException {
        whiteMan = ImageIO.read(new File("img/wm.png")).getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        blackMan = ImageIO.read(new File("img/bm.png")).getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        whiteKing = ImageIO.read(new File("img/wk.png")).getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        blackKing = ImageIO.read(new File("img/bk.png")).getScaledInstance(50, 50, Image.SCALE_SMOOTH);
    }

    private Image getBufferedImage(int pieceId) {
        if (pieceId == 1) {
            return whiteMan;
        } else if (pieceId == 3) {
            return whiteKing;
        } else if (pieceId == -1) {
            return blackMan;
        } else if (pieceId == -3) {
            return blackKing;
        } else {
            return null;
        }
    }

    public CheckersUI(String title, GameState gs, CheckersAI ai) throws IOException {
        super(title);
        this.gs = gs;
        pieceSquares = new JLabel[10][10];
this.ai = ai;
        readImages();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentPane = this.getContentPane();
        this.setSize(800, 800);
        contentPane.setLayout(outerLayout);
        initFields();
    }

    public void initFields() {

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                JLabel label = new JLabel();
                pieceSquares[i][j] = label;
                label.setSize(100, 100);
                label.setOpaque(true);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.CENTER);
                label.setName(i + String.valueOf(j));
//                if ((i + j) % 2 == 1)
                label.addMouseListener(this);
                if ((i + j) % 2 == 1) {
                    label.setBackground(Color.gray);
                }
                this.add(label);
            }
        }
    }

    public void UpdateFrame() {

        for (int i = 0; i < 10; i++) {
            for (int j = (i+1)%2; j < 10; j+=2) {
                if (gs.board[i][j] != 0) {
                    pieceSquares[i][j].setIcon(new ImageIcon(getBufferedImage(gs.board[i][j])));
                } else {
                    pieceSquares[i][j].setIcon(null);
                }
                pieceSquares[i][j].setBackground(Color.gray);
            }
        }
        this.getContentPane().repaint();

    }


    @Override
    public void mouseClicked(MouseEvent e) {
//        if(true){
//            return;
//        }
        if(!Main.isRunning || (gs.whiteToMove && !Main.isWhiteHuman || !gs.whiteToMove && !Main.isBlackHuman)){
            return;
        }
        Main.isRunning = true;
        JLabel label = (JLabel) e.getSource();
        int labelNameAsInt = Integer.parseInt(label.getName());
        System.out.println(label.getName());

        int row = labelNameAsInt / 10;
        int col = labelNameAsInt % 10;
        if (selectionCounter == 0) {
            for (Move move : gs.getValidMovesFromPlayerPerspective()) {
                if (move.startRow == row && move.startCol == col) {
                    selectionCounter = 1;
                    selection = labelNameAsInt;
                    higlightAfterPieceSelection(row, col);
//                    SwingUtilities.updateComponentTreeUI(this);
                    Main.isRunning = false;

                    return;
                }
            }
        }


            if (selectionCounter == 1) {
                if(selection == labelNameAsInt){
                    return;
                }
                boolean isValid = false;
                for (Move move : gs.getValidMovesFromPlayerPerspective()) {
                    if (move.startRow == row && move.startCol == col) {
                        selectionCounter = 1;
                        selection = labelNameAsInt;
                        removeHighlight();
                        higlightAfterPieceSelection(row, col);
//                        SwingUtilities.updateComponentTreeUI(this);
                        Main.isRunning = false;

                        return;
                    }
                    if (move.endRow == row && move.endCol == col) {
                        selectionCounter = 0;
                        isValid=true;
                        isRunning = true;
                        playMove(row, col);
                        selection = -1;
                        isRunning = false;
                        Main.isRunning = false;

                        return;
                    }

                }

                selectionCounter = 0;
                selection = -1;
                removeHighlight();
                Main.isRunning = false;

//                SwingUtilities.updateComponentTreeUI(this);


                return;

            }



    }

    private void removeHighlight() {

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                JLabel label = pieceSquares[i][j];
                if ((i + j) % 2 == 1) {
                    label.setBackground(Color.gray);
                }
            }
        }

    }

    private void playMove(int row, int col) {
        int startRow = selection/10;
        int startCol = selection%10;
        Move move = new Move(startRow, startCol, row, col, gs.board, gs.whiteToMove);
        gs.makeMoveWithUserInput(move);
        UpdateFrame();



    }

    private void higlightAfterPieceSelection(int row, int col) {
        for (Move move : gs.getValidMovesFromPlayerPerspective()) {
            if(move.startRow == row && move.startCol == col){
                pieceSquares[move.endRow][move.endCol].setBackground(Color.magenta);
            }
        }
        pieceSquares[row][col].setBackground(Color.green);


    }

        @Override
        public void mousePressed (MouseEvent e){

        }

        @Override
        public void mouseReleased (MouseEvent e){

        }

        @Override
        public void mouseEntered (MouseEvent e){

        }

        @Override
        public void mouseExited (MouseEvent e){

        }
    }



//    public void initFields()
//    {
//
//        pan = new JPanel[10][10];
//        for(int i=0; i<10; i++)
//        {
//            for(int j=0; j<10; j++)
//            {
//                pan[i][j] = new JPanel();
//                pan[i][j].setBackground(Color.WHITE);
//                pan[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
//                pan[i][j].setPreferredSize(new Dimension(25,25));
//                wrapper1.add(pan[i][j]);
//            }
//        }
//
//        pan2 = new JPanel[10][10];
//        for(int i=0; i<10; i++)
//        {
//            for(int j=0; j<10; j++)
//            {
//                pan2[i][j] = new JPanel();
//                pan2[i][j].setBackground(Color.WHITE);
//                pan2[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
//                pan2[i][j].setPreferredSize(new Dimension(25,25));
//                wrapper2.add(pan[i][j]);
//            }
//        }
//    }

//    public static void main(String[] args) throws IOException {
//        JFrame frame = new CheckersUI("Checkers");
//        frame.setVisible(true);
//
//    }

