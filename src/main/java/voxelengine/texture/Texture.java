package voxelengine.texture;

import org.lwjgl.opengl.GL13;

public class Texture {
    private final int textureID;
    private final String path;

    public Texture(int textureID, String path) {
        this.textureID = textureID;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public int getTextureID() {
        return textureID;
    }

    public void delete() {
        GL13.glDeleteTextures(textureID);
    }
}
