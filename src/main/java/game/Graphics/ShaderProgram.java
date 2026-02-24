package game.Graphics;

import game.utils.Destructible;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL43.*;

public class ShaderProgram extends Destructible implements GLObject {

    private final int id;

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

    public void use() {
        glUseProgram(id);
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public void destruct() {

    }
}
