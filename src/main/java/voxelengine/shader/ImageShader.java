package voxelengine.shader;


import org.joml.Vector2i;

public class ImageShader extends ShaderProgram {

    private static final String vertexPath = "res/shader/image/vertex.shader";
    private static final String fragmentPath = "res/shader/image/fragment.shader";

    public ImageShader() {
        super(vertexPath, fragmentPath);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
    }

    public void loadViewportSize(Vector2i viewportSize) {
        super.loadUniform(super.getUniformLocation("viewport"), viewportSize);
    }
}
