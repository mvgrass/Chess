import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.HashMap;

enum COLOR {BLACK, WHITE}

abstract class Figure {
    protected boolean isFirst;

    public void setFirst(boolean f){isFirst = f;};
    public boolean isFirst(){return isFirst;}

    Figure(int x, int y, COLOR col){
        setX(x);
        setY(y);
        setColor(col);
    }

    private int x;
    private int y;

    private COLOR color;

    protected Image img;

    int getX(){return x;}
    int getY(){return y;}
    COLOR getColor(){return this.color;}
    Image getImg(){return  this.img;}

    void setX(int x){this.x = x;}
    void setY(int y){this.y = y;}
    void setColor(COLOR col){this.color = col;}

    abstract void checkDesk(ArrayList<ArrayList<Figure>> desk, HashMap<Integer, Boolean> map);

    protected boolean checkCell(int X, int Y, ArrayList<ArrayList<Figure>> desk, HashMap<Integer, Boolean> map){
        Figure fig = desk.get(Y).get(X);
        if(fig==null){
            map.put(8*Y+X, false);
            return true;
        }
        else if(fig.getColor()!=this.getColor()) {
            map.put(8 * Y + X, true);
            return false;
        }

        return false;
    }

    void moveOn(int X, int Y){
        this.setX(X);
        this.setY(Y);

        this.setFirst(false);
    }
}

class King extends Figure{

    King(int x, int y, COLOR col){
        super(x,y,col);

        this.isFirst = true;

        if(col==COLOR.BLACK)
            this.img = new Image("/images/Chess_kdt60.png");
        else
            this.img = new Image("/images/Chess_klt60.png");
    }

    void checkDesk(ArrayList<ArrayList<Figure>> desk, HashMap<Integer, Boolean> map){
        if(this.getX()-1>=0)
            this.checkCell(this.getX()-1, this.getY(), desk, map);

        if(this.getX()+1<8)
            this.checkCell(this.getX()+1, this.getY(), desk, map);

        if(this.getY()+1<8)
            this.checkCell(this.getX(), this.getY()+1, desk, map);

        if(this.getY()-1>=0)
            this.checkCell(this.getX(), this.getY()-1, desk, map);

        if(this.getX()-1>=0&&this.getY()-1>=0)
            this.checkCell(this.getX()-1, this.getY()-1, desk, map);

        if(this.getX()-1>=0&&this.getY()+1<8)
            this.checkCell(this.getX()-1, this.getY()+1, desk, map);

        if(this.getX()+1<8&&this.getY()-1>=0)
            this.checkCell(this.getX()+1, this.getY()-1, desk, map);

        if(this.getX()+1<8&&this.getY()+1<8)
            this.checkCell(this.getX()+1, this.getY()+1, desk, map);


        if(isFirst()){
            if(this.getColor()==COLOR.WHITE){
                Figure fig1 = desk.get(7).get(7);
                Figure fig2 = desk.get(7).get(0);
                if(desk.get(7).get(5)==null&&desk.get(7).get(6)==null&&fig1!=null&&fig1.isFirst())
                    map.put(62, false);

                if(desk.get(7).get(1)==null&&desk.get(7).get(2)==null&&desk.get(7).get(3)==null&&fig2!=null&&fig2.isFirst())
                    map.put(58, false);
            }else{
                Figure fig1 = desk.get(0).get(7);
                Figure fig2 = desk.get(0).get(0);
                if(desk.get(0).get(5)==null&&desk.get(0).get(6)==null&&fig1!=null&&fig1.isFirst())
                    map.put(6, false);

                if(desk.get(0).get(1)==null&&desk.get(0).get(2)==null&&desk.get(0).get(3)==null&&fig2!=null&&fig2.isFirst())
                    map.put(2, false);

            }
        }

    }

}

