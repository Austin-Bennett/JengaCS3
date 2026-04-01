package game.graphics;

import game.JengaGame;
import org.joml.Matrix4f;

public class PerspectiveCamera extends Camera {
    protected Matrix4f proj = new Matrix4f();

    protected float fov    = (float) Math.PI / 3;
    protected float aspect = (float) JengaGame.WINDOW_WIDTH / JengaGame.WINDOW_HEIGHT;
    protected float near   = 0.01f;
    protected float far    = 100f;

    public PerspectiveCamera() {
        updatePerspective();
    }

    public void updatePerspective() {
        proj
            .identity()
            .perspective(fov, aspect, near, far)
        ;
    }

    @Override
    public Matrix4f getProjection() {
        return proj;
    }

    @Override
    public void onResize(int w, int h) {
        aspect = (float) w / (float) h;
        updatePerspective();
    }
}
