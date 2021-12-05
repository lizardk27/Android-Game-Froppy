package com.pontus.froppy;

/**
 * Created by usuario on 5/4/2016.
 */
public class LevelBannersTileManager extends ImageTileManager {

    private static final float RI_TEXT_UV_BOX_WIDTH = 0.25f;
    private static final float RI_TEXT_WIDTH = 250f;

    public LevelBannersTileManager() {
        super(new int[]{
                250, 250, 250, 250,
                250, 250, 250, 250,
                250, 250, 250, 250,
                250, 250, 250, 250}, RI_TEXT_UV_BOX_WIDTH, RI_TEXT_WIDTH, 17, 4);

    }
}
