/*
 * Written by Mark Monarch, Brent Wickenheiser, and Noah Whitehill
 */

import java.util.*;
import java.io.*;

public class CheckersGame
{
    
    private ArrayList<CheckersPiece> pieces;    //The checkers pieces currently in the game
    private ArrayList<String> moves;            //The moves that happen in a game
    private String blackName;                   //Player 1's name
    private String redName;                     //Player 2's name
    private int numMoves;                       //The total number of moves that have happened in a game
    private int currentMove;                    //The current move that is being accessed
    private int mandatoryPieceID;               //A double jumps piece ID
    
    /*
    *Creates a standard checkers game with 12 black and 12 red checkers pieces
    */
    public CheckersGame()
    {
        pieces = new ArrayList<>(0);
        moves = new ArrayList<>(0);
        for (int i = 1; i <= 8; i++)
        {
            for (int j = 1; j <= 8; j++)
            {
                if (i != 4 && i != 5)
                {
                    if (i%2 == 1 && j%2 == 1)
                    {
                        pieces.add(new CheckersPiece(i < 4, false, i, j));
                    }
                    else if (i%2 == 0 && j%2 == 0)
                    {
                        pieces.add(new CheckersPiece(i < 4, false, i, j));
                    }
                }
            }
        }
        mandatoryPieceID = -1;
    }
    
    /*
    *Creates a checkers game with an array of checkers pieces
    *Parameters: pieces is an arraylist of checkers pieces
    */
    public CheckersGame(ArrayList<CheckersPiece> pieces)
    {
        moves = new ArrayList<>(0);
        this.pieces = pieces;
        mandatoryPieceID = -1;
    }
    
    public int getNumMoves() {
        return moves.size();
    }
    
    public void setBlackName(String name) {
        blackName = name;
    }
    
    public String getBlackName() {
        return blackName;
    }
    
    public void setRedName(String name) {
        redName = name;
    }
    
    public String getRedName() {
        return redName;
    }
    
    public void setMandatoryID(int ID) {
        mandatoryPieceID = ID;
    }
    
    public int getMandatoryID() {
        return mandatoryPieceID;
    }
    
    public void movePiece(int fromRow, int fromColumn, int toRow, int toColumn) {
        pieces.get(findPieceID(fromRow, fromColumn)).setPosition(toRow, toColumn);
        if ((pieces.get(findPieceID(toRow, toColumn)).isRed() && toRow == 8) || (!pieces.get(findPieceID(toRow, toColumn)).isRed() && toRow == 1)) {
            pieces.get(findPieceID(toRow, toColumn)).setKing(true);
        }
    }
    
    public boolean isMovePossible(boolean isRedTurn) {
        int row,
            column;
        
        if (isJumpPossible(isRedTurn, 0) > -1)
        {
            return true;
        }
        for (int i = 0; i < pieces.size(); i++)
        {
            row = pieces.get(i).getRow();
            column = pieces.get(i).getColumn();
            if (pieces.get(i).isRed() == isRedTurn)
            {
                if ((pieces.get(i).isRed() || pieces.get(i).isKing()) && row < 8)
                {
                    if (column > 1 && findPieceID(row+1, column-1) == -1)
                    {
                        return true;
                    }
                    if (column < 8 && findPieceID(row+1, column+1) == -1)
                    {
                        return true;
                    }
                }
                if ((!pieces.get(i).isRed() || pieces.get(i).isKing()) && row > 1)
                {
                    if (column > 1 && findPieceID(row-1, column-1) == -1)
                    {
                        return true;
                    }
                    else if (column < 8 && findPieceID(row-1, column+1) == -1)
                    {
                        return true;
                    }
                }
            }    
        }
        return false;
    }
    
    public boolean isGameWon(boolean isRedTurn)
    {
        boolean isBlack = false,
                isRed = false;
        for (int i = 0; i < pieces.size(); i++)
        {
            if (pieces.get(i).isRed())
            {
                isRed = true;
            }
            else if (!pieces.get(i).isRed())
            {
                isBlack = true;
            }
            if (isRed && isBlack)
            {
                i = pieces.size();
            }
        }
        if (!isRed || !isBlack || !isMovePossible(isRedTurn))
        {
            return true;
        }
        return false;
    }
    
