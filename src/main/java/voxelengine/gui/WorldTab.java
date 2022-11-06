package voxelengine.gui;

import org.joml.Vector2i;
import org.joml.Vector4f;
import voxelgame.SaveWorld;

/**
 * @author gabed
 * @Date 10/24/2022
 */
public class WorldTab extends ScrollFrameEntry {
    private final int height;
    private final SaveWorld world;
    Image image;
    Button rect;
    Text titleLine;
    Text line2;
    Text line3;
    private int position;


    public WorldTab(int index, SaveWorld world) {
        this.world = world;
        this.height = 125 + index * 150;
        this.position = this.height;
        rect = new Button(new ScreenConstraint(null).addConstraint(Side.CENTER_X, "0px"), " ", new Vector2i(800, 125), new Vector4f(0, 0, 0, 0.6f), () -> UIManager.setSelectedTab(index));
        image = new Image(new ScreenConstraint(rect).addConstraint(Side.TOP, "2px").addConstraint(Side.LEFT, "2px"), 10, new Vector2i(120, 120));
        titleLine = new Text(new ScreenConstraint(image).addConstraint(Side.TOP, "20px").addConstraint(Side.LEFT, "130px"), world.getName(), 23, new Vector4f(1));
        line2 = new Text(new ScreenConstraint(titleLine).addConstraint(Side.TOP, "30px"), world.getPath() + " (12/22/13 12:47 PM)", 20, new Vector4f(0.5f, 0.5f, 0.5f, 1.0f));
        line3 = new Text(new ScreenConstraint(line2).addConstraint(Side.TOP, "30px"), "Survival Mode", 20, new Vector4f(0.5f, 0.5f, 0.5f, 1.0f));
    }

    public void updatePosition(float scrollPos, float max) {
        this.position = (int) (height - scrollPos * max);
        rect.getConstraints().addConstraint(Side.TOP, position + "px");
        rect.resetVAO();
        image.resetVAO();
        titleLine.resetVAO();
        line2.resetVAO();
        line3.resetVAO();
    }

    @Override
    public boolean Render() {
        rect.Render();
        image.Render();
        titleLine.Render();
        line2.Render();
        line3.Render();
        return false;
    }

    public SaveWorld getWorld() {
        return world;
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
        rect.resetVAO();
        image.resetVAO();
        titleLine.resetVAO();
        line2.resetVAO();
        line3.resetVAO();
    }
}
