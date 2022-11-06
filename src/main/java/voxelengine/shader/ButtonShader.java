package voxelengine.shader;


import org.joml.Vector2i;

public class ButtonShader extends ShaderProgram {

    private static final String vertexPath = "res/shader/button/vertex.shader";
    private static final String fragmentPath = "res/shader/button/fragment.shader";

    public ButtonShader() {
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

    public void loadHovered(boolean isHovered) {
        super.loadUniform(super.getUniformLocation("hovered"), isHovered ? 1 : 0);
    }

    public void loadDisabled(boolean isDisabled) {
        super.loadUniform(super.getUniformLocation("disabled"), isDisabled ? 1 : 0);
    }
}
