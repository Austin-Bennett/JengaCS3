package game.graphics;

import game.utils.ArrayUtils;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

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
    public static int VERTEX_SIZE = 5;


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


    public static VertexBuffer makeCube(float radius) {
        ArrayUtils.ArrayWrapper<float[]> vertex_data = ArrayUtils.wrap(new float[0]);

        //add each face
        ArrayUtils.appendAll(vertex_data, new float[]{ -radius, radius, radius, 0, 0 });  //front top left       0
        ArrayUtils.appendAll(vertex_data, new float[]{ radius, radius, radius, 1, 0 });   //front top right      1
        ArrayUtils.appendAll(vertex_data, new float[]{ -radius, radius, -radius, 0, 1 }); //front bottom left    2
        ArrayUtils.appendAll(vertex_data, new float[]{ radius, radius, -radius, 1, 1 });  //front bottom right   3

        ArrayUtils.appendAll(vertex_data, new float[]{ -radius, -radius, radius, 0, 0 }); //back top left        4
        ArrayUtils.appendAll(vertex_data, new float[]{ radius, -radius, radius, 1, 0 });  //back top right       5
        ArrayUtils.appendAll(vertex_data, new float[]{ -radius, -radius, -radius, 0, 1 });//back bottom left     6
        ArrayUtils.appendAll(vertex_data, new float[]{ radius, -radius, -radius, 1, 1 }); //back bottom right    7


        int[] indices = {
                0, 1, 2, //face 1
                1, 2, 3,

                4, 5, 6, //face 2
                5, 6, 7,

                0, 4, 2, //face 3
                4, 2, 6,

                4, 5, 0,
                5, 0, 1, //face 4

                5, 1, 3,
                5, 3, 7,

                7, 6, 3,
                3, 6, 2,
        };

        return new VertexBuffer(ArrayUtils.unwrap(vertex_data), indices);
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
        glVertexAttribPointer(1, 2, GL_FLOAT, false, Float.BYTES * VERTEX_SIZE, 3 * Float.BYTES);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
    }
}
