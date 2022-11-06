package voxelengine.shader;


import org.joml.Vector2i;
import org.joml.Vector4f;

public class UIShader extends ShaderProgram {

    private static final String vertexPath = "res/shader/UI/vertex.shader";
    private static final String fragmentPath = "res/shader/UI/fragment.shader";

    public UIShader() {
        super(vertexPath, fragmentPath);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

    public void loadViewportSize(Vector2i viewportSize) {
        super.loadUniform(super.getUniformLocation("viewport"), viewportSize);
    }

    public void loadUIColor(Vector4f color) {
        super.loadUniform(super.getUniformLocation("uiColor"), color);
    }
}