class Queen extends Figure{
    Queen(int x, int y, COLOR col){
        super(x,y,col);

        if(col == COLOR.BLACK)
            this.img = new Image("/images/Chess_qdt60.png");
        else
            this.img = new Image("/images/Chess_qlt60.png");
    }

    void checkDesk(ArrayList<ArrayList<Figure>> desk, HashMap<Integer, Boolean> map){
        int i;
        int j;

        i = 1;
        while(this.getX()-i>=0&&checkCell(this.getX()-i, this.getY(), desk, map))
            i++;

        i = 1;
        while(this.getX()+i<8&&checkCell(this.getX()+i, this.getY(), desk, map))
            i++;

        j = 1;
        while(this.getY()-j>=0&&checkCell(this.getX(), this.getY()-j, desk, map))
            j++;

        j = 1;
        while(this.getY()+j<8&&checkCell(this.getX(), this.getY()+j, desk, map))
            j++;

        i = j = 1;
        while(this.getX()-i>=0&&this.getY()+j<8&&checkCell(this.getX()-i, this.getY()+j, desk, map)){
            i++;j++;
        }

        i = j = 1;
        while(this.getX()+i<8&&this.getY()-j>=0&&checkCell(this.getX()+i, this.getY()-j, desk, map)){
            i++;j++;
        }

        i = j = 1;
        while(this.getX()+i<8&&this.getY()+j<8&&checkCell(this.getX()+i, this.getY()+j, desk, map)){
            i++;j++;
        }

        i = j = 1;
        while(this.getX()-i>=0&&this.getY()-j>=0&&checkCell(this.getX()-i, this.getY()-j, desk, map)){
            i++;j++;
        }
    }

}

class Rogue extends Figure{

    Rogue(int x, int y, COLOR col){
        super(x,y,col);

        this.isFirst = true;

        if(col == COLOR.BLACK)
            this.img = new Image("/images/Chess_rdt60.png");
        else
            this.img = new Image("/images/Chess_rlt60.png");
    }

    void checkDesk(ArrayList<ArrayList<Figure>> desk, HashMap<Integer, Boolean> map){
        int i;

        i = 1;
        while(this.getX()-i>=0&&checkCell(this.getX()-i, this.getY(), desk, map))
            i++;

        i = 1;
        while(this.getX()+i<8&&checkCell(this.getX()+i, this.getY(), desk, map))
            i++;

        i = 1;
        while(this.getY()-i>=0&&checkCell(this.getX(), this.getY()-i, desk, map))
            i++;

        i = 1;
        while(this.getY()+i<8&&checkCell(this.getX(), this.getY()+i, desk, map))
            i++;
    }
}

class Bishop extends Figure{
    Bishop(int x, int y, COLOR col){
        super(x,y,col);

        if(col == COLOR.BLACK)
            this.img = new Image("/images/Chess_bdt60.png");
        else
            this.img = new Image("/images/Chess_blt60.png");
    }

    void checkDesk(ArrayList<ArrayList<Figure>> desk, HashMap<Integer, Boolean> map){
        int i,j;

        i = j = 1;
        while(this.getX()-i>=0&&this.getY()+j<8&&checkCell(this.getX()-i, this.getY()+j, desk, map)){
            i++;j++;
        }

        i = j = 1;
        while(this.getX()+i<8&&this.getY()-j>=0&&checkCell(this.getX()+i, this.getY()-j, desk, map)){
            i++;j++;
        }

        i = j = 1;
        while(this.getX()+i<8&&this.getY()+j<8&&checkCell(this.getX()+i, this.getY()+j, desk, map)){
            i++;j++;
        }

        i = j = 1;
        while(this.getX()-i>=0&&this.getY()-j>=0&&checkCell(this.getX()-i, this.getY()-j, desk, map)){
            i++;j++;
        }

    }
}

class Knight extends Figure{
    Knight(int x, int y, COLOR col){
        super(x,y,col);

        if(col == COLOR.BLACK)
            this.img = new Image("/images/Chess_ndt60.png");
        else
            this.img = new Image("/images/Chess_nlt60.png");
    }

