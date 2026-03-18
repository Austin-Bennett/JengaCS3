package game.graphics;

import game.JengaGame;
import static org.lwjgl.glfw.GLFW.*;

public class FreeCam extends PerspectiveCamera {
    public float speed = 1f;
    public float sensitivity = 1f;


    public void update(float dt) {
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
            this.moveUp(-speed * dt);
        }
        if (JengaGame.isDown(GLFW_KEY_SPACE)) {
            this.moveUp(speed * dt);
        }

        if (JengaGame.isDown(GLFW_KEY_UP)) {
            this.rotatePitch(sensitivity * dt);
        }
        if (JengaGame.isDown(GLFW_KEY_LEFT)) {
            this.rotateYaw(sensitivity * dt);
        }

        if (JengaGame.isDown(GLFW_KEY_RIGHT)) {
            this.rotateYaw(-sensitivity*dt);
        }
        if (JengaGame.isDown(GLFW_KEY_DOWN)) {
            this.rotatePitch(-sensitivity*dt);
        }
    }
}
