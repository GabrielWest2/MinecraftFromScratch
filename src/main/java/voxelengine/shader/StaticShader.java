package voxelengine.shader;


import org.joml.Matrix4f;
import voxelengine.DayCycle;

public class StaticShader extends ShaderProgram {

    private static final String vertexPath = "res/shader/standard/vertex.shader";
    private static final String fragmentPath = "res/shader/standard/fragment.shader";

    public StaticShader() {
        super(vertexPath, fragmentPath);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
        super.bindAttribute(2, "normal");
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

    public void loadLight() {
        super.loadUniform(super.getUniformLocation("lightPosition"), DayCycle.getPosition());
        super.loadUniform(super.getUniformLocation("lightColor"), DayCycle.getColor().toVector());
    }

    public void setMaterial(float shineDamper, float reflectivity) {
        super.loadUniform(super.getUniformLocation("shineDamper"), shineDamper);
        super.loadUniform(super.getUniformLocation("reflectivity"), reflectivity);
    }
}
