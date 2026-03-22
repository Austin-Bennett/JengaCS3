package game.utils;

import org.joml.*;

public class Transform {

    private final Vector3f translation;
    private final Vector3f scale;
    private final Quaternionf rotation;

    private boolean dirty = true;
    private final Matrix4f m_matrix = new Matrix4f();
    private final Matrix3f n_matrix = new Matrix3f();

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public Transform() {
        this.translation = new Vector3f(0, 0, 0);
        this.scale       = new Vector3f(1, 1, 1);
        this.rotation    = new Quaternionf();
    }

    /** Position only; scale defaults to (1, 1, 1). */
    public Transform(Vector3f pos) {
        this.translation = new Vector3f(pos);
        this.scale       = new Vector3f(1, 1, 1);
        this.rotation    = new Quaternionf();
    }

    public Transform(Vector3f pos, Vector3f scale) {
        this.translation = new Vector3f(pos);
        this.scale       = new Vector3f(scale);
        this.rotation    = new Quaternionf();
    }

    public Transform(Vector3f pos, Vector3f scale, Quaternionf rotation) {
        this.translation = new Vector3f(pos);
        this.scale       = new Vector3f(scale);
        this.rotation    = new Quaternionf(rotation);
    }

    /** Copy constructor. */
    public Transform(Transform other) {
        this.translation = new Vector3f(other.translation);
        this.scale       = new Vector3f(other.scale);
        this.rotation    = new Quaternionf(other.rotation);
        this.dirty       = true;
    }

    //returns the internal translation
    public Vector3f translation() {
        return translation;
    }

    public Vector3f scale() {
        return scale;
    }

    public Quaternionf rotation() {
        return rotation;
    }


    // -------------------------------------------------------------------------
    // Internal matrix management
    // -------------------------------------------------------------------------

    private void updateMatrix() {
        if (dirty) {
            m_matrix.identity()
                    .translate(translation)
                    .rotate(rotation)
                    .scale(scale);

            n_matrix.set(m_matrix)
                    .invert()
                    .transpose();

            dirty = false;
        }
    }

    // -------------------------------------------------------------------------
    // Copy / Reset
    // -------------------------------------------------------------------------

    public void copy(Transform other) {
        this.translation.set(other.translation);
        this.scale.set(other.scale);
        this.rotation.set(other.rotation);
        this.dirty = true;
    }

    /** Resets this transform to identity (no translation, no rotation, scale 1). */
    public void reset() {
        this.translation.set(0, 0, 0);
        this.scale.set(1, 1, 1);
        this.rotation.identity();
        this.dirty = true;
    }

    // -------------------------------------------------------------------------
    // Translation
    // -------------------------------------------------------------------------

    public Vector3f getTranslation() {
        return new Vector3f(translation); // defensive copy
    }

    public void setTranslation(Vector3f translation) {
        this.translation.set(translation);
        this.dirty = true;
    }

    public void setTranslation(float x, float y, float z) {
        this.translation.set(x, y, z);
        this.dirty = true;
    }

    public void translate(Vector3f amt) {
        this.translation.add(amt);
        this.dirty = true;
    }

    public void translate(float x, float y, float z) {
        this.translation.add(x, y, z);
        this.dirty = true;
    }

    // -------------------------------------------------------------------------
    // Scale
    // -------------------------------------------------------------------------

    public Vector3f getScale() {
        return new Vector3f(scale); // defensive copy
    }

    public void setScale(Vector3f scale) {
        this.scale.set(scale);
        this.dirty = true;
    }

    public void setScale(float x, float y, float z) {
        this.scale.set(x, y, z);
        this.dirty = true;
    }

    /** Sets uniform scale on all axes. */
    public void setScale(float uniform) {
        this.scale.set(uniform, uniform, uniform);
        this.dirty = true;
    }

    /** Multiplies the current scale component-wise. */
    public void scale(Vector3f factor) {
        this.scale.mul(factor);
        this.dirty = true;
    }

    public void scale(float x, float y, float z) {
        this.scale.mul(x, y, z);
        this.dirty = true;
    }

    /** Applies a uniform scale multiplier on all axes. */
    public void scale(float uniform) {
        this.scale.mul(uniform);
        this.dirty = true;
    }

