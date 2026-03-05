package game.Graphics;

import game.utils.FloatUtils;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public abstract class Camera {
    protected Matrix4f view = new Matrix4f();
    protected boolean dirty = true;

    protected Vector3f position = new Vector3f();
    protected Vector3f target = new Vector3f(0, 1, 0);
    protected float pitch = 0f;
    protected float yaw = 0f;

    public static final Vector3f WORLD_FORWARD = new Vector3f(0, 1f, 0f);
    public static final Vector3f WORLD_RIGHT = new Vector3f(1f, 0f, 0f);
    public static final Vector3f WORLD_UP = new Vector3f(0f, 0f, 1f);


    public Camera() {
        updateDirty();
    }

    public Vector3f forward() {
        return new Vector3f(WORLD_FORWARD)
                .rotateX(pitch)
                .rotateZ(yaw);
    }

    public Vector3f right() {
        return new Vector3f(WORLD_FORWARD)
                .rotateZ(yaw - FloatUtils.PI_2);
    }


    public Vector3f up() {
        return new Vector3f(WORLD_FORWARD)
                .rotateX(pitch + FloatUtils.PI_2);
    }



    public void moveForward(float amt) {
        this.position.add(this.forward().mul(amt));
        dirty = true;
    }


    public void moveRight(float amt) {
        this.position.add(this.right().mul(amt));
        dirty = true;
    }


    public void moveUp(float amt) {
        this.position.add(this.up().mul(amt));
        dirty = true;
    }


    private void updateTarget() {
        this.target.set(WORLD_FORWARD);

        this.target
                .rotateX(pitch)
                .rotateZ(yaw)
                .add(position);
    }

    private void updateDirty() {
        if (dirty) {

            updateTarget();
            view.identity()
                    .lookAt(position, target, WORLD_UP);
            dirty = false;
        }
    }

    public float rotateYaw(float amt) {
        this.yaw += amt;
        this.dirty = true;

        return this.yaw;
    }

    public float yaw() {
        return this.yaw;
    }

    public float rotatePitch(float amt) {
        this.pitch = Math.clamp(this.pitch + amt, -(FloatUtils.PI_2-FloatUtils.epsilon), FloatUtils.PI_2-FloatUtils.epsilon);
        this.dirty = true;
        return this.pitch;
    }

    public float pitch() {
        return this.pitch;
    }

    public void addPosition(float x, float y, float z) {
        this.position.add(x, y, z);
        dirty = true;
    }

    public Vector3f position() {
        return this.position;
    }


    public Matrix4f getView() {
        updateDirty();

        return view;
    }

    public abstract Matrix4f getProjection();


}
