package game.utils;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Transform {
    private Vector3f translation;
    private Vector3f scale;
    private Quaternionf rotation;

    private boolean dirty = false;
    private Matrix4f matrix = new Matrix4f();



    private void update_matrix() {
        if (dirty) {
            matrix
                    .identity()
                    .translate(translation)
                    .rotate(rotation)
                    .scale(scale);

            dirty = false;
        }
    }

    public Transform() {
        this.translation = new Vector3f();
        this.scale = new Vector3f(1, 1, 1);
        this.rotation = new Quaternionf();
    }

    public Transform(Vector3f pos) {
        this.translation = pos;
        this.scale = new Vector3f();
        this.rotation = new Quaternionf();

    }

    public Transform(Vector3f pos, Vector3f scale) {
        this.translation = pos;
        this.scale = scale;
        this.rotation = new Quaternionf();

    }

    public Transform(Vector3f pos, Vector3f scale, Quaternionf rotator) {
        this.translation = pos;
        this.scale = scale;
        this.rotation = rotator;

    }

    public Transform(Transform other) {
        this.translation = new Vector3f(other.translation);
        this.scale = new Vector3f(other.scale);
        this.rotation = new Quaternionf(other.rotation);

        this.dirty = true;
    }

    public void copy(Transform other) {
        this.translation = new Vector3f(other.translation);
        this.scale = new Vector3f(other.scale);
        this.rotation = new Quaternionf(other.rotation);

        this.dirty = true;
    }

    public Vector3f getTranslation() {
        return this.translation;
    }

    public void setTranslation(Vector3f translation) {
        this.dirty = true;
        this.translation = translation;
    }

    public void translate(Vector3f amt) {
        this.dirty = true;
        this.translation.add(amt);
    }

    public void translate(float x, float y, float z) {
        this.dirty = true;
        this.translation.add(x, y, z);
    }

    public Vector3f getScale() {
        return this.scale;
    }

    public void setScale(Vector3f scale) {
        this.dirty = true;
        this.scale = scale;
    }

    public void scale(Vector3f scale) {
        this.dirty = true;
        this.scale.mul(scale);
    }

    public Quaternionf getRotator() {
        return this.rotation;
    }

    public void setRotation(Quaternionf rotation) {
        this.dirty = true;
        this.rotation = rotation;
    }

    public void rotate(Vector3f axis, float angle) {
        this.dirty = true;
        this.rotation.rotateAxis(angle, axis);
    }

    public void rotate(float x, float y, float z) {
        this.dirty = true;
        this.rotation
                .rotateX(x)
                .rotateY(y)
                .rotateZ(z);
    }

    public void rotate(Vector3f euler_angles) {
        this.dirty = true;
        this.rotation
                .rotateX(euler_angles.x)
                .rotateY(euler_angles.y)
                .rotateZ(euler_angles.z);
    }

    public Matrix4f getMatrix() {
        update_matrix();
        return matrix;
    }
}
