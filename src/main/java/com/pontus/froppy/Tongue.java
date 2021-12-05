package com.pontus.froppy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.RectF;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by gabriel yibirin on 6/27/2016.
 */
public class Tongue extends Sprite {

    private float angle;
    private float scale;
    private RectF base;
    private PointF translation;
    private boolean isTouched = false;
    private int texture_position;
    // Misc
    private Context mContext;
    // Geometric variables
    private static float vertices[];
    private static short indices[];
    private static float uvs[];
    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;
    private FloatBuffer uvBuffer;


    public Tongue(Context context, float scale, float angle, PointF initial_pos, RectF base) {
        super(context, scale, angle, initial_pos, base);
        mContext = context;
        this.base = base;
        this.translation = initial_pos;
        this.scale = scale;
        this.angle = angle;
    }

    public PointF getTranslation() {
        return translation;
    }

    public float getWidth() {
        return base.width();
    }

    public float getHeight() {
        return base.height();
    }

    public void setWidth(float width) {
        float curr_width = base.width();
        float txl = base.left;
        float txr = base.right;
        float txc = (txr - txl) / 2;

        base.left = txc - (width / 2);
        base.right = txc + (width / 2);
    }

    public void growFromLeft(float height) {
        base.right = height;
    }


    public void moveTo(float xpos, float ypos) {
        // Update our location.
        translation.x = xpos;
        translation.y = ypos;
    }

    public void move(float deltax, float deltay) {
        translation.x += deltax;
        translation.y += deltay;
    }

    public int getTexturePos() {
        return texture_position;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getAngle() {
        return angle;
    }

    public float getScale() {
        return scale;
    }

    public float getX() {
        return translation.x;
    }

    public float getY() {
        return translation.y;
    }

    public void setX(float xpos) {
        translation.x = xpos;
    }

    public void setY(float ypos) {
        translation.y = ypos;
    }

    public RectF getBase() {
        return base;
    }

    public ShortBuffer getDrawListBuffer() {
        return drawListBuffer;
    }

    public static short[] getIndices() {
        return indices;
    }

    public int getTexture_position() {
        return texture_position;
    }

    public FloatBuffer getUvBuffer() {
        return uvBuffer;
    }

    public static float[] getUvs() {
        return uvs;
    }

    public FloatBuffer getVertexBuffer() {
        return vertexBuffer;
    }

    public static float[] getVertices() {
        return vertices;
    }

    public float[] getTransformedVertices() {
        // Start with scaling
        float x1 = base.left * scale;
        float x2 = base.right * scale;
        float y1 = base.bottom * scale;
        float y2 = base.top * scale;

        // We now detach from our Rect because when rotating,
        // we need the seperate points, so we do so in opengl order
        PointF one = new PointF(x1, y2);
        PointF two = new PointF(x1, y1);
        PointF three = new PointF(x2, y1);
        PointF four = new PointF(x2, y2);

        // We create the sin and cos function once,
        // so we do not have calculate them each time.
        float s = (float) Math.sin(angle);
        float c = (float) Math.cos(angle);

        // Then we rotate each point
        one.x = x1 * c - y2 * s;
        one.y = x1 * s + y2 * c;
        two.x = x1 * c - y1 * s;
        two.y = x1 * s + y1 * c;
        three.x = x2 * c - y1 * s;
        three.y = x2 * s + y1 * c;
        four.x = x2 * c - y2 * s;
        four.y = x2 * s + y2 * c;

        // Finally we translate the sprite to its correct position.
        one.x += translation.x;
        one.y += translation.y;
        two.x += translation.x;
        two.y += translation.y;
        three.x += translation.x;
        three.y += translation.y;
        four.x += translation.x;
        four.y += translation.y;

        // We now return our float array of vertices.
        return new float[]
                {
                        one.x, one.y, 0.0f,
                        two.x, two.y, 0.0f,
                        three.x, three.y, 0.0f,
                        four.x, four.y, 0.0f,
                };
    }

    public void SetupImage(String image_resource_name, int pos, int[] texturenames) {

        SetupTriangle();

        // Create our UV coordinates.
        uvs = new float[]{
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f
        };

        texture_position = pos;

        // The texture buffer
        ByteBuffer bb = ByteBuffer.allocateDirect(uvs.length * 4);
        bb.order(ByteOrder.nativeOrder());
        uvBuffer = bb.asFloatBuffer();
        uvBuffer.put(uvs);
        uvBuffer.position(0);

        // Generate Textures
        GLES20.glGenTextures(texturenames.length, texturenames, 0);

        // Retrieve our image from resources.
        int id = mContext.getResources().getIdentifier(image_resource_name, null, mContext.getPackageName());

        // Temporary create a bitmap
        Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), id);

        int textureIntVal = GLES20.GL_TEXTURE0 + pos;


        GLES20.glActiveTexture(textureIntVal);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[pos]);

        // Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);

        // We are done using the bitmap so we should recycle it.
        bmp.recycle();

    }


    private void SetupTriangle() {
        // Get information of sprite.
        vertices = this.getTransformedVertices();

        // The order of vertexrendering for a quad
        indices = new short[]{0, 1, 2, 0, 2, 3};

        // The vertex buffer.
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(indices.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(indices);
        drawListBuffer.position(0);
    }

    public void UpdateSprite() {
        // Get new transformed vertices
        vertices = this.getTransformedVertices();

        // The vertex buffer.
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
    }

    public void handleActionDownTransformedVertices(float eventX, float eventY) {

        float[] transfVert = getTransformedVertices();

        float x1 = transfVert[0];
        float y1 = transfVert[1];
        float x2 = transfVert[3];
        float y2 = transfVert[4];
        float x3 = transfVert[6];
        float y3 = transfVert[7];
        float x4 = transfVert[9];
        float y4 = transfVert[10];

        float minX;
        if (x1 < x2) {
            minX = x1;
        } else {
            minX = x2;
        }
        if (minX < x3) {
        } else {
            minX = x3;
        }
        if (minX < x4) {
        } else {
            minX = x4;
        }


        float minY;
        if (y1 < y2) {
            minY = y1;
        } else {
            minY = y2;
        }
        if (minY < y3) {
        } else {
            minY = y3;
        }
        if (minY < y4) {
        } else {
            minY = y4;
        }

        float maxX;
        if (x1 > x2) {
            maxX = x1;
        } else {
            maxX = x2;
        }
        if (maxX > x3) {
        } else {
            maxX = x3;
        }
        if (maxX > x4) {
        } else {
            maxX = x4;
        }

        float maxY;
        if (y1 > y2) {
            maxY = y1;
        } else {
            maxY = y2;
        }
        if (maxY > y3) {
        } else {
            maxY = y3;
        }
        if (maxY > y4) {
        } else {
            maxY = y4;
        }

        if (eventX >= minX && eventX <= maxX){
            if (eventY >= minY && eventY <= maxY) {
                setTouched(true);
            } else {
                setTouched(false);
            }
        } else{
            setTouched(false);
        }
    }


    public void setNotTouched() {
        isTouched = false;
    }

    private void setTouched(boolean touch) {
        isTouched = touch;
    }

    public boolean isTouched() {
        return isTouched;
    }

}
