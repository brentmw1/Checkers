/**
 *
 * @author Mark Monarch
 */

import java.util.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.scene.paint.*;

public class CheckersGame extends Application
{
    final int MAX_TILE_SIZE = 80;
    final int PREF_TILE_SIZE = 40;
    final int MIN_TILE_SIZE = 32;
    
    private ArrayList<CheckersPiece> pieces;    //The checkers pieces currently in the game
    private CheckersBoard board;                 //The checkers board for the game
    private Stage primaryStage;
    private ToolBar topToolBar;
    private Pane gameBoard;
    private VBox gameControlPane;
    private VBox rightPane;
    private HBox infoPane;
    
    /*
    *Creates a standard checkers game with 12 black and 12 red checkers pieces
    */
    public CheckersGame() {
        pieces = new ArrayList<CheckersPiece>(0);
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                if (i != 4 && i != 5) {
                    if (i%2 == 1 && j%2 == 1) {
                        pieces.add(new CheckersPiece(i < 4, false, i, j));
                    } else if (i%2 == 0 && j%2 == 0) {
                        pieces.add(new CheckersPiece(i < 4, false, i, j));
                    }
                }
            }
        }
        board = new CheckersBoard(pieces);
    }
    
    /*
    *Creates a checkers game with an array of checkers pieces
    *Parameters: pieces is an arraylist of checkers pieces
    */
    public CheckersGame(ArrayList<CheckersPiece> pieces) {
        this.pieces = pieces;
        board = new CheckersBoard(this.pieces);
    }
    
    public boolean isMovePossible(boolean isRedTurn) {
        int row,
            column;
        if (isJumpPossible(isRedTurn, 0) > -1) {
            return true;
        }
        for (int i = 0; i < pieces.size(); i++) {
            row = pieces.get(i).getRow();
            column = pieces.get(i).getColumn();
            if (pieces.get(i).isRed() == isRedTurn) {
                if ((pieces.get(i).isRed() || pieces.get(i).isKing()) && row < 8) {
                    if (column > 1 && findPieceID(row+1, column-1) == -1) {
                        return true;
                    }
                    if (column < 8 && findPieceID(row+1, column+1) == -1) {
                        return true;
                    }
                }
                if ((!pieces.get(i).isRed() || pieces.get(i).isKing()) && row > 1) {
                    if (column > 1 && findPieceID(row-1, column-1) == -1) {
                        return true;
                    } else if (column < 8 && findPieceID(row-1, column+1) == -1) {
                        return true;
                    }
                }
            }    
        }
        return false;
    }
    
    public boolean isGameWon(boolean isRedTurn) {
        boolean isBlack = false,
                isRed = false;
        for (int i = 0; i < pieces.size(); i++) {
            if (pieces.get(i).isRed()) {
                isRed = true;
            } else if (!pieces.get(i).isRed()) {
                isBlack = true;
            }
            if (isRed && isBlack) {
                i = pieces.size();
            }
        }
        if (!isRed || !isBlack) {
            return true;
        }
        if (!isMovePossible(isRedTurn)) {
            return true;
        }
        return false;
    }
    
    /*
    *Finds the index of a checkers piece from its location
    *Parameters: row is the row of the piece 1-8, column is the column of the piece 1-8
    *Return: returns the index of the piece, or -1 if no piece is there
    */
    public int findPieceID(int row, int column) {
        for (int i = 0; i < pieces.size(); i++) {
            if (pieces.get(i).getRow() == row && pieces.get(i).getColumn() == column) {
                return i;
            }
        }
        return -1;
    }
    
    /*
    *Simulates a players turn
    *Parameters: isRedTurn indicates whose turn, true for red, false for black
    */
    public void turn(boolean isRedTurn) {
        Scanner in = new Scanner(System.in);
        int fromRow,
            fromColumn,
            toRow,
            toColumn,
            mandatoryPieceID;
        mandatoryPieceID = -1;
        boolean isValid;
        isValid = false;
        while (!isValid) {
            fromRow = in.nextInt();
            fromColumn = in.nextInt();
            toRow = in.nextInt();
            toColumn = in.nextInt();
            if (isValidMove(fromRow, fromColumn, toRow, toColumn, isRedTurn) && (mandatoryPieceID == -1 || mandatoryPieceID == findPieceID(fromRow, fromColumn))) {
                isValid = true;
                pieces.get(findPieceID(fromRow, fromColumn)).setRow(toRow);
                pieces.get(findPieceID(toRow, fromColumn)).setColumn(toColumn);
                if (toRow == 1 && !pieces.get(findPieceID(toRow, toColumn)).isRed()) {
                     pieces.get(findPieceID(toRow, toColumn)).setKing(true);
                } else if (toRow == 8 && pieces.get(findPieceID(toRow, toColumn)).isRed()) {
                    pieces.get(findPieceID(toRow, toColumn)).setKing(true);
                }
                board.movePiece(fromRow, fromColumn, toRow, toColumn);
                if (Math.abs(toRow-fromRow) == 2) {   
                    pieces.remove(findPieceID(fromRow+(toRow-fromRow)/2, fromColumn+(toColumn-fromColumn)/2));
                    board.removePiece(fromRow-(fromRow-toRow)/2, fromColumn-(fromColumn-toColumn)/2);
                    if (isJumpPossible(isRedTurn, findPieceID(toRow, toColumn)) == findPieceID(toRow, toColumn)) {
                        isValid = false;
                        mandatoryPieceID = findPieceID(toRow, toColumn);
                        board.printBoard();
                    }
                }
            }
        }
    }
    
    /*
    *Checks the board to see if a jump is possible for a certain player
    *Parameters: isRedTurn indicates whose turn it is, true for red, false for black, startPoint indicates where in pieces to begin the search
    *Return: returns the index of a possible jump, or -1 if no jump is possible
    */
    public int isJumpPossible(boolean isRedTurn, int startPoint) {
        int row,
            column;
        for (int i = startPoint; i < pieces.size(); i++) {
            row = pieces.get(i).getRow();
            column = pieces.get(i).getColumn();
            if (pieces.get(i).isRed() == isRedTurn) {
                if ((pieces.get(i).isRed() || pieces.get(i).isKing()) && row < 7) {
                    if (column > 2 && findPieceID(row+1, column-1) > -1 && pieces.get(findPieceID(row+1, column-1)).isRed() != isRedTurn && findPieceID(row+2, column-2) == -1) {
                        return i;
                    }
                    if (column < 7 && findPieceID(row+1, column+1) > -1 && pieces.get(findPieceID(row+1, column+1)).isRed() != isRedTurn && findPieceID(row+2, column+2) == -1) {
                        return i;
                    }
                }
                if ((!pieces.get(i).isRed() || pieces.get(i).isKing()) && row > 2) {
                    if (column > 2 && findPieceID(row-1, column-1) > -1 && pieces.get(findPieceID(row-1, column-1)).isRed() != isRedTurn && findPieceID(row-2, column-2) == -1) {
                        return i;
                    } else if (column < 7 && findPieceID(row-1, column+1) > -1 && pieces.get(findPieceID(row-1, column+1)).isRed() != isRedTurn && findPieceID(row-2, column+2) == -1) {
                        return i;
                    }
                }
            }    
        }
        return -1;
    }
    
    /*
    *Checks to see if the players desired move is valid
    *Parameters: fromRow is the original row, fromColumn is the original column, toRow is the final row, toColumn is the final column, isRedTurn indicates whose turn it is, true for red, false for black
    *Return: returns true is the desired move is legal, and false if it is not
    */
    public boolean isValidMove(int fromRow, int fromColumn, int toRow, int toColumn, boolean isRedTurn) {
        int i = findPieceID(fromRow, fromColumn);
        if (i > -1 && toRow > 0 && toRow < 9 && toColumn > 0 && toColumn < 9 && isRedTurn == pieces.get(i).isRed()) {
            if (isJumpPossible(isRedTurn, 0) == -1 && Math.abs(fromRow-toRow) == 1 && Math.abs(fromColumn-toColumn) == 1) {
                if ((pieces.get(i).isRed() || pieces.get(i).isKing()) && fromRow < toRow) {
                    return true;
                }
                if ((!pieces.get(i).isRed() || pieces.get(i).isKing()) && fromRow > toRow) {
                    return true;
                }
            }
            if (isJumpPossible(isRedTurn, 0) > -1) {
                if (findPieceID(toRow, toColumn) == -1 && Math.abs(fromRow-toRow) == 2 && Math.abs(fromColumn-toColumn) == 2 && findPieceID(fromRow-(fromRow-toRow)/2, fromColumn-(fromColumn-toColumn)/2) > -1 && pieces.get(findPieceID(fromRow-(fromRow-toRow)/2, fromColumn-(fromColumn-toColumn)/2)).isRed() != isRedTurn) {
                    if ((pieces.get(i).isRed() || pieces.get(i).isKing()) && fromRow < toRow) {
                        return true;
                    }
                    if ((!pieces.get(i).isRed() || pieces.get(i).isKing()) && fromRow > toRow) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    /*
    *returns an arraylist of the pieces on the board
    *Return: returns the pieces on the board
    */
    public ArrayList<CheckersPiece> getPieces() {
        return pieces;
    }
    
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        gameBoard = new Pane();
        gameBoard.setPrefSize(320, 320);
        gameBoard.setMaxSize(640, 640);
        gameBoard.setMinSize(240, 240);
        gameControlPane = new VBox(new Button("Button 1"));
        gameControlPane.setPrefSize(100, 400);
        gameControlPane.setMaxSize(200, 800);
        gameControlPane.setMinSize(50, 200);
        rightPane = new VBox(new Circle(10));
        rightPane.setPrefSize(100, 400);
        rightPane.setMaxSize(200, 800);
        rightPane.setMinSize(50, 200);
        infoPane = new HBox();
        infoPane.setPrefSize(600, 100);
        int sides = 40;
        Rectangle square[] = new Rectangle[64];
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                if (i%2 == j%2)
                {
                    square[i*8+j] = new Rectangle(sides, sides, Color.BLACK);                    
                }
                else
                {
                    square[i*8+j] = new Rectangle(sides, sides, Color.RED);
                }
                square[i*8+j].widthProperty().bind(gameBoard.widthProperty().divide(8));
                square[i*8+j].heightProperty().bind(gameBoard.widthProperty().divide(8));
                square[i*8+j].xProperty().bind(gameBoard.widthProperty().multiply((double)j/8.0));
                square[i*8+j].yProperty().bind(gameBoard.widthProperty().multiply((double)i/8.0));
            }
        }
        gameBoard.getChildren().addAll(square);
        Button saveButton = new Button("Save");
        Button newButton = new Button("New");
        Button openButton = new Button("Open");
        Button settingsButton = new Button("Settings");
        topToolBar = new ToolBar();
        topToolBar.getItems().addAll(saveButton, newButton, openButton, settingsButton);
        BorderPane pane = new BorderPane();
        pane.setTop(topToolBar);
        pane.setLeft(gameControlPane);
        pane.setCenter(gameBoard);
        pane.setRight(rightPane);
        pane.setBottom(infoPane);
        Scene scene = new Scene(pane, 520, 480);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Checkers");
        primaryStage.show();
    }
    
    public static void saveGame (int numMoves) 
    throws IOException {
        String fileName;
        Scanner stdin = new Scanner(System.in);
        PrintStream P;
        System.out.println("Enter the name you want to store your game with: ");
        fileName = stdin.nextLine();
        P = new PrintStream(fileName + ".text");
        
        P.println(_________________________Black Name);
        P.println(_________________________Red Name);
        P.println(____________s or c___________________);
        if (board is custom___________) {
            SAVE START CONFIGURATION
        }
        for (int i = 0; i < nunMoves; i++) {
            P.println(moves[i].COORDINATES);
        }
        P.close();
        System.out.println("Game Saved.");
}
    
    public static void main(String [] args)
    {
        Scanner in = new Scanner(System.in);
        Application.launch(args);
        
        ArrayList<CheckersPiece> pieces = new ArrayList<CheckersPiece>(0);
        pieces.add(new CheckersPiece(true, false, 2, 4));
        pieces.add(new CheckersPiece(true, false, 1, 1));
        pieces.add(new CheckersPiece(false, false, 3, 5));
        pieces.add(new CheckersPiece(false, false, 5, 7));
        pieces.add(new CheckersPiece(false, false, 7, 7));
        pieces.add(new CheckersPiece(false, false, 7, 5));
        pieces.add(new CheckersPiece(false, false, 5, 3));
        CheckersGame game = new CheckersGame(pieces);
        game.board.printBoard();
        boolean isRedTurn = true;
        while (!game.isGameWon(isRedTurn))
        {
            game.turn(isRedTurn);
            game.board.printBoard();
            isRedTurn = !isRedTurn;
        }
    }
}
