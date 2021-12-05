package com.pontus.froppy;

/**
 * Created by usuario on 5/4/2016.
 */
public class FroppyTileManager extends ImageTileManager {

    private static final float RI_TEXT_UV_BOX_WIDTH = 0.1666666666666667F;
    private static final float RI_TEXT_WIDTH = 250f;

    public FroppyTileManager() {
        super(new int[]{
                170, 170, 170, 170, 170, 170,
                170, 170, 170, 170, 170, 170,
                170, 170, 170, 170, 170, 170,
                170, 170, 170, 170, 170, 170,
                170, 170, 170, 170, 170, 170,
                170, 170, 170, 170, 170, 170}, RI_TEXT_UV_BOX_WIDTH, RI_TEXT_WIDTH, 37, 6);

    }
}
