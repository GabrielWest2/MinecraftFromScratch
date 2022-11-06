package voxelgame.block;

import org.joml.Vector2i;

/**
 * @author gabed
 * @Date 10/12/2022
 */
public class BlockTexture {
    //The texture ID of each face
    private final Vector2i TOP_FACE, BOTTOM_FACE, LEFT_FACE, RIGHT_FACE, FRONT_FACE, BACK_FACE;

    public BlockTexture(Vector2i top_face, Vector2i bottom_face, Vector2i left_face, Vector2i right_face, Vector2i front_face, Vector2i back_face) {
        TOP_FACE = top_face;
        BOTTOM_FACE = bottom_face;
        LEFT_FACE = left_face;
        RIGHT_FACE = right_face;
        FRONT_FACE = front_face;
        BACK_FACE = back_face;
    }

    public BlockTexture(Vector2i tex) {
        TOP_FACE = tex;
        BOTTOM_FACE = tex;
        LEFT_FACE = tex;
        RIGHT_FACE = tex;
        FRONT_FACE = tex;
        BACK_FACE = tex;
    }

    public BlockTexture(Vector2i topBottom, Vector2i sides) {
        TOP_FACE = topBottom;
        BOTTOM_FACE = topBottom;
        LEFT_FACE = sides;
        RIGHT_FACE = sides;
        FRONT_FACE = sides;
        BACK_FACE = sides;
    }

    public Vector2i getFaceTexture(int id) {
        if (id == 0)
            return TOP_FACE;
        if (id == 1)
            return BOTTOM_FACE;
        if (id == 2)
            return LEFT_FACE;
        if (id == 3)
            return RIGHT_FACE;
        if (id == 4)
            return FRONT_FACE;
        if (id == 5)
            return BACK_FACE;
        return TOP_FACE;
    }
}
