package game;

import game.Graphics.Shader;
import game.Graphics.ShaderProgram;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL43.*;

public class JengaGame {
    public long window;

    public static final String vertexShader =
            "#version 330 core\n" +
            "layout (location = 0) in vec3 aPos;\n" +
            "void main() {\n" +
            "    gl_Position = vec4(aPos.x, aPos.y, aPos.z, 1.0);\n" +
            "}";

    public static final String fragShader =
            "#version 330 core\n" +
            "out vec4 FragColor;\n" +
            "void main() {\n" +
            "    FragColor = vec4(1.0f, 0.5f, 0.2f, 1.0f);\n" +
            "}";



    public JengaGame() {
    }

    public void run() {

        float[] vertices = {
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.0f,  0.5f, 0.0f
        };

        int VAO = glGenVertexArrays();
        int VBO = glGenBuffers();

        //bind the vao
        glBindVertexArray(VAO);

        //create vbo
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        //set vertex attributes
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);


        //create shader
        ShaderProgram program;
        {
            Shader vert = new Shader(GL_VERTEX_SHADER, vertexShader);
            Shader frag = new Shader(GL_FRAGMENT_SHADER, fragShader);

            program = new ShaderProgram(vert, frag);
        }




        //set clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        while ( !glfwWindowShouldClose(window) ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer


            //draw
            program.use();
            glBindVertexArray(VAO);
            //draw triangle
            glDrawArrays(GL_TRIANGLES, 0, 3);


            //window loop end
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }
}
