package game.Graphics;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL43.*;

public class VertexBuffer {


    /*
    * Vertex layout:
    * position: 3 floats
    * normal: 3 floats
    * uv: 2 floats
    * */
    private FloatBuffer vertex_data;
    private IntBuffer index_data;
    public static int VERTEX_SIZE = 8;


    public VertexBuffer(FloatBuffer buffer) {
        vertex_data = buffer;
    }

    public VertexBuffer(float[] vertices, int[] indices) {
        vertex_data = BufferUtils.createFloatBuffer(vertices.length);
        vertex_data.put(vertices);
        vertex_data.flip();

        index_data = BufferUtils.createIntBuffer(indices.length);
        index_data.put(indices);
        index_data.flip();
    }

    public FloatBuffer getBuffer() {
        return vertex_data;
    }

    public IntBuffer getIndices() {
        return index_data;
    }

    public int indicesLength() {
        return index_data.remaining();
    }

    public static void setVertexAttributes() {
        glVertexAttribPointer(0, 3, GL_FLOAT, false, Float.BYTES * VERTEX_SIZE, 0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, Float.BYTES * VERTEX_SIZE, 3 * Float.BYTES);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, Float.BYTES * VERTEX_SIZE, 6 * Float.BYTES);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
    }
}
