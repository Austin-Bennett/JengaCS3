package game.graphics;

import game.JengaGame;
import static org.lwjgl.glfw.GLFW.*;

public class FreeCam extends PerspectiveCamera {
    public float speed = 1f;
    public float sensitivity = 0.01f;

    private boolean mouse_enabled = true;

    public FreeCam() {
        JengaGame.enableFPSInput();
    }


    public void update(float dt, boolean update_mouse) {

        if (JengaGame.isPressed(GLFW_KEY_ESCAPE)) {
            this.mouse_enabled = !mouse_enabled;
            if (mouse_enabled) {
                JengaGame.enableFPSInput();
            } else {
                JengaGame.disableFPSInput();
            }
        }

        if (JengaGame.isDown(GLFW_KEY_W)) {
            this.moveForward(speed * dt);
        }
        if (JengaGame.isDown(GLFW_KEY_S)) {
            this.moveForward(-speed * dt);
        }

        if (JengaGame.isDown(GLFW_KEY_A)) {
            this.moveRight(-speed * dt);
        }
        if (JengaGame.isDown(GLFW_KEY_D)) {
            this.moveRight(speed * dt);
        }

        if (JengaGame.isDown(GLFW_KEY_LEFT_CONTROL)) {
            this.position.z += -speed * dt;
            dirty = true;
        }
        if (JengaGame.isDown(GLFW_KEY_SPACE)) {
            this.position.z += speed * dt;
            dirty = true;
        }

        if (mouse_enabled && update_mouse) {
            var dx = (float) JengaGame.getMouseDX();
            var dy = (float) JengaGame.getMouseDY();

            this.rotatePitch(-dy * sensitivity);
            this.rotateYaw(-dx * sensitivity);
        }
    }
}
