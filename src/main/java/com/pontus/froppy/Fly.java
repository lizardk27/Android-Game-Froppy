package com.pontus.froppy;

import android.provider.Settings;

import java.io.IOException;

/**
 * Created by gabriel yibirin on 6/14/2016.
 */
public class Fly extends TileObject {


    private int step = 10;
    private int heading;
    private boolean shining = false;
    private final int fly_type;

    public static final int TYPE_FLY = 1;
    public static final int TYPE_BLUE_FLY = 2;
    public static final int TYPE_BEE = 3;
    public static final int TYPE_FIRE_FLY = 4;
    public static final int TYPE_QUEEN_BEE = 5;
    public static final int TYPE_DRAGON_FLY = 6;

    public static final int HEADING_LEFT_45   = 1;
    public static final int HEADING_LEFT_90   = 2;
    public static final int HEADING_LEFT_135  = 3;
    public static final int HEADING_RIGHT_45  = 4;
    public static final int HEADING_RIGHT_90  = 5;
    public static final int HEADING_RIGHT_135 = 6;



    public Fly(int FLY_TYPE, float x, float y, float width, float height) {
        super(0, x, y, width, height);
        fly_type = FLY_TYPE;
        try {
            this.setHeading(HEADING_RIGHT_90);
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }


      /*
      Heading will have values of
      1 = right45
      2 = right
      3 = right135
      4 = left135
      5 = left
      6 = left45
      */
    public void setHeading(int heading) throws IOException {
        if (heading < 1 || heading > 6) {
            throw new IOException("Heading should have values (1-6)");
        }
        this.heading = heading;
        switch (heading) {
            case HEADING_LEFT_45:
                switch (fly_type) {
                    case TYPE_FLY:
                        setIndex(3);
                        break;
                    case TYPE_BLUE_FLY:
                        setIndex(19);
                        break;
                    case TYPE_BEE:
                        setIndex(11);
                        break;
                    case TYPE_FIRE_FLY:
                        if (shining){
                            setIndex(43);
                        }else{
                            setIndex(35);
                        }
                        break;
                    case TYPE_QUEEN_BEE:
                        setIndex(51);
                        break;
                    case TYPE_DRAGON_FLY:
                        setIndex(27);
                        break;
                }
                break;
            case HEADING_LEFT_90:
                switch (fly_type) {
                    case TYPE_FLY:
                        setIndex(2);
                        break;
                    case TYPE_BLUE_FLY:
                        setIndex(18);
                        break;
                    case TYPE_BEE:
                        setIndex(10);
                        break;
                    case TYPE_FIRE_FLY:
                        if (shining){
                            setIndex(42);
                        }else{
                            setIndex(34);
                        }
                        break;
                    case TYPE_QUEEN_BEE:
                        setIndex(50);
                        break;
                    case TYPE_DRAGON_FLY:
                        setIndex(26);
                        break;
                }
                break;
            case HEADING_LEFT_135:
                switch (fly_type) {
                    case TYPE_FLY:
                        setIndex(1);
                        break;
                    case TYPE_BLUE_FLY:
                        setIndex(17);
                        break;
                    case TYPE_BEE:
                        setIndex(9);
                        break;
                    case TYPE_FIRE_FLY:
                        if (shining){
                            setIndex(41);
                        }else{
                            setIndex(33);
                        }
                        break;
                    case TYPE_QUEEN_BEE:
                        setIndex(49);
                        break;
                    case TYPE_DRAGON_FLY:
                        setIndex(25);
                        break;
                }
                break;
            case HEADING_RIGHT_45:
                switch (fly_type) {
                    case TYPE_FLY:
                        setIndex(4);
                        break;
                    case TYPE_BLUE_FLY:
                        setIndex(20);
                        break;
                    case TYPE_BEE:
                        setIndex(12);
                        break;
                    case TYPE_FIRE_FLY:
                        if (shining){
                            setIndex(44);
                        }else{
                            setIndex(36);
                        }
                        break;
                    case TYPE_QUEEN_BEE:
                        setIndex(52);
                        break;
                    case TYPE_DRAGON_FLY:
                        setIndex(28);
                        break;
                }
                break;
            case HEADING_RIGHT_90:
                switch (fly_type) {
                    case TYPE_FLY:
                        setIndex(5);
                        break;
                    case TYPE_BLUE_FLY:
                        setIndex(21);
                        break;
                    case TYPE_BEE:
                        setIndex(13);
                        break;
                    case TYPE_FIRE_FLY:
                        if (shining){
                            setIndex(45);
                        }else{
                            setIndex(37);
                        }
                        break;
                    case TYPE_QUEEN_BEE:
                        setIndex(53);
                        break;
                    case TYPE_DRAGON_FLY:
                        setIndex(29);
                        break;
                }
                break;
            case HEADING_RIGHT_135:
                switch (fly_type) {
                    case TYPE_FLY:
                        setIndex(6);
                        break;
                    case TYPE_BLUE_FLY:
                        setIndex(22);
                        break;
                    case TYPE_BEE:
                        setIndex(14);
                        break;
                    case TYPE_FIRE_FLY:
                        if (shining){
                            setIndex(46);
                        }else{
                            setIndex(38);
                        }
                        break;
                    case TYPE_QUEEN_BEE:
                        setIndex(54);
                        break;
                    case TYPE_DRAGON_FLY:
                        setIndex(30);
                        break;
                }
                break;
        }
    }

    public void move() throws IOException{
        if (fly_type==TYPE_FIRE_FLY){
            shining=!shining;
            setHeading(this.heading);
        }
        switch (heading) {
            case HEADING_RIGHT_45:
                x = x + step;
                y = y + step;
                break;
            case HEADING_RIGHT_90:
                x = x + step;
                break;
            case HEADING_RIGHT_135:
                x = x + step;
                y = y - step;
                break;
            case HEADING_LEFT_135:
                x = x - step;
                y = y - step;
                break;
            case HEADING_LEFT_90:
                x = x - step;
                break;
            case HEADING_LEFT_45:
                x = x - step;
                y = y + step;
                break;
            default:
                //do nothing
                break;

        }
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getHeading() {
        return heading;
    }

    public int getType() {
        return fly_type;
    }

}
