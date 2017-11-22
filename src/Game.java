import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Artem on 07.06.2017.
 */


class Game {
    private ArrayList<ArrayList<Figure>> desk;

    private COLOR turn;

    ArrayList<Figure> Lights;
    ArrayList<Figure> Darks;

    Game(){
        desk = new ArrayList<>(8);
        Lights = new ArrayList<>();
        Darks = new ArrayList<>();
        for(int i = 0;i<8;i++) {
            desk.add(new ArrayList<Figure>(8));
            ArrayList<Figure> subArr = desk.get(i);
            for (int j = 0;j<8;j++)
                subArr.add(null);

        }

        for(int i = 0;i<8;i++){

            desk.get(1).set(i, new Pawn(i, 1, COLOR.BLACK));
            Darks.add(desk.get(1).get(i));


            desk.get(6).set(i, new Pawn(i, 6 , COLOR.WHITE));
            Lights.add(desk.get(6).get(i));
        }

        ArrayList<Figure> subLights = desk.get(7);
        ArrayList<Figure> subDarks = desk.get(0);

        subDarks.set(0, new Rogue(0, 0, COLOR.BLACK));
        subDarks.set(7, new Rogue(7, 0, COLOR.BLACK));
        subLights.set(0, new Rogue(0, 7, COLOR.WHITE));
        subLights.set(7, new Rogue(7, 7, COLOR.WHITE));

        subDarks.set(1, new Knight(1, 0, COLOR.BLACK));
        subDarks.set(6, new Knight(6, 0, COLOR.BLACK));
        subLights.set(1, new Knight(1, 7, COLOR.WHITE));
        subLights.set(6, new Knight(6, 7, COLOR.WHITE));

        subDarks.set(2, new Bishop(2, 0, COLOR.BLACK));
        subDarks.set(5, new Bishop(5, 0, COLOR.BLACK));
        subLights.set(2, new Bishop(2, 7, COLOR.WHITE));
        subLights.set(5, new Bishop(5, 7, COLOR.WHITE));

        subDarks.set(3, new Queen(3, 0, COLOR.BLACK));
        subDarks.set(4, new King(4, 0, COLOR.BLACK));

        subLights.set(3, new Queen(3, 7, COLOR.WHITE));
        subLights.set(4, new King(4, 7, COLOR.WHITE));

        for(int i = 0;i<8;i++){
            Darks.add(subDarks.get(i));
            Lights.add(subLights.get(i));
        }

        turn = COLOR.WHITE;

    }

    public HashMap<Integer, Boolean> getPossibleCells(int X, int Y){
        HashMap<Integer, Boolean> ans = new HashMap<>();

        desk.get(Y).get(X).checkDesk(this.desk, ans);

        return ans;
    }

    public COLOR turn(){return this.turn;}

    public void move(int startX, int startY, int endX, int endY){
        Figure f = desk.get(startY).get(startX);

        Figure del = desk.get(endY).get(endX);
        if(del!=null){
            if(del.getColor()==COLOR.WHITE)
                Lights.remove(del);
            else
                Darks.remove(del);
        }

        f.moveOn(endX, endY);

        desk.get(endY).set(endX, f);
        desk.get(startY).set(startX, null);

        changeTurn();
    }


    public void changeTurn(){
        if(this.turn()==COLOR.WHITE)
            this.turn = COLOR.BLACK;
        else
            this.turn = COLOR.WHITE;
    }

    public void transform(int X, int Y, Figure new_fig){
        if(desk.get(Y).get(X).getColor()==COLOR.WHITE) {
            Lights.remove(desk.get(Y).get(X));
            Lights.add(new_fig);
        }
        else {
            Darks.remove(desk.get(Y).get(X));
            Darks.add(new_fig);
        }
        desk.get(Y).set(X, new_fig);
    }

