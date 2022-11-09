package voxelengine;

import org.joml.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import voxelengine.engine.DisplayManager;
import voxelengine.gui.UIManager;
import voxelengine.model.*;
import voxelengine.shader.*;
import voxelengine.util.MatrixBuilder;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.opengl.GL14.glBlendFuncSeparate;

public class Renderer {
    private static StaticShader defaultShader;
    private static SkyboxShader skyboxShader;
    private static DebugShader debugShader;
    private static BlockShader blockShader;
    private static UIShader uiShader;
    private static FontShader fontShader;
    private static ButtonShader buttonShader;
    private static ImageShader imageShader;

    private static SkyboxModel skyboxModel;
    private static SkyboxModel panorama;
    private static SkyboxModel day;

    public static void UpdateProjection() {
        Matrix4f mat = MatrixBuilder.createProjectionMatrix();
        imageShader.start();
        imageShader.loadViewportSize(new Vector2i(DisplayManager.WIDTH, DisplayManager.HEIGHT));
        imageShader.stop();
        buttonShader.start();
        buttonShader.loadViewportSize(new Vector2i(DisplayManager.WIDTH, DisplayManager.HEIGHT));
        buttonShader.stop();
        fontShader.start();
        fontShader.loadRotationMatrix(new Matrix4f().identity());
        fontShader.loadViewportSize(new Vector2i(DisplayManager.WIDTH, DisplayManager.HEIGHT));
        fontShader.loadRotationMatrix(new Matrix4f().identity());
        fontShader.stop();
        uiShader.start();
        uiShader.loadViewportSize(new Vector2i(DisplayManager.WIDTH, DisplayManager.HEIGHT));
        uiShader.stop();
        blockShader.start();
        blockShader.loadProjectionMatrix(mat);
        blockShader.stop();
        defaultShader.start();
        defaultShader.loadProjectionMatrix(mat);
        defaultShader.stop();
        skyboxShader.start();
        skyboxShader.loadProjectionMatrix(mat);
        skyboxShader.stop();
        debugShader.start();
        debugShader.loadProjectionMatrix(mat);
        debugShader.stop();
    }

    public static void Prepare() {
        glEnable(GL_MULTISAMPLE);
        glClearColor(0.5f, 0.5f, 1.0f, 1.0f);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glEnable(GL_ALPHA_TEST);
        glEnable(GL_BLEND);
        glBlendFuncSeparate(GL_ONE, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ONE);

        defaultShader = new StaticShader();
        skyboxShader = new SkyboxShader();
        debugShader = new DebugShader();
        blockShader = new BlockShader();
        uiShader = new UIShader();
        fontShader = new FontShader();
        buttonShader = new ButtonShader();
        imageShader = new ImageShader();
        UpdateProjection();

        panorama = ModelCreator.createSkyboxModel(new String[]{"panorama_3", "panorama_1", "panorama_4", "panorama_5", "panorama_2", "panorama_0"});
        day = ModelCreator.createSkyboxModel(new String[]{"right", "left", "top", "bottom", "back", "front"});
        skyboxModel = panorama;
    }

    public static void Render(Model model) {
        if (SkyboxModel.class.isAssignableFrom(model.getClass()))
            RenderSkybox((SkyboxModel) model);
        else if (TexturedModel.class.isAssignableFrom(model.getClass()))
            RenderBlockModel((TexturedModel) model, new Vector3f(0, 0, 0), new Quaternionf(), new Vector3f(1, 1, 1));
    }

    public static void RenderWireframeBox(Vector3f position, Quaternionf rotation, Vector3f size) {
        glDisable(GL_CULL_FACE);
        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        debugShader.start();
        debugShader.loadTransformationMatrix(MatrixBuilder.createTransformationMatrix(position, rotation, size));
        debugShader.loadViewMatrix(MatrixBuilder.createViewMatrix());
        Model model = ModelManager.getLoadedModels().get(0L);
        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(2);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        debugShader.stop();
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        glEnable(GL_CULL_FACE);
    }

    public static void RenderWireframeBox(Vector3i position, Quaternionf rotation, Vector3f size) {
        glDisable(GL_CULL_FACE);
        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        debugShader.start();
        debugShader.loadTransformationMatrix(MatrixBuilder.createTransformationMatrix(new Vector3f(position.x, position.y, position.z), rotation, size));
        debugShader.loadViewMatrix(MatrixBuilder.createViewMatrix());
        Model model = ModelManager.getLoadedModels().get(0L);
        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(2);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        debugShader.stop();
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        glEnable(GL_CULL_FACE);
    }


    private static void RenderTexturedModel(TexturedModel model, Vector3f position, Quaternionf rotation, Vector3f scale) {
        if (model == null)
            return;
        defaultShader.start();
        defaultShader.loadLight();
        defaultShader.setMaterial(20, 0.5f);
        defaultShader.loadTransformationMatrix(MatrixBuilder.createTransformationMatrix(position, rotation, scale));
        defaultShader.loadViewMatrix(MatrixBuilder.createViewMatrix());
        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getTextureID());
        GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(2);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        defaultShader.stop();
    }

    private static void RenderBlockModel(TexturedModel model, Vector3f position, Quaternionf rotation, Vector3f scale) {
        if (model == null)
            return;
        blockShader.start();
        blockShader.loadTransformationMatrix(MatrixBuilder.createTransformationMatrix(position, rotation, scale));
        blockShader.loadViewMatrix(MatrixBuilder.createViewMatrix());
        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getTextureID());
        GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(2);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        blockShader.stop();
    }

    public static UIShader getUiShader() {
        return uiShader;
    }

    public static FontShader getFontShader() {
        return fontShader;
    }

    public static ButtonShader getButtonShader() {
        return buttonShader;
    }

    public static ImageShader getImageShader() {
        return imageShader;
    }

    private static void RenderSkybox(SkyboxModel model) {
        skyboxShader.start();
        skyboxShader.loadViewMatrix(MatrixBuilder.createStationaryViewMatrix());
        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, model.getCubemapTexture());
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertexCount());
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        skyboxShader.stop();
        skyboxShader.stop();

    }

    public static void beginFrame() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
    }

    public static void endFrame() {
        Render(skyboxModel);
        UIManager.render();
        glfwPollEvents();
    }

    public static void cleanUp() {
        defaultShader.cleanUp();
        skyboxShader.cleanUp();
    }

    public static void setSkybox(int i) {
        skyboxModel = i==0 ? panorama : day;
    }
}
