package voxelengine;

import org.joml.Vector3f;
import voxelengine.util.Color;

public class DayCycle {
    private static Vector3f sunPosition = new Vector3f(100, 150, -100);
    private static Color sunColor = new Color(1.3f, 1.3f, 1.3f);


    public static void moveBy(Vector3f vec) {
        sunPosition.add(vec);
    }

    public static Vector3f getPosition() {
        return sunPosition;
    }

    public static void setPosition(Vector3f position) {
        sunPosition = position;
    }

    public static Color getColor() {
        return sunColor;
    }

    public static void setColor(Color color) {
        sunColor = color;
    }
}