    public boolean preCheck(COLOR whom, int startX, int startY, int endX, int endY){
        Figure fig1 = desk.get(startY).get(startX);
        Figure fig2 = desk.get(endY).get(endX);

        boolean isFirst = fig1.isFirst();

        King k = null;
        ArrayList<Figure>enemy;
        if(whom == COLOR.WHITE){
            enemy = Darks;
        }else{
            enemy = Lights;
        }

        move(startX, startY, endX, endY);

        find:
        {
            for (int i = 0; i < 8; i++)
                for (int j = 0; j < 8; j++)
                    if (desk.get(i).get(j) != null && desk.get(i).get(j).getClass() == King.class && desk.get(i).get(j).getColor() == whom) {
                        k = (King) desk.get(i).get(j);
                        break find;
                    }
        }

        HashMap<Integer, Boolean> possibleCells;
        for(int i = 0;i< enemy.size();i++){
            possibleCells = new HashMap<>();
            enemy.get(i).checkDesk(desk,possibleCells);
            if(possibleCells.get(8*k.getY()+k.getX())!=null) {
                desk.get(startY).set(startX,fig1);
                fig1.moveOn(startX, startY);
                fig1.setFirst(isFirst);
                if(fig2!=null) {
                    enemy.add(fig2);
                    desk.get(endY).set(endX, fig2);
                }else
                    desk.get(endY).set(endX, null);
                this.changeTurn();
                return true;
            }
        }

        desk.get(startY).set(startX,fig1);
        fig1.moveOn(startX, startY);
        fig1.setFirst(isFirst);
        if(fig2!=null) {
            enemy.add(fig2);
            desk.get(endY).set(endX, fig2);
        }else
            desk.get(endY).set(endX, null);

        this.changeTurn();
        return false;
    }

    public boolean postCheck(COLOR whom){

        King k = null;
        ArrayList<Figure>enemy;
        if(whom == COLOR.WHITE){
            enemy = Darks;
        }else{
            enemy = Lights;
        }

        find:
        {
            for (int i = 0; i < 8; i++)
                for (int j = 0; j < 8; j++)
                    if (desk.get(i).get(j) != null && desk.get(i).get(j).getClass() == King.class && desk.get(i).get(j).getColor() == whom) {
                        k = (King) desk.get(i).get(j);
                        break find;
                    }
        }

        HashMap<Integer, Boolean> possibleCells;
        for(int i = 0;i< enemy.size();i++){
            possibleCells = new HashMap<>();
            enemy.get(i).checkDesk(desk,possibleCells);
            if(possibleCells.get(8*k.getY()+k.getX())!=null) {
                return true;
            }
        }

        return false;
    }

    public boolean mate(COLOR whom){
        ArrayList<Figure> figures;
        figures = (whom == COLOR.WHITE)?Lights:Darks;

        HashMap<Integer, Boolean> possibleCells;
        for(int i = 0;i<figures.size();i++){
            possibleCells = new HashMap<>();
            Figure fig = figures.get(i);
            fig.checkDesk(desk,possibleCells);

            //Блокировка рокировки при проверке на мат
            if(fig!=null&&fig.getClass()==King.class&&fig.isFirst()){
                possibleCells.remove(2);
                possibleCells.remove(6);
                possibleCells.remove(62);
                possibleCells.remove(58);
            }

            final boolean[] isMate = {true};
            possibleCells.forEach((Integer k, Boolean v)->{
                if(!this.preCheck(whom, fig.getX(), fig.getY(), k%8, k/8)){
                    isMate[0] = false;
                }
            });

            if(!isMate[0])
                return false;
        }

        return true;
    }

    public boolean Castling(int chosenX, int chosenY, int X, int Y){
        if(this.postCheck(this.turn())||
                (X==6&&(this.preCheck(this.turn(),chosenX, chosenY, 5, chosenY))) ||
                (X==2&&this.preCheck(this.turn(),chosenX, chosenY, 3, chosenY)))
            return false;
        else{
            if(X==6) {
                this.move(7, chosenY, 5, chosenY);
                this.changeTurn();
                if (this.preCheck(this.turn(), chosenX, chosenY, X, Y)) {
                    this.move(5,chosenY, 7, chosenY);
                    this.changeTurn();
                    this.get(7, chosenY).setFirst(true);
                    return false;
                }else{
                    this.move(5, chosenY, 7,chosenY);
                    this.changeTurn();
                    this.get(7, chosenY).setFirst(true);
                    return true;
                }
            }else{
                this.move(0, chosenY, 3, chosenY);
                this.changeTurn();
                if (this.preCheck(this.turn(), chosenX, chosenY, X, Y)) {
                    this.move(3, chosenY, 0, chosenY);
                    this.changeTurn();
                    this.get(0, chosenY).setFirst(true);
                    return false;
                }else{
                    this.move(3, chosenY, 0, chosenY);
                    this.changeTurn();
                    this.get(0, chosenY).setFirst(true);
                    return true;
                }
            }
        }
    }

    public Figure get(int x, int y){
        return desk.get(y).get(x);
    }
}
