package game;

import game.graphics.*;
import game.utils.StopWatch;
import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.nanovg.NanoVGGL3;
import org.lwjgl.opengl.GL43;
import org.ode4j.math.DMatrix3;
import org.ode4j.math.DMatrix3C;
import physics.PhysicsObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL43.*;

public class JengaGame {
    public static long window;
    public static final int NUMKEYS = 350;
    public static int WINDOW_WIDTH = 800;
    public static int WINDOW_HEIGHT = 600;


    //input key state
    private static final boolean[] keyset = new boolean[NUMKEYS];
    private static final boolean[] previous = new boolean[NUMKEYS];

    public static ArrayList<Camera> cameras = new ArrayList<>();

    public JengaBoard board = null;

    // --- Mouse state ---
    public static final int NUMBUTTONS = 8; // GLFW supports up to 8 mouse buttons

    private static final boolean[] mouseButtons     = new boolean[NUMBUTTONS];
    private static final boolean[] mousePrevious    = new boolean[NUMBUTTONS];

    private static double mouseX = 0, mouseY = 0;       // current position
    private static double mouseDX = 0, mouseDY = 0;      // delta since last frame
    private static double prevMouseX = 0, prevMouseY = 0;
    private static double scrollDX = 0, scrollDY = 0;   // scroll delta this frame
    private static double scrollAccumX = 0, scrollAccumY = 0; // accumulates between updateInput() calls
    private static boolean mouseFirstMove = true;



    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (button < 0 || button >= NUMBUTTONS) return;
        mouseButtons[button] = (action == GLFW_PRESS);
    }

    public static void cursorPosCallback(long window, double xpos, double ypos) {
        if (mouseFirstMove) {
            prevMouseX = xpos;
            prevMouseY = ypos;
            mouseFirstMove = false;
        }
        mouseX = xpos;
        mouseY = ypos;
    }

    public static void scrollCallback(long window, double xoffset, double yoffset) {
        scrollAccumX += xoffset;
        scrollAccumY += yoffset;
    }



    public static boolean isMousePressed(int button) {
        if (button < 0 || button >= NUMBUTTONS) return false;
        return mouseButtons[button] && !mousePrevious[button];
    }

    public static boolean isMouseReleased(int button) {
        if (button < 0 || button >= NUMBUTTONS) return false;
        return !mouseButtons[button] && mousePrevious[button];
    }

    public static boolean isMouseDown(int button) {
        if (button < 0 || button >= NUMBUTTONS) return false;
        return mouseButtons[button];
    }

    public static boolean isMouseUp(int button) {
        if (button < 0 || button >= NUMBUTTONS) return false;
        return !mouseButtons[button];
    }

    /** Raw cursor position in screen pixels */
    public static double getMouseX() { return mouseX; }
    public static double getMouseY() { return mouseY; }

    /** Movement delta since the last frame */
    public static double getMouseDX() { return mouseDX; }
    public static double getMouseDY() { return mouseDY; }

    /** Scroll delta since the last frame (Y is the typical scroll wheel axis) */
    public static double getScrollDX() { return scrollDX; }
    public static double getScrollDY() { return scrollDY; }



    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (key < 0 || key > keyset.length) return;
        //ignore GLFW_REPEAT
        if (action == GLFW_REPEAT) return;
        keyset[key] = action == GLFW_PRESS;
    }

    public static void windowSizeCallback(long window, int w, int h) {
        glViewport(0, 0, w, h);

        WINDOW_WIDTH = w;
        WINDOW_HEIGHT = h;

        for (var c: cameras) {
            c.onResize(w, h);
        }
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

        long vg = NanoVGGL3.nnvgCreate(NanoVGGL3.NVG_ANTIALIAS);
        int font = NanoVG.nvgCreateFont(vg, "main", "src/main/resources/fonts/unitblock.ttf");

        this.board = new JengaBoard();

        Scanner in = new Scanner(System.in);

        FreeCam camera = new FreeCam();
        cameras.add(camera);
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
        int normal_l = program.getUniformLoc("mat_normal");
        int view_l = program.getUniformLoc("mat_view");
        int projection_l = program.getUniformLoc("mat_projection");
        StopWatch sw = new StopWatch();
        sw.reset();

        var model = new Model(VertexBuffer.loadObj("src/main/resources/models/airplane.obj"));

        boolean do_game = true;
        while (do_game) {
            do_game = false;
            boolean start = false;


            while (!start && !glfwWindowShouldClose(window)) {


                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

                NanoVG.nvgBeginFrame(vg, WINDOW_WIDTH, WINDOW_HEIGHT, 1.0f);


                NanoVG.nvgFontFace(vg, "main");
                NanoVG.nvgFontSize(vg, 24.0f);
                NanoVG.nvgText(vg, 20, 20, "Controls:");

                int start_text = 45;
                String[] helpText = {
                        "P: Pause/Unpause (starts paused)",
                        "H: show this screen again",
                        "AWSD: Move",
                        "LCtrl: Go Down",
                        "Space: Go Up",
                        "Mouse: Look",
                        "Left Click: Grab block",
                        "Shift + Mouse: Rotate held block",
                        "R: Reset board",
                        "Esc: Show/Hide mouse, exit this screen",
                };

                for (String s : helpText) {

                    NanoVG.nvgText(vg, 20, start_text, s);

                    start_text += 25;
                }

                NanoVG.nvgEndFrame(vg);


                GL43.glEnable(GL43.GL_DEPTH_TEST);

                if (isPressed(GLFW_KEY_ESCAPE)) {
                    start = true;
                }

                //display the new frame and poll events
                glfwSwapBuffers(window);

                //update our input (store keyset in previous)
                updateInput();


                //tell GLFW to update the window input
                glfwPollEvents();
            }

            PhysicsObject heldObj = null;
            float dist = 0;

            //main loop
            //set clear color
            glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            while (!glfwWindowShouldClose(window)) {

                float dt = sw.secondsF();
                sw.reset();


                board.update(dt);

                if (isPressed(GLFW_KEY_R)) {
                    board = new JengaBoard();
                }

                if (isPressed(GLFW_KEY_H)) {
                    do_game = true;
                    break;
                }


                if (heldObj != null && isDown(GLFW_KEY_LEFT_SHIFT)) {
                    var right = camera.right();
                    var up = Camera.WORLD_UP;

                    var dx = getMouseDX();
                    var dy = getMouseDY();

                    heldObj.body.setAngularVel(
                            right.x * dy + up.x * dx,
                            right.y * dy + up.y * dx,
                            right.z * dy + up.z * dx
                    );

                    camera.update(dt, false);
                } else if (heldObj != null) {
                    heldObj.body.setAngularVel(0, 0, 0);

                    camera.update(dt, true);
                } else {
                    camera.update(dt, true);
                }

                if (heldObj == null && isPressed(GLFW_KEY_F)) {
                    var fwd = camera.forward();
                    var hit = board.raycast(camera.position(), fwd);

                    float f = 15;
                    if (hit != null && !hit.object.body.isKinematic()) {
                        hit.object.body.addLinearVel(fwd.x * f, fwd.y * f, fwd.z * f);
                    }
                }


                if (isMousePressed(GLFW_MOUSE_BUTTON_1)) {
                    var fwd = camera.forward();
                    var hit = board.raycast(camera.position(), fwd);

                    if (hit != null && !hit.object.body.isKinematic()) {
                        heldObj = hit.object;
                        dist = heldObj.collider.transform.translation().distance(camera.position());
                    }
                }

                if (heldObj != null) {
                    var pos = camera.forward().mul(dist).add(camera.position());
                    var coll = heldObj.collider;

                    float speed = 5;

                    heldObj.body.setLinearVel(-(coll.x() - pos.x) * speed, -(coll.y() - pos.y) * speed, -(coll.z() - pos.z) * speed);

                    var scroll = getScrollDY();

                    dist += scroll;
                }


                if (isMouseReleased(GLFW_MOUSE_BUTTON_1)) {
                    heldObj = null;
                    dist = 0;
                }

                if (isPressed(GLFW_KEY_G)) {
                    float f = 20;
                    //make a new block at the camera position
                    var fwd = camera.forward();

                    var block = new Block(board, 3, 3, 3, 10);
                    //block.collider.setVertices(model);
                    block.setPosition(camera.position().x, camera.position().y, camera.position().z);
                    block.body.addLinearVel(fwd.x * f, fwd.y * f, fwd.z * f);

                    board.addObject(block);
                }


                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

                program.use();

                program.setUniformMatrix4(view_l, camera.getView());
                program.setUniformMatrix4(projection_l, camera.getProjection());
                board.draw(program, model_l, normal_l);

                NanoVG.nvgBeginFrame(vg, WINDOW_WIDTH, WINDOW_HEIGHT, 1.0f);


                NanoVG.nvgFontFace(vg, "main");
                NanoVG.nvgFontSize(vg, 24.0f);
                NanoVG.nvgText(vg, WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2, "+");

                if (!board.do_physics) {
                    NanoVG.nvgText(vg, 20, 20, "GAME PAUSED ||");
                }

                NanoVG.nvgEndFrame(vg);


                GL43.glEnable(GL43.GL_DEPTH_TEST);


                //display the new frame and poll events
                glfwSwapBuffers(window);

                //update our input (store keyset in previous)
                updateInput();

                //tell GLFW to update the window input
                glfwPollEvents();
            }
        }
    }

    private void updateInput() {
        System.arraycopy(keyset, 0, previous, 0, NUMKEYS);

        // Mouse buttons
        System.arraycopy(mouseButtons, 0, mousePrevious, 0, NUMBUTTONS);

        // Mouse movement delta
        mouseDX = mouseX - prevMouseX;
        mouseDY = mouseY - prevMouseY;
        prevMouseX = mouseX;
        prevMouseY = mouseY;

        // Scroll delta (consume the accumulator)
        scrollDX = scrollAccumX;
        scrollDY = scrollAccumY;
        scrollAccumX = 0;
        scrollAccumY = 0;
    }

    public static void enableFPSInput() {
        // Lock and hide the cursor for FPS-style look
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        // If the driver supports it, use raw (unaccelerated) mouse deltas
        if (glfwRawMouseMotionSupported())
            glfwSetInputMode(window, GLFW_RAW_MOUSE_MOTION, GLFW_TRUE);
    }

    public static void disableFPSInput() {
        // Lock and hide the cursor for FPS-style look
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);

        // If the driver supports it, use raw (unaccelerated) mouse deltas
        if (glfwRawMouseMotionSupported())
            glfwSetInputMode(window, GLFW_RAW_MOUSE_MOTION, GLFW_FALSE);
    }
}
