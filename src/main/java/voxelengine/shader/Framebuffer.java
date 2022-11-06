package voxelengine.shader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import voxelengine.engine.DisplayManager;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;


public class Framebuffer {
    public static final int NONE = 0;
    public static final int DEPTH_TEXTURE = 1;
    public static final int DEPTH_RENDER_BUFFER = 2;
    private static final List<Framebuffer> instances = new ArrayList<>();
    private final int depthBufferType;
    private final boolean colorAttachment;
    private int width;
    private int height;
    private int frameBuffer;
    private int colorTexture;
    private int depthTexture;
    private int depthBuffer;
    private int colorBuffer;

    /**
     * Creates an FBO of a specified width and height, with the desired type of
     * depth buffer attachment.
     *
     * @param width           - the width of the FBO.
     * @param height          - the height of the FBO.
     * @param depthBufferType - an int indicating the type of depth buffer attachment that
     *                        this FBO should use.
     * @param colorAttachment - a bool indicating whether to render to a color buffer
     */
    public Framebuffer(int width, int height, int depthBufferType, boolean colorAttachment) {
        this.width = width;
        this.height = height;
        this.depthBufferType = depthBufferType;
        this.colorAttachment = colorAttachment;
        instances.add(this);
        initialiseFrameBuffer(depthBufferType);
    }

    /**
     * Deletes the frame buffer and its attachments, then creates
     * a new one with a different resolution.
     */
    public static void setFrameBufferSize(int width, int height) {
        for (Framebuffer fb : instances) {
            fb.cleanUp();
            fb.width = width;
            fb.height = height;
            fb.initialiseFrameBuffer(fb.depthBufferType);
        }
    }

    /**
     * Deletes the frame buffer and its attachments when the game closes.
     */
    public void cleanUp() {
        GL30.glDeleteFramebuffers(frameBuffer);
        GL11.glDeleteTextures(colorTexture);
        GL11.glDeleteTextures(depthTexture);
        GL30.glDeleteRenderbuffers(depthBuffer);
        GL30.glDeleteRenderbuffers(colorBuffer);
    }

    /**
     * Unbinds the frame buffer, setting the default frame buffer as the current
     * render target. Anything rendered after this will be rendered to the
     * screen, and not this FBO
     */
    private void unbindFrameBuffer() {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        GL11.glViewport(0, 0, DisplayManager.WIDTH, DisplayManager.HEIGHT);
    }

    /**
     * @return The ID of the texture containing the color buffer of the FBO.
     */
    public int getColorTexture() {
        return colorTexture;
    }

    /**
     * @return The texture containing the FBOs depth buffer.
     */
    public int getDepthTexture() {
        return depthTexture;
    }

    /**
     * Creates the FBO along with a color buffer texture attachment, and
     * possibly a depth buffer.
     *
     * @param type - the type of depth buffer attachment to be attached to the
     *             FBO.
     */
    private void initialiseFrameBuffer(int type) {
        createFrameBuffer();
        if (colorAttachment)
            createTextureAttachment();
        if (type == DEPTH_RENDER_BUFFER) {
            createDepthBufferAttachment();
        } else if (type == DEPTH_TEXTURE) {
            createDepthTextureAttachment();
        }
        unbindFrameBuffer();
    }

    /**
     * Creates a new frame buffer object and sets the buffer to which drawing
     * will occur - color attachment 0. This is the attachment where the color
     * buffer texture is.
     */
    private void createFrameBuffer() {
        frameBuffer = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
        if (colorAttachment)
            GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
        else
            GL11.glDrawBuffer(GL30.GL_NONE);
    }

    /**
     * Creates a texture and sets it as the color buffer attachment for this
     * FBO.
     */
    private void createTextureAttachment() {
        colorTexture = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTexture);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
                (ByteBuffer) null);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, colorTexture,
                0);
    }

    /**
     * Adds a depth buffer to the FBO in the form of a texture, which can later
     * be sampled.
     */
    private void createDepthTextureAttachment() {
        depthTexture = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, depthTexture);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT24, width, height, 0, GL11.GL_DEPTH_COMPONENT,
                GL11.GL_FLOAT, (ByteBuffer) null);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, depthTexture, 0);
    }

    /**
     * Adds a depth buffer to the FBO in the form of a render buffer. This can't
     * be used for sampling in the shaders.
     */
    private void createDepthBufferAttachment() {
        depthBuffer = GL30.glGenRenderbuffers();
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBuffer);
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL14.GL_DEPTH_COMPONENT24, width, height);
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER,
                depthBuffer);
    }

    public void bind() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
        glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
        glBindRenderbuffer(GL_RENDERBUFFER, depthBuffer);
    }

    public void unbind() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glBindRenderbuffer(GL_RENDERBUFFER, 0);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
