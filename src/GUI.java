import javafx.animation.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;


public class GUI extends Application {

    Game g;

    boolean isCheck;
    boolean isSecond;
    int kingX;
    int kingY;
    int chosenX;
    int chosenY;
    HashMap<Integer, Boolean> possibleCells;

    public void init() throws Exception{
        isSecond = false;
        isCheck = false;

        chosenX = -1;
        chosenY = -1;

        kingX = -1;
        kingY = -1;

        possibleCells = new HashMap<>();

        g = new Game();
    }

    public void start(Stage stage){
        stage.setTitle("Chess");

        GridPane gridPane = new GridPane();

        for(int i = 0;i<8;i++)
            for(int j = 0;j<8;j++){

                ImageView imgView = null;

                if(g.get(j,i)!=null){
                    imgView = new ImageView(g.get(j,i).getImg());
                }

                MyPane cell = new MyPane(j,i);
                cell.setPrefWidth(70.0);
                cell.setPrefHeight(70.0);
                cell.setCenter(imgView);

                cell.setOnMouseClicked((MouseEvent e)-> {
                    Figure fig = g.get(cell.getX(),cell.getY());

                    if(kingX!=-1&&kingY!=-1) {
                        if ((kingX + kingY) % 2 == 0)
                            gridPane.getChildren().get(8 * kingY + kingX).setStyle("-fx-background-color: rgb(255,255,255);" +
                                    "-fx-border-color: rgba(0,0,0);");
                        else
                            gridPane.getChildren().get(8 * kingY + kingX).setStyle("-fx-background-color: rgb(255,210,96);" +
                                    "-fx-border-color: rgba(0,0,0);");

                        kingX = -1;
                        kingY = -1;
                    }

                    if(!isSecond&&fig!=null&&fig.getColor()==g.turn()) {
                        cell.setStyle("-fx-background-color: rgba(0,0,255,0.5);" +
                                "-fx-border-color: rgba(0,0,0);");

                        chosenX = cell.getX();
                        chosenY = cell.getY();

                        this.colorPossible(chosenX, chosenY, gridPane);

                        isSecond = true;
                    }else if(isSecond){
                        if ((chosenX + chosenY) % 2 == 0)
                            gridPane.getChildren().get(8*chosenY+chosenX).setStyle("-fx-background-color: rgb(255,255,255);" +
                                    "-fx-border-color: rgba(0,0,0);");
                        else
                            gridPane.getChildren().get(8*chosenY+chosenX).setStyle("-fx-background-color: rgb(255,210,96);" +
                                    "-fx-border-color: rgba(0,0,0);");

                        this.uncolorPossible(gridPane);


                        if((cell.getX()!=chosenX||cell.getY()!=chosenY)
                                &&g.get(cell.getX(), cell.getY())!=null&&
                                g.get(cell.getX(), cell.getY()).getColor()==g.turn()){
                            cell.setStyle("-fx-background-color: rgba(0,0,255,0.5);" +
                                    "-fx-border-color: rgba(0,0,0);");

                            chosenX = cell.getX();
                            chosenY = cell.getY();

                            possibleCells.clear();

                            this.colorPossible(chosenX, chosenY, gridPane);
                        } else {
                            if(possibleCells.get(8*cell.getY()+cell.getX())!=null){
                                if(g.get(chosenX,chosenY).getClass()==King.class&&g.get(chosenX,chosenY).isFirst()
                                        &&(cell.getX()==2||cell.getX()==6)){
                                    if(!g.Castling(chosenX, chosenY, cell.getX(), cell.getY())){
                                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                        alert.setTitle("Warning");
                                        alert.setHeaderText(null);
                                        alert.setContentText("Castling is not alowed now!");
                                        alert.showAndWait();

                                        if((cell.getX()+cell.getY())%2==0)
                                            cell.setStyle("-fx-background-color: rgb(255, 255, 255);" +
                                                    "-fx-border-color: rgb(0, 0, 0);");
                                        else
                                            cell.setStyle("-fx-background-color: rgb(255,210,96);" +
                                                    "-fx-border-color: rgb(0, 0, 0);");

                                        chosenX = -1;
                                        chosenY = -1;
                                    }else{
                                        castling(cell, (MyPane) gridPane.getChildren().get(8 * chosenY + chosenX), gridPane);
                                    }
                                }

                                else if(g.get(chosenX,chosenY).getClass()==Pawn.class&&(cell.getY()==0||cell.getY()==7)) {
                                    if(g.preCheck(g.turn(), chosenX, chosenY, cell.getX(), cell.getY())) {
                                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                        alert.setTitle("Warning");
                                        alert.setHeaderText(null);
                                        alert.setContentText("It's forbidden move.\nYou will be in a check situation");
                                        alert.showAndWait();

                                        if((cell.getX()+cell.getY())%2==0)
                                            cell.setStyle("-fx-background-color: rgb(255, 255, 255);" +
                                                    "-fx-border-color: rgb(0, 0, 0);");
                                        else
                                            cell.setStyle("-fx-background-color: rgb(255,210,96);" +
                                                    "-fx-border-color: rgb(0, 0, 0);");

                                        chosenX = -1;
                                        chosenY = -1;
                                    }

                                    else {
                                        ArrayList<String> choices = new ArrayList<>();
                                        choices.add("Queen");
                                        choices.add("Rogue");
                                        choices.add("Bishop");
                                        choices.add("Knight");

                                        ChoiceDialog<String> dialog = new ChoiceDialog<>("Queen", choices);
                                        dialog.setTitle("Choose the figure");
                                        dialog.setContentText("Choose");
                                        dialog.setHeaderText("Choice Dialog");

                                        Optional<String> result = dialog.showAndWait();

                                        if ((cell.getX() + cell.getY()) % 2 == 0)
                                            cell.setStyle("-fx-background-color: rgb(255, 255, 255);" +
                                                    "-fx-border-color: rgb(0, 0, 0);");
                                        else
                                            cell.setStyle("-fx-background-color: rgb(255,210,96);" +
                                                    "-fx-border-color: rgb(0, 0, 0);");

                                        if (result.isPresent()) {

                                            Figure new_fig = null;
                                            switch (result.get()) {
                                                case "Queen":
                                                    new_fig = new Queen(cell.getX(), cell.getY(), g.get(chosenX, chosenY).getColor());
                                                    break;
                                                case "Rogue":
                                                    new_fig = new Rogue(cell.getX(), cell.getY(), g.get(chosenX, chosenY).getColor());
                                                    break;
                                                case "Bishop":
                                                    new_fig = new Bishop(cell.getX(), cell.getY(), g.get(chosenX, chosenY).getColor());
                                                    break;
                                                case "Knight":
                                                    new_fig = new Knight(cell.getX(), cell.getY(), g.get(chosenX, chosenY).getColor());
                                                    break;
                                            }

                                            transform(cell, (MyPane) gridPane.getChildren().get(8 * chosenY + chosenX), gridPane, new_fig);

                                        } else {
                                            chosenX = -1;
                                            chosenY = -1;
                                        }
                                    }
                                }
                                else {
                                    if(g.preCheck(g.turn(), chosenX, chosenY, cell.getX(), cell.getY())) {
                                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                        alert.setTitle("Warning");
                                        alert.setHeaderText(null);
                                        alert.setContentText("It's forbidden move.\nYou will be in a check situation");
                                        alert.showAndWait();

                                        if((cell.getX()+cell.getY())%2==0)
                                            cell.setStyle("-fx-background-color: rgb(255, 255, 255);" +
                                                    "-fx-border-color: rgb(0, 0, 0);");
                                        else
                                            cell.setStyle("-fx-background-color: rgb(255,210,96);" +
                                                    "-fx-border-color: rgb(0, 0, 0);");

                                        chosenX = -1;
                                        chosenY = -1;
                                    }
                                    else{
                                        animate(cell, (MyPane) gridPane.getChildren().get(8 * chosenY + chosenX), gridPane);

                                    }
                                }
                            }else{
                                chosenX = -1;
                                chosenY = -1;
                            }

                            possibleCells.clear();

                            isSecond = false;
                        }
                    }

                });


                cell.setOnMouseEntered((MouseEvent e)->{
                    if(cell.getX()!=chosenX||cell.getY()!=chosenY) {
                        cell.setStyle("-fx-background-color: rgba(123,23,344, 0.5);" +
                                "-fx-border-color: rgba(0,0,0);");
                    }
                });

                cell.setOnMouseExited((MouseEvent e)->{
                    if(cell.getX()!=chosenX||cell.getY()!=chosenY) {
                        if(possibleCells.get(8*cell.getY()+cell.getX())!=null){
                            if(possibleCells.get(8*cell.getY()+cell.getX()))
                                cell.setStyle("-fx-background-color: rgba(255, 0, 0, 0.5);" +
                                            "-fx-border-color: rgba(0,0,0);");
                            else
                                cell.setStyle("-fx-background-color: rgba(0, 255, 0, 0.5);" +
                                            "-fx-border-color: rgba(0,0,0);");
                        }
                        else if(cell.getX() == kingX&& cell.getY()==kingY){
                            cell.setStyle("-fx-background-color: rgba(255, 0, 0, 0.5);" +
                                        "-fx-border-color: rgba(0,0,0);");
                        }
                        else if ((cell.getX() + cell.getY()) % 2 == 0)
                            cell.setStyle("-fx-background-color: rgb(255,255,255);" +
                                    "-fx-border-color: rgba(0,0,0);");
                        else
                            cell.setStyle("-fx-background-color: rgb(255,210,96);" +
                                    "-fx-border-color: rgba(0,0,0);");
                    }
                });

                if((i+j)%2==0)
                    cell.setStyle("-fx-background-color: rgb(255,255,255);" +
                            "-fx-border-color: rgba(0,0,0);");
                else
                    cell.setStyle("-fx-background-color: rgb(255,210,96);" +
                            "-fx-border-color: rgba(0,0,0);");


                gridPane.add(cell,j,i);
            }


        Scene mainScene = new Scene(gridPane);

        stage.setScene(mainScene);

        stage.show();

        stage.setMaxHeight(stage.getHeight());
        stage.setMinHeight(stage.getHeight());
        stage.setMaxWidth(stage.getWidth());
        stage.setMinWidth(stage.getWidth());
    }

