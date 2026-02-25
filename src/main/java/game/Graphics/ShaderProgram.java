package game.Graphics;

import game.utils.Destructible;
import game.utils.FastHashMap;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL43.*;

public class ShaderProgram extends Destructible implements GLObject {

    private final int id;
    private FastHashMap<String, Integer> uniform_locs = new FastHashMap<>();

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


    public int getUniformLoc(String name) {
        var l = uniform_locs.get(name);
        if (l == null) {
            int loc = glGetUniformLocation(id, name);

            if (loc == -1) throw new RuntimeException("No uniform named: " + name);

            uniform_locs.insert(name, loc);

            System.out.println(loc);
            return loc;
        }

        return l;
    }

    //value
    public void setUniform(int loc, float value) {
        use();

        glUniform1f(loc, value);
    }

    public void setUniform(String name, float value) {
        setUniform(getUniformLoc(name), value);
    }

    //FVEC2
    public void setUniform(int loc, float x, float y) {
        use();
        glUniform2f(loc, x, y);
    }

    public void setUniform(String name, float x, float y) {
        setUniform(getUniformLoc(name), x, y);
    }

    public void setUniform(int loc, Vector2f vec2) {
        setUniform(loc, vec2.x, vec2.y);
    }

    public void setUniform(String name, Vector2f vec2) {
        setUniform(getUniformLoc(name), vec2);
    }

    //FVEC3
    public void setUniform(int loc, float x, float y, float z) {
        use();
        glUniform3f(loc, x, y, z);
    }

    public void setUniform(String name, float x, float y, float z) {
        setUniform(getUniformLoc(name), x, y, z);
    }

    public void setUniform(int loc, Vector3f vec3) {
        setUniform(loc, vec3.x, vec3.y, vec3.z);
    }

    public void setUniform(String name, Vector3f vec3) {
        setUniform(getUniformLoc(name), vec3);
    }

    //FVEC4
    public void setUniform(int loc, float x, float y, float z, float w) {
        use();
        glUniform4f(loc, x, y, z, w);
    }

    public void setUniform(String name, float x, float y, float z, float w) {
        setUniform(getUniformLoc(name), x, y, z, w);
    }

    public void setUniform(int loc, Vector4f vec4) {
        setUniform(loc, vec4.x, vec4.y, vec4.z, vec4.w);
    }

    public void setUniform(String name, Vector4f vec4) {
        setUniform(getUniformLoc(name), vec4);
    }


    //todo: other uniforms, we don't need any others right now tho

    @Override
    public int getID() {
        return id;
    }

    @Override
    public void destruct() {
        glUseProgram(0);
        glDeleteProgram(this.id);
    }
}
