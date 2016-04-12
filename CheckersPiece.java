/**
 *
 * @author Mark Monarch
 */
public class CheckersPiece {
    private boolean isRed;  //True is red, False is black
    private boolean isKing; //True is king, False is normal piece
    private int row;        //Indicates the row the piece is in 1-8
    private int column;     //Indicates the column the piece is in 1-8
    
    /*
    *Creates a standard checkers piece
    *Parameters: isRed sets color, isKing sets if it is a king, row sets the row, and column sets the column
    */
    public CheckersPiece(boolean isRed, boolean isKing, int row, int column) {
        this.isRed= isRed;
        this.isKing = isKing;
        this.row = row;
        this.column = column;
    }
    
    /*
    *Creates a blank checkers piece
    *No Parameters
    */
    public CheckersPiece() {
        
    }
    
    /*
    *returns the state of the variable is red
    *Return: true for red and false for black
    */
    public boolean isRed() {
        return isRed;
    }
    
    /*
    *sets the color of the checkers piece
    *Parameters: isRed sets color, true for red, false for black
    */
    public void setColor(boolean isRed) {
        this.isRed = isRed;
    }
    
    /*
    *returns if the checkers piece is a king
    *return: true for king, false for normal
    */
    public boolean isKing() {
        return isKing;
    }
    
    /*
    *sets if the checkers piece is a king
    *Parameters: isKing sets if the piece is a king
    */
    public void setKing(boolean isKing) {
        this.isKing = isKing;
    }
    
    /*
    *returns the row number
    *Return: The row number
    */
    public int getRow() {
        return row;
    }
    
    /*
    *sets the row number
    *Parameters: row sets the row numbers
    */
    public void setRow(int row) {
        this.row = row;
    }
    
    /*
    *returns the column number
    *Return: column number
    */
    public int getColumn() {
        return column;
    }
    
    /*
    *sets the row number
    *Parameters: column sets the column number
    */
    public void setColumn(int column) {
        this.column = column;
    }
    
    public void setPosition(int row, int column) {
        setRow(row);
        setColumn(column);
    }
}