    // -------------------------------------------------------------------------
    // Rotation
    // -------------------------------------------------------------------------

    public Quaternionf getRotation() {
        return new Quaternionf(rotation); // defensive copy
    }

    public void setRotation(Quaternionf rotation) {
        this.rotation.set(rotation);
        this.dirty = true;
    }

    /** Sets rotation from Euler angles (radians) applied in X → Y → Z order. */
    public void setRotation(float x, float y, float z) {
        this.rotation.identity().rotateXYZ(x, y, z);
        this.dirty = true;
    }

    public void setRotation(Vector3f eulerAngles) {
        setRotation(eulerAngles.x, eulerAngles.y, eulerAngles.z);
    }

    /** Rotates around an arbitrary axis by angle (radians). */
    public void rotate(Vector3f axis, float angle) {
        this.rotation.rotateAxis(angle, axis);
        this.dirty = true;
    }

    /** Rotates by Euler angles (radians) applied in X → Y → Z order. */
    public void rotate(float x, float y, float z) {
        this.rotation.rotateXYZ(x, y, z);
        this.dirty = true;
    }

    public void rotate(Vector3f eulerAngles) {
        rotate(eulerAngles.x, eulerAngles.y, eulerAngles.z);
    }

    /**
     * Extracts approximate Euler angles (radians, XYZ order) from the rotation
     * quaternion. Returned as a new Vector3f(pitch, yaw, roll).
     */
    public Vector3f getEulerAngles() {
        Vector3f angles = new Vector3f();
        rotation.getEulerAnglesXYZ(angles);
        return angles;
    }

    // -------------------------------------------------------------------------
    // Direction vectors (derived from rotation)
    // -------------------------------------------------------------------------

    /** Returns the local forward vector (-Z axis rotated by this transform's rotation). */
    public Vector3f getForward() {
        return rotation.positiveZ(new Vector3f()).negate();
    }

    /** Returns the local right vector (+X axis rotated by this transform's rotation). */
    public Vector3f getRight() {
        return rotation.positiveX(new Vector3f());
    }

    /** Returns the local up vector (+Y axis rotated by this transform's rotation). */
    public Vector3f getUp() {
        return rotation.positiveY(new Vector3f());
    }

    // -------------------------------------------------------------------------
    // Look-at
    // -------------------------------------------------------------------------

    /**
     * Orients this transform so its forward axis points toward {@code target}
     * in world space.
     *
     * @param target world-space position to look at
     * @param up     world-space up hint (typically (0, 1, 0))
     */
    public void lookAt(Vector3f target, Vector3f up) {
        Vector3f direction = target.sub(translation, new Vector3f());
        if (direction.lengthSquared() < 1e-10f) return; // target is at same position
        this.rotation.lookAlong(direction, up);
        this.dirty = true;
    }

    // -------------------------------------------------------------------------
    // Combination
    // -------------------------------------------------------------------------

    /**
     * Returns a new Transform that represents applying {@code other} on top of
     * {@code this} (i.e. this × other, parent–child style composition).
     */
    public Transform combine(Transform other) {
        Vector3f combinedTranslation = rotation
                .transform(new Vector3f(other.translation).mul(scale))
                .add(translation);

        Vector3f combinedScale = new Vector3f(scale).mul(other.scale);

        Quaternionf combinedRotation = new Quaternionf(rotation).mul(other.rotation);

        return new Transform(combinedTranslation, combinedScale, combinedRotation);
    }

    // -------------------------------------------------------------------------
    // Matrix accessors
    // -------------------------------------------------------------------------

    /** Returns the (lazily updated) model matrix for this transform. */
    public Matrix4f getMatrix() {
        updateMatrix();
        return m_matrix;
    }

    public Matrix3f getNormalMatrix() {
        updateMatrix();
        return n_matrix;
    }

    /**
     * Returns the inverse of the model matrix. Useful for view matrices and
     * computing normal matrices on the CPU.
     */
    public Matrix4f getInverseMatrix() {
        return getMatrix().invert(new Matrix4f());
    }

    // -------------------------------------------------------------------------
    // Object overrides
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return "Transform{" +
                "translation=" + translation +
                ", scale=" + scale +
                ", rotation=" + rotation +
                '}';
    }
}