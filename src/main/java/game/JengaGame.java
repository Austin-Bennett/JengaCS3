package game;

import game.Graphics.*;
import game.utils.StopWatch;
import org.joml.Matrix4f;

import java.io.IOException;

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

        Matrix4f norm = new Matrix4f();

        Model m = new Model(
            new VertexBuffer(
                new float[]{
                        //position         normal         uv
                         0.5f,  1f, 0.5f,    0f, 0f, 0f,   0f, 0f,    // top right
                         0.5f, 1f, -0.5f,    0f, 0f, 0f,   0f, 0f,    // bottom right
                        -0.5f, 1f, -0.5f,    0f, 0f, 0f,   0f, 0f,    // bottom left
                        -0.5f,  1f, 0.5f,    0f, 0f, 0f,   0f, 0f,    // top left
                },
                new int[]{
                    0, 1, 3,   // first triangle
                    1, 2, 3    // second triangle
                }
            )
        );

        FreeCam camera = new FreeCam();

        ShaderProgram program = new ShaderProgram(
                Shader.fromFile(GL_VERTEX_SHADER, "src/main/resources/main.vert"),
                Shader.fromFile(GL_FRAGMENT_SHADER, "src/main/resources/main.frag")
        );
//        uniform mat4 mat_model;
        //uniform mat4 mat_normal;
        //uniform mat4 mat_view;
        //uniform mat4 mat_projection;

        int model_l = program.getUniformLoc("mat_model");
        int normal_l = program.getUniformLoc("mat_normal");
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


            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            program.use();
            program.setUniformMatrix(model_l, m.getMatrix());
            program.setUniformMatrix(normal_l, norm.identity().mul(m.getMatrix()).invert().transpose());
            program.setUniformMatrix(view_l, camera.getView());
            program.setUniformMatrix(projection_l, camera.getProjection());

            m.draw();
            camera.update(dt);

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
