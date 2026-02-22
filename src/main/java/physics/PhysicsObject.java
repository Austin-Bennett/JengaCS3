package physics;

import game.GameObject;
import game.utils.BoundingBox;
import org.joml.Vector2f;
import org.joml.Vector3f;


//brian do all this
public abstract class PhysicsObject extends GameObject {

    float mass;
    Vector3f velocity;
    Vector3f acceleration;

    public PhysicsObject(BoundingBox collision, float mass) {
        super(collision);
        velocity = new Vector3f();
        acceleration = new Vector3f();
        this.mass = mass;
    }


    public PhysicsObject(BoundingBox collision, float mass, Vector3f velocity, Vector3f acceleration) {
        super(collision);

        this.velocity = velocity;
        this.acceleration = acceleration;
        this.mass = mass;
    }


    public void updatePhysics(double deltaTime) {
        //update all physics in here (add velocity to position, acceleration to velocity, reset velocity)
        //note that all updates should be applied with respect to delta time, in other words,
        //multiply everything by deltaTime
    }

    @Override
    public void onCollision(GameObject other) {
        //this one is a bit tricky, because we want to calculate an appropriate normal force to apply
        //in order to stop colliding
        //i think you will find dot products very useful here
        /*
        * DOT PRODUCTS TUTORIAL
        * =====================
        * given 2 vectors v and u,
        * v • u = v.x * u.x + v.y * u.y + v.z * u.z
        * geometric definition:
        * v • u = v.length() * u.length() * cos(θ), where θ is the angle between the vectors
        *
        * so in other words, the dot product is proportional to the amount which one vector points
        * to another vector because of the cos(θ)
        * EXAMPLE:
        * ===========
        * if θ is very small (i.e the 2 vectors point in the same direction) cos(θ) ≈ 1
        * if θ is close to 90 degrees, (i.e the 2 vectors are perpendicular, cos(θ) ≈ 0
        * if θ is close to 180 degrees, (i.e the 2 vectors are facing opposite to each other) cos(θ) ≈ -1
        *
        * so the idea is simple: calculate the dot product between the direction from the center of this object
        * to the center of other (normalized)
        * and the net acceleration on this object (normalized)
        * then multiply the net acceleration by this number to get the normal force applied
        *
        * in other words,
        *
        * let v = this.position() - other.position()
        * let u = this.acceleration
        * let d = v • u
        * this.acceleration.multiply(d)
        * */
    }

    public void applyForce(Vector3f force) {
        //use F = Ma to calculate applied acceleration
    }

    public void addVelocity(Vector3f v) {

    }

    public void addAcceleration(Vector3f a) {

    }
}
