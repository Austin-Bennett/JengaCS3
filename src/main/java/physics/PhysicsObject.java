package physics;

import game.GameObject;
import game.graphics.ShaderProgram;
import game.utils.BoxCollider;
import org.ode4j.math.DQuaternion;
import org.ode4j.math.DQuaternionC;
import org.ode4j.math.DVector3C;
import org.ode4j.ode.*;

public abstract class PhysicsObject extends GameObject {

    public BoxCollider collider; // still used for rendering
    protected DBody body;
    protected DGeom geom;

    public PhysicsObject(PhysicsBoard board, float w, float h, float d, float mass) {
        // Create the rigid body
        body = OdeHelper.createBody(board.getWorld());

        // Set mass — ode4j computes the inertia tensor for you
        DMass m = OdeHelper.createMass();
        m.setBox(1.0, w, d, h); // density=1, it scales to correct mass
        m.adjust(mass);
        body.setMass(m);

        // Create box collision geometry and attach to body
        geom = OdeHelper.createBox(board.getSpace(), w, d, h);
        geom.setBody(body);

        // Keep your Transform in sync for rendering
        collider = new BoxCollider(w, h, d);
    }

    public void setPosition(float x, float y, float z) {
        body.setPosition(x, y, z);
    }

    public void setRotation(float x, float y, float z) {
        // ode4j uses quaternions internally
        DQuaternion q = new DQuaternion();
        OdeMath.dQFromAxisAndAngle(q, 0, 0, 1, z);
        body.setQuaternion(q);
    }

    public void setStatic(boolean isStatic) {
        if (isStatic) body.setKinematic(); // won't be affected by forces
        else          body.setDynamic();
    }

    // Call this every frame AFTER stepPhysics() to sync the render transform
    public void syncTransform() {
        DVector3C pos = body.getPosition();
        DQuaternionC q = body.getQuaternion();

        collider.transform.setTranslation((float) pos.get0(), (float) pos.get1(), (float) pos.get2());
        collider.transform.setRotation(new org.joml.Quaternionf(
                (float) q.get1(), (float) q.get2(), (float) q.get3(), (float) q.get0()
        ));
    }

    public void applyImpulse(float x, float y, float z) {
        body.addForce(x, y, z);
    }

    public void applyImpulseAtPoint(
            float fx, float fy, float fz,
            float px, float py, float pz) {
        body.addForceAtPos(fx, fy, fz, px, py, pz);
    }

    @Override
    public void draw(ShaderProgram shader, int mat_model_loc, int mat_normal_loc) {
        syncTransform();
        shader.setUniformMatrix4(mat_model_loc, collider.transform.getMatrix());
        shader.setUniformMatrix3(mat_normal_loc, collider.transform.getNormalMatrix());
        collider.vertices.draw();
    }
}