    /*
    *Finds the index of a checkers piece from its location
    *Parameters: row is the row of the piece 1-8, column is the column of the piece 1-8
    *Return: returns the index of the piece, or -1 if no piece is there
    */
    public int findPieceID(int row, int column)
    {
        for (int i = 0; i < pieces.size(); i++)
        {
            if (pieces.get(i).getRow() == row && pieces.get(i).getColumn() == column)
            {
                return i;
            }
        }
        return -1;
    }
    
    public void turn() {
        int fromRow,
            fromColumn,
            toRow,
            toColumn;
        String cMove;
        cMove = moves.get(currentMove);
        fromRow = Integer.parseInt(cMove.substring(0, 1));
        fromColumn = Integer.parseInt(cMove.substring(2, 3));
        toRow = Integer.parseInt(cMove.substring(4, 5));
        toColumn = Integer.parseInt(cMove.substring(6));
        pieces.get(findPieceID(fromRow, fromColumn)).setPosition(toRow, toColumn);
        if (toRow == 1 && !pieces.get(findPieceID(toRow, toColumn)).isRed())
        {
            pieces.get(findPieceID(toRow, toColumn)).setKing(true);
        }
        else if (toRow == 8 && pieces.get(findPieceID(toRow, toColumn)).isRed())
        {
            pieces.get(findPieceID(toRow, toColumn)).setKing(true);
        }
        if (Math.abs(toRow-fromRow) == 2)
        {   
            pieces.remove(findPieceID(fromRow+(toRow-fromRow)/2, fromColumn+(toColumn-fromColumn)/2));
        }
    }
    
    public void addMove(int fromRow, int fromColumn, int toRow, int toColumn) {
        moves.add(fromRow + " " + fromColumn + " " + toRow + " " + toColumn);
        numMoves++;
    }
    
    /*
    *Simulates a players turn
    *Parameters: isRedTurn indicates whose turn, true for red, false for black
    */
    public void turn(boolean isRedTurn)
    {
        Scanner in = new Scanner(System.in);
        int fromRow,
            fromColumn,
            toRow,
            toColumn,
            mandatoryPieceID;
        mandatoryPieceID = -2;
        boolean isValid;
        isValid = false;
        do
        {
            fromRow = in.nextInt();
            fromColumn = in.nextInt();
            toRow = in.nextInt();
            toColumn = in.nextInt();
            if (isValidMove(fromRow, fromColumn, toRow, toColumn, isRedTurn) && (mandatoryPieceID == -2 || mandatoryPieceID == findPieceID(fromRow, fromColumn)))
            {
                moves.add(fromRow + " " + fromColumn + " " + toRow + " " + toColumn);
                numMoves++;
                isValid = true;
                pieces.get(findPieceID(fromRow, fromColumn)).setRow(toRow);
                pieces.get(findPieceID(toRow, fromColumn)).setColumn(toColumn);
                if (toRow == 1 && !pieces.get(findPieceID(toRow, toColumn)).isRed())
                {
                     pieces.get(findPieceID(toRow, toColumn)).setKing(true);
                }
                else if (toRow == 8 && pieces.get(findPieceID(toRow, toColumn)).isRed())
                {
                    pieces.get(findPieceID(toRow, toColumn)).setKing(true);
                }
                if (Math.abs(toRow-fromRow) == 2)
                {   
                    pieces.remove(findPieceID(fromRow+(toRow-fromRow)/2, fromColumn+(toColumn-fromColumn)/2));
                    if (isJumpPossible(isRedTurn, findPieceID(toRow, toColumn)) == findPieceID(toRow, toColumn))
                    {
                        System.out.println(findPieceID(toRow, toColumn));
                        isValid = false;
                        mandatoryPieceID = findPieceID(toRow, toColumn);
                    }
                }
            }
        } while (!isValid);
    }
    
    public void resetMoves() {
        moves = new ArrayList<>(0);
        currentMove = 0;
    }
    
    public boolean isDoubleJumpPossible(boolean isRedTurn, int jumpID) {
        if (isJumpPossible(isRedTurn, jumpID) == jumpID) {
            return true;
        }
        return false;
    }
    
