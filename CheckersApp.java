/**
 *
 * @author Mark Monarch
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.text.*;
import javafx.scene.effect.*;
import java.io.*;
import java.util.*;

public class CheckersApp extends Application{
    private Stage primaryStage;
    private CheckersGame game;
    private boolean isGameRunning;
    private boolean isRedTurn;
    private boolean isFirstSelected;
    private int fromRow;
    private int fromColumn;
    private int toRow;
    private int toColumn;
    private String saveName;
    private ToolBar toolBar;
    private Button saveButton;
    private Button newButton;
    private Button openButton;
    private VBox mainPane;
    private GridPane board;
    private Button nextButton;
    private HBox display;
    private VBox playerNames;
    private Text playerTurn;
    private Text blackDisplay;
    private Text redDisplay;
    private ArrayList<Circle> gamePieces;
    private Rectangle [] tiles;
    private Color blackTile;
    private Color redTile;
    private Color blackPiece;
    private Color redPiece;
    private DropShadow selected;
    
    public void getBlackName() {
        Stage nameInput;
        StackPane pane;
        Button OK;
        Text instructions;
        TextField name;
        
        nameInput = new Stage();
        pane = new StackPane();
        OK = new Button("OK");
        instructions = new Text("Enter Black Player's Name");
        name = new TextField();
        name.setText("Black");
        OK.setOnAction(event -> {
            game.setBlackName(name.getText());
            if (game.getBlackName().equals("")) {
                game.setBlackName("Black");
            }
            nameInput.close();
        });
        pane.getChildren().addAll(instructions, name, OK);
        pane.setAlignment(instructions, Pos.TOP_CENTER);
        pane.setAlignment(name, Pos.CENTER);
        pane.setAlignment(OK, Pos.BOTTOM_CENTER);
        nameInput.setScene(new Scene(pane, 240, 80));
        nameInput.showAndWait();
    }
    
    public void getRedName() {
        Stage nameInput;
        StackPane pane;
        Button OK;
        Text instructions;
        TextField name;
        
        nameInput = new Stage();
        pane = new StackPane();
        OK = new Button("OK");
        instructions = new Text("Enter Red Player's Name");
        name = new TextField();
        name.setText("Red");
        OK.setOnAction(event -> {
            game.setRedName(name.getText());
            if (game.getRedName().equals("")) {
                game.setRedName("Red");
            }
            nameInput.close();
        });
        pane.getChildren().addAll(instructions, name, OK);
        pane.setAlignment(instructions, Pos.TOP_CENTER);
        pane.setAlignment(name, Pos.CENTER);
        pane.setAlignment(OK, Pos.BOTTOM_CENTER);
        nameInput.setScene(new Scene(pane, 240, 80));
        nameInput.showAndWait();
    }    
    
    public void setPieces() {
        gamePieces = new ArrayList<>(0);
        
        for (int i = 0; i < game.getPieces().size(); i++) {
            gamePieces.add(new Circle(30));
            if (game.getPieces().get(i).isRed()) {
                gamePieces.get(i).setFill(redPiece);
            gamePieces.get(i).setStroke(blackPiece);
            } else {
                gamePieces.get(i).setFill(blackPiece);
            gamePieces.get(i).setStroke(redPiece);
            }
            gamePieces.get(i).setMouseTransparent(true);
            board.add(gamePieces.get(i), game.getPieces().get(i).getColumn()-1, game.getPieces().get(i).getRow()-1);
        }
    }
    
    public void newGame() {
        game = new CheckersGame();
        getBlackName();
        getRedName();
        updatePlayerNames();
        updatePlayerTurn();
        isGameRunning = true;
        isRedTurn = false;
        isFirstSelected = false;
        saveName = null;
        setPieces();
    }
    
    public void updatePlayerNames() {
        blackDisplay.setText("Black: " + game.getBlackName());
        redDisplay.setText("Red: " + game.getRedName());
    }
    
    public void updatePlayerTurn() {
        String displayName;
        
        displayName = new String(isRedTurn ? game.getRedName() : game.getBlackName());
        if (displayName.length() > 6) {
            displayName = displayName.substring(0, 7);
        }
        playerTurn.setText(displayName);
        playerTurn.setFill(isRedTurn ? redPiece : blackPiece);
    }
    
    @Override
    public void start(Stage primaryStage) {
        Rectangle rect;
        boolean isRed;
       
        game = new CheckersGame();
        this.primaryStage = primaryStage;
        saveButton = new Button("Save");
        newButton = new Button("New");
        openButton = new Button("Open");
        toolBar = new ToolBar(saveButton, newButton, openButton);
        toolbar.setMinHeight(80);
        mainPane = new VBox();
        board = new GridPane();
        playerNames = new VBox();
        blackDisplay = new Text("Black");
        redDisplay = new Text("Red");
        playerNames.setWrappingWidth(170);
        playerNames.setFont(new Font(27));
        playerTurn = new Text("Black");
        playerTurn.setFont(new Font(50));
        playerTurn.setFill(Color.BLACK);
        playerTurn.setWrappingWidth(180);
        nextButton = new Button("Next");
        nextButton.setPrefSize(180, 80);
        display = new HBox(playerNames, nextButton, playerTurn);
        selected = new DropShadow();
        isRed = false;
        tiles = new Rectangle[64];
        redPiece = Color.RED;
        blackPiece = Color.BLACK;
        redTile = Color.WHEAT;
        blackTile = Color.SIENNA;
        selected.setOffsetY(0f);
        selected.setOffsetX(0f);
        selected.setColor(Color.YELLOW);
        selected.setWidth(200);
        selected.setHeight(200);
        nextButton.setOnAction(event -> {
            try{
            game.turn();
            game.incrementCurrent();
            clearBoard();
            setPieces();
            }catch(Exception e){}
            isGameRunning = true;
        });
        updatePlayerNames();
        newButton.setOnAction(event -> {
            clearBoard();
            newGame(); 
        });
        saveButton.setOnAction(event -> {
            saveGame();
        });
        openButton.setOnAction(event -> {
            loadGame();
        });
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (isRed) {
                    rect = new Rectangle(60, 60, redTile);
                } else {
                    rect = new Rectangle(60, 60, blackTile);
                }               
                rect.setStroke(blackTile);
                board.add(rect, col, row);
                isRed = !isRed;
            }
            isRed = !isRed;
        }
        board.setOnMouseClicked(event -> {
            int row,
                column;
            
            if (isGameRunning) {
                row = (int)event.getY()/60 + 1;
                column = (int)event.getX()/60 +1;
                if (isFirstSelected) {
                    toRow = row;
                    toColumn = column;
                    if (game.isValidMove(fromRow, fromColumn, toRow, toColumn, isRedTurn)) {
                        game.addMove(fromRow, fromColumn, toRow, toColumn);
                        game.movePiece(fromRow, fromColumn, toRow, toColumn);
                        if (Math.abs(fromRow-toRow) == 2) {
                            game.getPieces().remove(game.findPieceID(fromRow+(toRow-fromRow)/2, fromColumn+(toColumn-fromColumn)/2));
                            if (game.isDoubleJumpPossible(isRedTurn, game.findPieceID(toRow, toColumn))) {
                                isRedTurn = !isRedTurn;
                                game.setMandatoryID(game.findPieceID(toRow, toColumn));
                            } else {
                                game.setMandatoryID(-1);
                            }
                        }
                        isRedTurn = !isRedTurn; 
                    }
                    clearBoard();
                    setPieces();
                    isFirstSelected = false;
                    updatePlayerTurn();
                } else {
                    fromRow = row;
                    fromColumn = column;
                    isFirstSelected = true;
                    tiles[(column-1)*8 + (row-1)].setEffect(selected);
                }
                if (game.isGameWon(!isRedTurn)) {
                    System.out.println("Won");
                    isGameRunning = false;
                }
            }
        });
        this.primaryStage.setTitle("Checkers");
        mainPane.getChildren().addAll(toolBar, board, display);
        this.primaryStage.setScene(new Scene(mainPane, 488, 600));
        this.primaryStage.show();
    }
    
    public void clearBoard() {
        boolean isRed = false;
        
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (isRed) {
                    tiles[col*8 + row] = new Rectangle(60, 60, redTile);
                } else {
                    tiles[col*8 + row] = 
                            new Rectangle(60, 60, blackTile);
                }               
                tiles[col*8 + row].setStroke(blackTile);
                board.add(tiles[col*8 + row], col, row);
                isRed = !isRed;
            }
            isRed = !isRed;
        }
    }
    
    public void saveGame() {
        Stage saveGame;
        StackPane pane;
        Button OK;
        Text instructions;
        TextField fileName;
        
        if (saveName == null) {
            saveGame = new Stage();
            pane = new StackPane();
            OK = new Button("OK");
            instructions = new Text("Enter The Name You Wish To Save As");
            fileName = new TextField();
            fileName.setText("game" + System.currentTimeMillis());
            OK.setOnAction(event -> {            
                saveName = fileName.getText().replaceAll(" ", "_") + ".che";
                game.saveGame(saveName);
                saveGame.close();
            });
            pane.getChildren().addAll(instructions, fileName, OK);
            pane.setAlignment(instructions, Pos.TOP_CENTER);
            pane.setAlignment(fileName, Pos.CENTER);
            pane.setAlignment(OK, Pos.BOTTOM_CENTER);
            saveGame.setScene(new Scene(pane, 240, 80));
            saveGame.showAndWait();
        } else {
            game.saveGame(saveName);
        }
    }
    
    public void loadGame() {
        Stage loadGame;
        StackPane pane;
        Button OK;
        Text instructions;
        TextField fileName;
        
        game = new CheckersGame();
        loadGame = new Stage();
        pane = new StackPane();
        OK = new Button("OK");
        instructions = new Text("Enter The Name Of The Game You Wish To Load");
        fileName = new TextField();
        OK.setOnAction(event -> {
            String name;
            name = fileName.getText();
            game.loadGame(name);
            clearBoard();
            setPieces();
            if (game.getNumMoves() % 2 == 1)
                isRedTurn = true;
            else 
                isRedTurn = false;
            loadGame.close();
        });
        pane.getChildren().addAll(instructions, fileName, OK);
        pane.setAlignment(instructions, Pos.TOP_CENTER);
        pane.setAlignment(fileName, Pos.CENTER);
        pane.setAlignment(OK, Pos.BOTTOM_CENTER);
        loadGame.setScene(new Scene(pane, 320, 80));
        loadGame.showAndWait();
    }
    
    public void loadGameToEnd() {
        loadGame();
        game.runThrough();
        clearBoard();
        setPieces();
        isGameRunning = true;
    }
    
    public static void main(String [] args) {
        Application.launch(args);
    }
}
