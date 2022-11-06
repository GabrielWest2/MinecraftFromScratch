package voxelengine.shader;

import org.joml.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.HashMap;

public abstract class ShaderProgram {
    private static final FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
    private final int programID;
    private final int vertexShaderID;
    private final int fragmentShaderID;
    private final HashMap<String, Integer> uniformCache = new HashMap<>();

    public ShaderProgram(String vertexFile, String fragmentFile) {
        vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
        fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
        programID = GL20.glCreateProgram();
        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);
        bindAttributes();
        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);
    }

    private static int loadShader(String file, int type) {
        StringBuilder shaderSource = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append("//\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);
        if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
            System.err.println("Could not compile " + file);
            System.exit(-1);
        }
        return shaderID;
    }

    public int getProgramID() {
        return programID;
    }

    protected void loadUniform(int location, float value) {
        GL20.glUniform1f(location, value);
    }

    protected void loadUniform(int location, int value) {
        GL20.glUniform1i(location, value);
    }

    protected void loadUniform(int location, Vector2f value) {
        GL20.glUniform2f(location, value.x, value.y);
    }

    protected void loadUniform(int location, Vector2i value) {
        GL20.glUniform2f(location, (float) value.x, (float) value.y);
    }

    protected void loadUniform(int location, Vector3f value) {
        GL20.glUniform3f(location, value.x, value.y, value.z);
    }

    protected void loadUniform(int location, Vector4f value) {
        GL20.glUniform4f(location, value.x, value.y, value.z, value.w);
    }

    protected void loadUniform(int location, Matrix4f value) {
        value.get(matrixBuffer);
        GL20.glUniformMatrix4fv(location, false, matrixBuffer);
    }

    public int getUniformLocation(String uniform) {
        if (uniformCache.containsKey(uniform)) {
            return uniformCache.get(uniform);
        } else {
            int location = GL20.glGetUniformLocation(programID, uniform);
            uniformCache.put(uniform, location);
            return location;
        }
    }

    public void start() {
        GL20.glUseProgram(programID);
    }

    public void loadProjectionMatrix(Matrix4f mat) {

    }

    public void stop() {
        GL20.glUseProgram(0);
    }

    public void cleanUp() {
        stop();
        GL20.glDetachShader(programID, vertexShaderID);
        GL20.glDetachShader(programID, fragmentShaderID);
        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(fragmentShaderID);
        GL20.glDeleteProgram(programID);
    }

    protected abstract void bindAttributes();

    protected void bindAttribute(int attribute, String variableName) {
        GL20.glBindAttribLocation(programID, attribute, variableName);
    }

}