package game.Graphics;

import game.utils.Destructible;

import java.io.Closeable;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL43.*;

public class Shader extends Destructible implements GLObject {

    private final int id;

    public Shader(int type, String source) {
        id = glCreateShader(type);

        glShaderSource(id, source);

        glCompileShader(id);

        if (glGetShaderi(id, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new RuntimeException("Shader compilation failed: " + glGetShaderInfoLog(id));
        }
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public void destruct() {
        glDeleteShader(id);
    }
}
