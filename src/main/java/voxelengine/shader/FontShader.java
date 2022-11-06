package voxelengine.shader;


import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector4f;

public class FontShader extends ShaderProgram {

    private static final String vertexPath = "res/shader/font/vertex.shader";
    private static final String fragmentPath = "res/shader/font/fragment.shader";

    public FontShader() {
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

    public void loadUIColor(Vector4f color) {
        super.loadUniform(super.getUniformLocation("uiColor"), color);
    }

    public void loadRotationMatrix(Matrix4f rotation) {
        super.loadUniform(super.getUniformLocation("rotationMat"), rotation);
    }
}
