package game.Graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.function.Function;

public class Camera {

    protected Matrix4f viewMatrix = new Matrix4f();

    protected Vector3f position;
    protected Vector3f target = new Vector3f();


    public static final Vector3f WORLD_UP = new Vector3f(0f, 0f, 1f);


    protected float pitch = 0f, yaw = 0f;


    public Camera() {
        position = new Vector3f();
    }

    public Camera(Vector3f position, float pitch, float yaw) {
        this.position = position;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    protected void updateTarget() {
        target.x = 1f;
        target.y = 0f;
        target.z = 0f;

        target.rotateX(pitch);
        target.rotateZ(yaw);
        target.add(position);
    }
    
    public Vector3f getPosition() {
        return position;
    }
    
    public float getPitch() {
        return pitch;
    }
    
    public float getYaw() {
        return yaw;
    }

    public void addPitch(float angle_rads) {
        this.pitch += angle_rads;
    }

    public void addYaw(float angle_yaw) {
        this.yaw += angle_yaw;
    }

    public Matrix4f getViewMatrix() {
        updateTarget();

        return viewMatrix.identity()
                .lookAt(position, target, WORLD_UP);
    }

}
