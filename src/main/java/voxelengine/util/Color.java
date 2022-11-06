package voxelengine.util;

import org.joml.Vector3f;

/**
 * @author gabed
 * @Date 7/23/2022
 */
public class Color {
    public float r, g, b;

    public Color(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public void set(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public Vector3f toVector() {
        return new Vector3f(this.r, this.g, this.b);
    }
}
