/**
 *
 * @author Mark Monarch
 */
public class CheckersSquare {
    private boolean hasPiece;       //True if the square has a piece, false if not
    private CheckersPiece piece;    //The checkers piece on the square (or last on the square)
    
    /*
    *Creates a Checker tile with no piece
    */
    public CheckersSquare() {
        this.hasPiece = false;
        this.piece = new CheckersPiece();
    }
    
    /*
    *Creates a checkers tile with a piece on it
    *Parameters: piece the checkers piece on the tile
    */
    public CheckersSquare(CheckersPiece piece) {
        this.hasPiece = true;
        this.piece = piece;
    }
    
    /*
    *prints the current state of the tile
    */
    public void printState() {
        String space = " ";
        if (hasPiece) {
            if (piece.isKing()) {
                space = "K";
            }
            if (!piece.isRed()) {
                System.out.print(space + "B" + space);
            } else {
                System.out.print(space + "R" + space);
            }
        } else {
            System.out.print("   ");
        }
    }
    
    /*
    *sets the checkers piece on the tile
    *Parameters: piece sets the piece on the tile
    */
    public void setPiece(CheckersPiece piece) {
        this.piece = piece;
        this.hasPiece = true;
    }
    
    /*
    *returns the piece on the tile
    *Return: piece on tile
    */
    public CheckersPiece getPiece() {
        return this.piece;
    }
    
    /*
    returns if a piece is currently on the tile
    Return: true if there is a piece, false if not
    */
    public boolean hasPiece() {
        return this.hasPiece;
    }
    
    /*
    *sets hasPiece to false
    */
    public void removePiece() {
        this.hasPiece = false;
    }
}
