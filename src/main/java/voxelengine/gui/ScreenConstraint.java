package voxelengine.gui;

import org.joml.Vector2i;
import voxelengine.engine.DisplayManager;

import java.util.HashMap;

/**
 * @author gabed
 * @Date 10/16/2022
 */
public class ScreenConstraint {
    private final HashMap<Side, String> constraints = new HashMap<>();
    private final UIElement relativeTo;

    public ScreenConstraint(UIElement relativeTo) {
        this.relativeTo = relativeTo;
    }

    public ScreenConstraint addConstraint(Side side, String string) {
        if (constraints.containsKey(Side.values()[side.opposite]) && side.opposite != 4 && side.opposite != 5) {
            constraints.remove(Side.values()[side.opposite]);
        }
        constraints.put(side, string);
        return this;
    }

    public String getConstraint(Side side){
        return constraints.get(side);
    }

    public Vector2i calculatePixelPosition(Vector2i size) {
        int pixelX = 0;
        int pixelY = 0;

        if (relativeTo == null) {
            if (constraints.containsKey(Side.LEFT)) {
                pixelX = getPixelValue(constraints.get(Side.LEFT), Side.LEFT);
            } else if (constraints.containsKey(Side.RIGHT)) {
                pixelX = DisplayManager.WIDTH - getPixelValue(constraints.get(Side.RIGHT), Side.RIGHT) - size.x;
            }

            if (constraints.containsKey(Side.TOP)) {
                pixelY = getPixelValue(constraints.get(Side.TOP), Side.TOP);
            } else if (constraints.containsKey(Side.BOTTOM)) {
                pixelY = DisplayManager.HEIGHT - getPixelValue(constraints.get(Side.BOTTOM), Side.BOTTOM) - size.y;
            }

            if (constraints.containsKey(Side.CENTER_X)) {
                pixelX = (int) ((DisplayManager.WIDTH / 2f) - (size.x / 2f)) + getPixelValue(constraints.get(Side.CENTER_X), Side.CENTER_X);
            }

            if (constraints.containsKey(Side.CENTER_Y)) {
                pixelY = (int) ((DisplayManager.HEIGHT / 2f) - (size.y / 2f)) + getPixelValue(constraints.get(Side.CENTER_Y), Side.CENTER_Y);
            }
        } else {
            Vector2i relativePos = relativeTo.getConstraints().calculatePixelPosition(relativeTo.getSize());
            if (constraints.containsKey(Side.LEFT)) {
                pixelX = getPixelValue(constraints.get(Side.LEFT), Side.LEFT);
            } else if (constraints.containsKey(Side.RIGHT)) {
                pixelX = DisplayManager.WIDTH - getPixelValue(constraints.get(Side.RIGHT), Side.RIGHT) - size.x;
            }
            pixelX += relativePos.x;
            if (constraints.containsKey(Side.TOP)) {
                pixelY = getPixelValue(constraints.get(Side.TOP), Side.TOP);
            } else if (constraints.containsKey(Side.BOTTOM)) {
                pixelY = DisplayManager.HEIGHT - getPixelValue(constraints.get(Side.BOTTOM), Side.BOTTOM) - size.y;
            }
            pixelY += relativePos.y;
        }
        return new Vector2i(pixelX, pixelY);
    }

    public int getPixelValue(String str, Side s) {
        int i = Integer.parseInt(str.replaceAll("px", "").replaceAll("%", ""));
        if (str.endsWith("px")) {
            return i;
        }
        if (str.endsWith("%")) {
            if (s == Side.TOP || s == Side.BOTTOM || s == Side.CENTER_Y)
                return (int) ((i / 100f) * DisplayManager.HEIGHT);
            if (s == Side.LEFT || s == Side.RIGHT || s == Side.CENTER_X)
                return (int) ((i / 100f) * DisplayManager.WIDTH);
        }
        return 0;
    }
}

