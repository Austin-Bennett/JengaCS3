package game;

import game.Graphics.Shader;
import game.Graphics.ShaderProgram;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL43.*;

public class JengaGame {
    public final long window;
    public static final int NUMKEYS = 350;



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

    public void run() {




        //main loop
        //set clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        while ( !glfwWindowShouldClose(window) ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer


            if (isDown(GLFW_KEY_0)) {
                System.out.println("KEY 0 down");
            }


            //display the new frame and poll events
            glfwSwapBuffers(window);

            //update our input (store keyset in previous)
            updateInput();

            //tell GLFW to update the window input
            glfwPollEvents();
        }
    }

    private void updateInput() {
        System.arraycopy(keyset, 0, previous, 0, NUMKEYS);
    }
}