    private void colorPossible(int X, int Y, GridPane pane){
        possibleCells = g.getPossibleCells(X, Y);

        possibleCells.forEach((Integer k, Boolean v)->{
            if(v)
                pane.getChildren().get(k).setStyle("-fx-background-color: rgba(255,0,0, 0.5);" +
                                                    "-fx-border-color: rgba(0,0,0);");
            else
                pane.getChildren().get(k).setStyle("-fx-background-color: rgba(0,255,0, 0.5);" +
                                                    "-fx-border-color: rgba(0,0,0);");
        });
    }

    private void uncolorPossible(GridPane pane){
        possibleCells.forEach((Integer k, Boolean v)->{
            if(((k/8)+k%8)%2==0)
                pane.getChildren().get(k).setStyle("-fx-background-color: rgba(255,255,255);" +
                        "-fx-border-color: rgba(0,0,0);");
            else
                pane.getChildren().get(k).setStyle("-fx-background-color: rgba(255,210,96);" +
                        "-fx-border-color: rgba(0,0,0);");
        });
    }


    private void animate(MyPane cell, MyPane chosenCell, GridPane gp){
        ParallelTransition pt = new ParallelTransition();

        chosenCell.toFront();

        TranslateTransition tt = new TranslateTransition();
        tt.setDuration(Duration.seconds(1));
        tt.setNode(chosenCell.getChildren().get(0));
        tt.setToX((cell.getX()-chosenX)*cell.getPrefWidth());
        tt.setToY((cell.getY()-chosenY)*cell.getPrefHeight());

        if(!cell.getChildren().isEmpty()){
            FadeTransition fd = new FadeTransition();
            fd.setDuration(Duration.seconds(1));
            fd.setNode(cell.getChildren().get(0));
            fd.setToValue(0.0);
            pt.getChildren().add(fd);
        }

        pt.setOnFinished((ActionEvent event)->{
            if(!cell.getChildren().isEmpty())
                cell.getChildren().remove(0);

            cell.setCenter(new ImageView(g.get(chosenX, chosenY).getImg()));
            chosenCell.getChildren().remove(0);

            g.move(chosenX, chosenY, cell.getX(), cell.getY());

            gp.getChildren().remove(63);
            gp.getChildren().add(8*chosenY+chosenX, chosenCell);

            if(g.postCheck(g.turn())){

                if(g.mate(g.turn())) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Mate");
                    alert.setHeaderText("WIN!");
                    String winner;
                    if (g.turn() == COLOR.WHITE)
                        winner = "Darks";
                    else
                        winner = "Lights";

                    alert.setContentText("Congratulations! " + winner + " are win!");
                    alert.show();
                }else {
                    find:
                    {
                        for (int r = 0; r < 8; r++)
                            for (int col = 0; col < 8; col++)
                                if (g.get(col, r) != null && g.get(col, r).getClass() == King.class && g.get(col, r).getColor() == g.turn()) {

                                    gp.getChildren().get(8 * r + col).setStyle("-fx-background-color: rgb(255, 0, 0, 0.5);" +
                                            "-fx-border-color: rgba(0, 0, 0);");

                                    kingX = col;
                                    kingY = r;

                                    isCheck = true;
                                    break find;
                                }
                    }
                }
            }

            chosenX = -1;
            chosenY = -1;
        });