    void checkDesk(ArrayList<ArrayList<Figure>> desk, HashMap<Integer, Boolean> map){
        if(this.getX()-1>=0&&this.getY()-2>=0)
            this.checkCell(this.getX()-1, this.getY()-2, desk, map);

        if(this.getX()+1<8&&this.getY()-2>=0)
            this.checkCell(this.getX()+1, this.getY()-2, desk, map);

        if(this.getX()+2<8&&this.getY()-1>=0)
            this.checkCell(this.getX()+2, this.getY()-1, desk, map);

        if(this.getX()+2<8&&this.getY()+1<8)
            this.checkCell(this.getX()+2, this.getY()+1, desk, map);

        if(this.getX()+1<8&&this.getY()+2<8)
            this.checkCell(this.getX()+1, this.getY()+2, desk, map);

        if(this.getX()-1>=0&&this.getY()+2<8)
            this.checkCell(this.getX()-1, this.getY()+2, desk, map);

        if(this.getX()-2>=0&&this.getY()+1<8)
            this.checkCell(this.getX()-2, this.getY()+1, desk, map);

        if(this.getX()-2>=0&&this.getY()-1>=0)
            this.checkCell(this.getX()-2, this.getY()-1, desk, map);
    }
}

class Pawn extends Figure {

    Pawn(int x, int y, COLOR col) {
        super(x, y, col);

        isFirst = true;

        if (col == COLOR.BLACK)
            this.img = new Image("/images/Chess_pdt60.png");
        else
            this.img = new Image("/images/Chess_plt60.png");
    }

    void checkDesk(ArrayList<ArrayList<Figure>> desk, HashMap<Integer, Boolean> map) {

        if (this.getColor() == COLOR.WHITE) {
            if(this.getY()-1>=0) {
                if (desk.get(this.getY() - 1).get(this.getX()) == null) {
                    map.put(8 * (this.getY() - 1) + this.getX(), false);

                    if(this.isFirst&&desk.get(this.getY() - 2).get(this.getX()) == null)
                        map.put(8 * (this.getY() - 2) + this.getX(), false);
                }

            }

            if(this.getY()-1>=0&&this.getX()-1>=0)
                if (desk.get(this.getY() - 1).get(this.getX() - 1) != null && desk.get(this.getY() - 1).get(this.getX() - 1).getColor() == COLOR.BLACK)
                    map.put(8 * (this.getY() - 1) + this.getX() - 1, true);

            if(this.getY()-1>=0&&this.getX()+1<8)
                if (desk.get(this.getY() - 1).get(this.getX() + 1) != null && desk.get(this.getY() - 1).get(this.getX() + 1).getColor() == COLOR.BLACK)
                    map.put(8 * (this.getY() - 1) + this.getX() + 1, true);


        } else {
            if(this.getY()+1<8) {
                if (desk.get(this.getY() + 1).get(this.getX()) == null) {
                    map.put(8 * (this.getY() + 1) + this.getX(), false);

                    if(this.isFirst&&desk.get(this.getY() + 2).get(this.getX()) == null)
                        map.put(8 * (this.getY() + 2) + this.getX(), false);
                }

            }

            if(this.getY()+1<8&&this.getX()-1>=0)
                if (desk.get(this.getY() + 1).get(this.getX() - 1) != null && desk.get(this.getY() + 1).get(this.getX() - 1).getColor() == COLOR.WHITE)
                    map.put(8 * (this.getY() + 1) + this.getX() - 1, true);

            if(this.getY()+1<8&&this.getX()+1<8)
                if (desk.get(this.getY() + 1).get(this.getX() + 1) != null && desk.get(this.getY() + 1).get(this.getX() + 1).getColor() == COLOR.WHITE)
                    map.put(8 * (this.getY() + 1) + this.getX() + 1, true);
        }
    }
}
