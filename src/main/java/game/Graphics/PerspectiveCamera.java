package game.Graphics;


import game.JengaGame;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class PerspectiveCamera extends Camera {
    private Matrix4f projection = new Matrix4f();

    public float fov = (float) Math.toRadians(45);
    public float aspectRatio = (float) JengaGame.WINDOW_WIDTH / JengaGame.WINDOW_HEIGHT;

    //NEVER set to 0.0
    public float zNear = 0.01f;
    public float zFar = 100.0f;


    public PerspectiveCamera() {

    }

    public PerspectiveCamera(Vector3f pos, float pitch, float yaw) {
        super(pos, pitch, yaw);
    }




    public Matrix4f getProjection() {
        return projection.perspective(fov, aspectRatio, zNear, zFar);
    }

}