        pt.getChildren().add(tt);

        pt.play();

    }

    private void transform(MyPane cell, MyPane chosenCell, GridPane gp, Figure new_fig){
        ParallelTransition pt = new ParallelTransition();

        chosenCell.toFront();

        TranslateTransition tt = new TranslateTransition();
        tt.setDuration(Duration.seconds(1));
        tt.setNode(chosenCell.getChildren().get(0));
        tt.setToX((cell.getX()-chosenX)*cell.getPrefWidth());
        tt.setToY((cell.getY()-chosenY)*cell.getPrefHeight());

        if(!cell.getChildren().isEmpty()){
            FadeTransition fd = new FadeTransition();
            fd.setDuration(Duration.seconds(1));
            fd.setNode(cell.getChildren().get(0));
            fd.setToValue(0.0);
            pt.getChildren().add(fd);
        }


        pt.getChildren().add(tt);

        pt.setOnFinished((ActionEvent event)->{
            if(!cell.getChildren().isEmpty())
                cell.getChildren().remove(0);

            cell.setCenter(new ImageView(g.get(chosenX, chosenY).getImg()));
            chosenCell.getChildren().remove(0);

            g.move(chosenX, chosenY, cell.getX(), cell.getY());

            gp.getChildren().remove(63);
            gp.getChildren().add(8*chosenY+chosenX, chosenCell);

            g.transform(cell.getX(), cell.getY(), new_fig);
            cell.setCenter(new ImageView(new_fig.getImg()));

            if(g.postCheck(g.turn())){
                if(g.mate(g.turn())){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Mate");
                    alert.setHeaderText("WIN!");
                    String winner;
                    if(g.turn()==COLOR.WHITE)
                        winner = "Darks";
                    else
                        winner = "Lights";

                    alert.setContentText("Congratulations! "+winner+" are win!");
                    alert.show();
                }else {
                    find:
                    {
                        for (int r = 0; r < 8; r++)
                            for (int col = 0; col < 8; col++)
                                if (g.get(col, r) != null && g.get(col, r).getClass() == King.class && g.get(col, r).getColor() == g.turn()) {

                                    gp.getChildren().get(8 * r + col).setStyle("-fx-background-color: rgb(255, 0, 0, 0.5);" +
                                            "-fx-border-color: rgba(0, 0, 0);");

                                    kingX = col;
                                    kingY = r;

                                    isCheck = true;
                                    break find;
                                }
                    }
                }
            }

            chosenX = -1;
            chosenY = -1;
        });

        pt.play();
    }


    private void castling(MyPane cell, MyPane chosenCell, GridPane gp){
        ParallelTransition pt = new ParallelTransition();

        chosenCell.toFront();

        TranslateTransition tt = new TranslateTransition();
        tt.setDuration(Duration.seconds(1));
        tt.setNode(chosenCell.getChildren().get(0));
        tt.setToX((cell.getX()-chosenX)*cell.getPrefWidth());
        tt.setToY((cell.getY()-chosenY)*cell.getPrefHeight());

        MyPane rogueEnd;
        if(cell.getX()==6) {
            gp.getChildren().get(8 * cell.getY() + 6).toFront();
            rogueEnd = (MyPane) gp.getChildren().get(8*cell.getY()+4);
        }
        else {
            gp.getChildren().get(8 * cell.getY()).toFront();
            rogueEnd = (MyPane) gp.getChildren().get(8*cell.getY()+2);
        }


        MyPane rogueCell = (MyPane)gp.getChildren().get(63);

        TranslateTransition tt1 = new TranslateTransition();
        tt1.setDuration(Duration.seconds(1));
        tt1.setNode(rogueCell.getChildren().get(0));
        if(cell.getX()==6)
            tt1.setToX(-2*cell.getPrefWidth());
        else
            tt1.setToX(3*cell.getPrefWidth());
        tt1.setToY(0);


        pt.setOnFinished((ActionEvent event)->{
            rogueEnd.setCenter(new ImageView(g.get(rogueCell.getX(), rogueCell.getY()).getImg()));
            rogueCell.getChildren().remove(0);
            g.move(rogueCell.getX(), rogueCell.getY(), rogueEnd.getX(), rogueEnd.getY());

            gp.getChildren().remove(63);
            if(cell.getX()==2)
                gp.getChildren().add(8*rogueCell.getY()+rogueCell.getX(), rogueCell);
            else
                gp.getChildren().add(8*rogueCell.getY()+rogueCell.getX()-1, rogueCell);

            cell.setCenter(new ImageView(g.get(chosenX, chosenY).getImg()));
            chosenCell.getChildren().remove(0);

            g.move(chosenX, chosenY, cell.getX(), cell.getY());
            g.changeTurn();

            gp.getChildren().remove(63);
            gp.getChildren().add(8*chosenY+chosenX, chosenCell);

            if(g.postCheck(g.turn())){

                if(g.mate(g.turn())) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Mate");
                    alert.setHeaderText("WIN!");
                    String winner;
                    if (g.turn() == COLOR.WHITE)
                        winner = "Darks";
                    else
                        winner = "Lights";

                    alert.setContentText("Congratulations! " + winner + " are win!");
                    alert.show();
                }else {
                    find:
                    {
                        for (int r = 0; r < 8; r++)
                            for (int col = 0; col < 8; col++)
                                if (g.get(col, r) != null && g.get(col, r).getClass() == King.class && g.get(col, r).getColor() == g.turn()) {

                                    gp.getChildren().get(8 * r + col).setStyle("-fx-background-color: rgb(255, 0, 0, 0.5);" +
                                            "-fx-border-color: rgba(0, 0, 0);");

                                    kingX = col;
                                    kingY = r;

                                    isCheck = true;
                                    break find;
                                }
                    }
                }
            }

            chosenX = -1;
            chosenY = -1;
        });

        pt.getChildren().addAll(tt, tt1);

        pt.play();
    }


    public static void main(String args[]){
        launch(args);
    }
}

class MyPane extends BorderPane{
    private int x;
    private int y;

    MyPane(int x, int y){
        super();
        this.x = x;
        this.y = y;
    }

    public int getX(){return this.x;}
    public int getY(){return this.y;}
}