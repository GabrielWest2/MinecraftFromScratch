package voxelengine.model;


import voxelengine.texture.Texture;

public class TexturedModel extends Model {
    private Texture texture;

    public TexturedModel(int vaoID, int vertexCount, Texture texture) {
        super(vaoID, vertexCount);
        this.texture = texture;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
