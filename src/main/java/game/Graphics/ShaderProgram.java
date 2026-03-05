package game.Graphics;

import game.utils.Destructible;
import org.joml.Matrix4f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL43.*;

public class ShaderProgram extends Destructible implements GLObject {

    private final int id;
    public static final float[] mat4v = new float[16];

    public ShaderProgram(Shader... shaders) {
        id = glCreateProgram();

        for (Shader shader: shaders) {
            glAttachShader(id, shader.getID());
        }

        glLinkProgram(id);

        if (glGetProgrami(id, GL_LINK_STATUS) == GL_FALSE) {
            throw new RuntimeException("Program linking failed: " + glGetProgramInfoLog(id));
        }
    }

    public int getUniformLoc(String name) {
        return glGetUniformLocation(id, name);
    }

    public void setUniformMatrix(int loc, Matrix4f mat) {
        glUniformMatrix4fv(loc, false, mat.get(mat4v));
    }

    public void use() {
        glUseProgram(id);
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public void destruct() {
        glDeleteProgram(id);
    }
}
