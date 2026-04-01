package game.graphics;

import game.utils.Destructible;
import game.utils.Transform;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL43.*;

public class Model extends Destructible {

    private VertexBuffer vertices;
    private int VBO;
    private int EBO;
    private int VAO;

    public Model(VertexBuffer buffer) {
        this.vertices = buffer;

        this.VAO = glGenVertexArrays();
        this.VBO = glGenBuffers();
        this.EBO = glGenBuffers();

        glBindVertexArray(VAO);

        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, buffer.getBuffer(), GL_STATIC_DRAW);

        VertexBuffer.setVertexAttributes();

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer.getIndices(), GL_STATIC_DRAW);
    }

    public void draw() {
        glBindVertexArray(VAO);
        glDrawElements(GL_TRIANGLES, vertices.indicesLength(), GL_UNSIGNED_INT, 0);
    }


    @Override
    public void destruct() {
        glDeleteBuffers(VBO);
        glDeleteBuffers(EBO);
        glDeleteVertexArrays(VAO);
    }

    public Model clone() {
        //only clones transform
        var res = new Model(vertices);

        return res;
    }
}
