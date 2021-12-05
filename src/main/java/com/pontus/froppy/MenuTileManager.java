package com.pontus.froppy;

/**
 * Created by usuario on 5/4/2016.
 */
public class MenuTileManager extends ImageTileManager {

    private static final float RI_TEXT_UV_BOX_WIDTH = 0.3333333333f;
    private static final float RI_TEXT_WIDTH = 250f;

    public MenuTileManager() {
        super(new int[]{
                225, 225, 225,
                80, 105, 105,
                410, 200, 410,}, RI_TEXT_UV_BOX_WIDTH, RI_TEXT_WIDTH, 10, 3);

    }
}
