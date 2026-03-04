package game;

import game.Graphics.Shader;
import game.Graphics.ShaderProgram;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.IntBuffer;
import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL43.*;

public class JengaGame {
    public final long window;
    public static final int NUMKEYS = 350;


    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;


    //input key state
    private static final boolean[] keyset = new boolean[NUMKEYS];
    private static final boolean[] previous = new boolean[NUMKEYS];

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        keyset[key] = action == GLFW_PRESS;
    }

    public static boolean isPressed(int key) {
        if (key < 0 || key >= NUMKEYS) return false;
        return keyset[key] && !previous[key];
    }

    public static boolean isReleased(int key) {
        if (key < 0 || key >= NUMKEYS) return false;
        return !keyset[key] && previous[key];
    }

    public static boolean isDown(int key) {
        if (key < 0 || key >= NUMKEYS) return false;
        return keyset[key];
    }

    public static boolean isUp(int key) {
        if (key < 0 || key >= NUMKEYS) return false;
        return !keyset[key];
    }



    public JengaGame(long window) {
        this.window = window;
    }

    public void run() throws IOException {
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
            Shader vert = Shader.fromFile(GL_VERTEX_SHADER, "src/main/resources/main.vert");
            Shader frag = Shader.fromFile(GL_FRAGMENT_SHADER, "src/main/resources/main.frag");

            program = new ShaderProgram(vert, frag);
        }

        program.setUniform("ourColor", 1, 0, 1, 1);


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


    private void updateInput() {
        System.arraycopy(keyset, 0, previous, 0, NUMKEYS);
    }
}
