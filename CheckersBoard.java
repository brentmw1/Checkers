/**
 *
 * @author Mark Monarch
 */

import java.util.ArrayList;

public class CheckersBoard {
    public CheckersSquare[][] tiles;    //2 dimensional array of checkers squares representing the square on the checkers board
    
    /*
    *Creates a checkers board object with no pieces on it
    */
    public CheckersBoard() {
        tiles = new CheckersSquare[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tiles[i][j] = new CheckersSquare();
            }
        }
    }
   
    /*
    *Creates a checkers board object with a list of pieces on it
    *Parameters: pieces is an arraylist of the pieces on the board
    */
    public CheckersBoard(ArrayList<CheckersPiece> pieces) {
        tiles = new CheckersSquare[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tiles[i][j] = new CheckersSquare();
            }
        }
        int row,
            column;
        for (int i = 0; i < pieces.size(); i++) {
            row = pieces.get(i).getRow();
            column = pieces.get(i).getColumn();
            tiles[row-1][column-1] = new CheckersSquare(pieces.get(i));
        }
    }
    
    /*
    *Moves a piece from on tile to another
    *Parameters: fromRow is the original row, fromColumn is the original column, toRow is the final row, and toColumn is the final column
    */
    public void movePiece(int fromRow, int fromColumn, int toRow, int toColumn) {
        tiles[toRow-1][toColumn-1].setPiece(tiles[fromRow-1][fromColumn-1].getPiece());
        tiles[fromRow-1][fromColumn-1].removePiece();
    }
   
    /*
    *removes a piece from the board
    *Parameters: row is the row of the removed piece 1-8, column is the removed piece'c column 1-8
    */
    public void removePiece(int row, int column) {
        tiles[row-1][column-1].removePiece();
    }
    
    /*
    *Prints the state of the current board
    */
    public void printBoard() {
        System.out.println("\n     1  2  3  4  5  6  7  8\n");
        for (int i = 0; i < 8; i++) {
            System.out.print(i+1 + "   ");
            for (int j = 0; j < 8; j++) {
                tiles[i][j].printState();
            }
            System.out.println();
        }
    }
}
