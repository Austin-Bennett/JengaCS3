import game.JengaGame;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL43;
import org.lwjgl.system.*;

import java.io.IOException;
import java.nio.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

//jimmy code

/*
* NOTE:
* if running this, you must go into build.gradle and set lwjglNatives to natives-windows
* */
public class Main {

    //private long window;
    private JengaGame game;

    public static void main(String[] args) throws IOException {
        new Main().run();
    }

    Main() {
    }

    private void run() throws IOException {
        init();

        game.run();
    }


    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation

        // Create the window
        game = new JengaGame(
                glfwCreateWindow(800, 600, "JENGA", NULL, NULL)
        );



        if ( game.window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(game.window, JengaGame::keyCallback);
        glfwSetWindowSizeCallback(game.window, JengaGame::windowSizeCallback);
        glfwSetMouseButtonCallback(game.window, JengaGame::mouseButtonCallback);
        glfwSetCursorPosCallback  (game.window, JengaGame::cursorPosCallback);
        glfwSetScrollCallback     (game.window, JengaGame::scrollCallback);

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(game.window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    game.window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(game.window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(game.window);


        GL.createCapabilities();

        GL43.glEnable(GL43.GL_DEPTH_TEST);
    }
}