package voxelengine.gui;

import org.joml.Vector2i;

/**
 * @author gabed
 * @Date 10/24/2022
 */
public class ScrollFrameEntry implements UIElement {
    @Override
    public boolean Render() {
        return false;
    }

    @Override
    public Vector2i getSize() {
        return null;
    }

    @Override
    public ScreenConstraint getConstraints() {
        return null;
    }

    @Override
    public int getVAO() {
        return 0;
    }


    @Override
    public void resetVAO() {

    }
}
