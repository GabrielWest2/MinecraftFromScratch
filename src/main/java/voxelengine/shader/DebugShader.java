package voxelengine.shader;


import org.joml.Matrix4f;

public class DebugShader extends ShaderProgram {

    private static final String vertexPath = "res/shader/debug/vertex.shader";
    private static final String fragmentPath = "res/shader/debug/fragment.shader";

    public DebugShader() {
        super(vertexPath, fragmentPath);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadUniform(super.getUniformLocation("transformationMatrix"), matrix);

    }

    @Override
    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadUniform(super.getUniformLocation("projectionMatrix"), matrix);
    }

    public void loadViewMatrix(Matrix4f matrix) {
        super.loadUniform(super.getUniformLocation("viewMatrix"), matrix);
    }

}
