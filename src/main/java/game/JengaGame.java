package game;

import game.graphics.*;
import game.utils.StopWatch;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.io.IOException;
import java.util.Scanner;

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


    public JengaBoard board = null;

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

        this.board = new JengaBoard();

        Scanner in = new Scanner(System.in);

        FreeCam camera = new FreeCam();
        camera.moveForward(-5);
        camera.speed = 10;

        ShaderProgram program = new ShaderProgram(
                Shader.fromFile(GL_VERTEX_SHADER, "src/main/resources/main.vert"),
                Shader.fromFile(GL_FRAGMENT_SHADER, "src/main/resources/main.frag")
        );
//        uniform mat4 mat_model;
        //uniform mat4 mat_normal;
        //uniform mat4 mat_view;
        //uniform mat4 mat_projection;

        int model_l = program.getUniformLoc("mat_model");
        int view_l = program.getUniformLoc("mat_view");
        int projection_l = program.getUniformLoc("mat_projection");
        StopWatch sw = new StopWatch();
        sw.reset();

        //main loop
        //set clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        while ( !glfwWindowShouldClose(window) ) {

            float dt = sw.secondsF();
            sw.reset();

            camera.update(dt);

            if (isDown(GLFW_KEY_ENTER)) {
                dt *= 0.01f;
            }

            if (isPressed(GLFW_KEY_G)) {
                float x = in.nextFloat();
                float y = in.nextFloat();
                float z = in.nextFloat();

                camera.setPos(x, y, z);
            }

            board.update(dt);


            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            program.use();

            program.setUniformMatrix(view_l, camera.getView());
            program.setUniformMatrix(projection_l, camera.getProjection());
            board.draw(program, model_l);



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