    /*
    *Checks the board to see if a jump is possible for a certain player
    *Parameters: isRedTurn indicates whose turn it is, true for red, false for black, startPoint indicates where in pieces to begin the search
    *Return: returns the index of a possible jump, or -1 if no jump is possible
    */
    public int isJumpPossible(boolean isRedTurn, int startPoint)
    {
        int row,
            column;
        for (int i = startPoint; i < pieces.size(); i++)
        {
            row = pieces.get(i).getRow();
            column = pieces.get(i).getColumn();
            if (pieces.get(i).isRed() == isRedTurn)
            {
                if ((pieces.get(i).isRed() || pieces.get(i).isKing()) && row < 7)
                {
                    if (column > 2 && findPieceID(row+1, column-1) > -1 && pieces.get(findPieceID(row+1, column-1)).isRed() != isRedTurn && findPieceID(row+2, column-2) == -1)
                    {
                        return i;
                    }
                    if (column < 7 && findPieceID(row+1, column+1) > -1 && pieces.get(findPieceID(row+1, column+1)).isRed() != isRedTurn && findPieceID(row+2, column+2) == -1)
                    {
                        return i;
                    }
                }
                if ((!pieces.get(i).isRed() || pieces.get(i).isKing()) && row > 2)
                {
                    if (column > 2 && findPieceID(row-1, column-1) > -1 && pieces.get(findPieceID(row-1, column-1)).isRed() != isRedTurn && findPieceID(row-2, column-2) == -1)
                    {
                        return i;
                    }
                    else if (column < 7 && findPieceID(row-1, column+1) > -1 && pieces.get(findPieceID(row-1, column+1)).isRed() != isRedTurn && findPieceID(row-2, column+2) == -1)
                    {
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
    public boolean isValidMove(int fromRow, int fromColumn, int toRow, int toColumn, boolean isRedTurn)
    {
        int i = findPieceID(fromRow, fromColumn);
        if (i > -1 && toRow > 0 && toRow < 9 && toColumn > 0 && toColumn < 9 && isRedTurn == pieces.get(i).isRed() && (i == mandatoryPieceID || mandatoryPieceID == -1))
        {
            if (isJumpPossible(isRedTurn, 0) == -1 && Math.abs(fromRow-toRow) == 1 && Math.abs(fromColumn-toColumn) == 1 && findPieceID(toRow, toColumn) == -1)
            {
                if ((pieces.get(i).isRed() || pieces.get(i).isKing()) && fromRow < toRow)
                {
                    return true;
                }
                if ((!pieces.get(i).isRed() || pieces.get(i).isKing()) && fromRow > toRow)
                {
                    return true;
                }
            }
            if (isJumpPossible(isRedTurn, 0) > -1)
            {
                if (findPieceID(toRow, toColumn) == -1 && Math.abs(fromRow-toRow) == 2 && Math.abs(fromColumn-toColumn) == 2 && findPieceID(fromRow-(fromRow-toRow)/2, fromColumn-(fromColumn-toColumn)/2) > -1 && pieces.get(findPieceID(fromRow-(fromRow-toRow)/2, fromColumn-(fromColumn-toColumn)/2)).isRed() != isRedTurn)
                {
                    if ((pieces.get(i).isRed() || pieces.get(i).isKing()) && fromRow < toRow)
                    {
                        return true;
                    }
                    if ((!pieces.get(i).isRed() || pieces.get(i).isKing()) && fromRow > toRow)
                    {
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
    public ArrayList<CheckersPiece> getPieces()
    {
        return pieces;
    }
    
    public void saveGame (String fileName) {
        PrintStream P;
        
        try {
            P = new PrintStream(fileName);
            P.println(blackName);
            P.println(redName);
            for (int i = 0; i < moves.size(); i++) {
                P.println(moves.get(i));
            }
            P.println("End");
            P.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    
    public void loadGame (String fileName) {
        BufferedReader in;
        String currentLine;
        int i;

        moves = new ArrayList<>(0);
        try {
            in = new BufferedReader(new FileReader(fileName + ".che"));
            i = 0;
            blackName = in.readLine();
            redName = in.readLine();
            currentLine = in.readLine();
            while(!currentLine.equals("End")) {
                moves.add(currentLine);
                currentLine = in.readLine();
                i++;
                System.out.println(currentLine);
            }
            in.close();
        } catch (IOException e) {}
    }
    
    public void printBoard() {
        CheckersBoard print = new CheckersBoard(pieces);
        print.printBoard();
    }
}
 
