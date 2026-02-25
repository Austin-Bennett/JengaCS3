package game.Graphics;

import game.utils.Destructible;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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

    public static Shader fromFile(int type, String path) throws IOException {
        String filestring = Files.readString(Path.of(path));

        return new Shader(type, filestring);
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
