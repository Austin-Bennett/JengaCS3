package physics;

import game.GameObject;
import game.graphics.Model;
import game.utils.BoundingBox;
import org.joml.Vector2f;
import org.joml.Vector3f;


//brian do all this
public abstract class PhysicsObject extends GameObject {

    float mass;
    Vector3f velocity;
    Vector3f acceleration;

    public boolean simulate_physics = true;

    public float g = -9.8f;

    public float damp = 0.9f;

    public PhysicsObject(BoundingBox collision, float mass, Model m) {
        super(collision, m);
        velocity = new Vector3f();
        acceleration = new Vector3f();
        this.mass = mass;
    }


    public PhysicsObject(BoundingBox collision, float mass, Vector3f velocity, Vector3f acceleration, Model m) {
        super(collision, m);

        this.velocity = velocity;
        this.acceleration = acceleration;
        this.mass = mass;
    }


    public void updatePhysics(double deltaTime) {
        float dt = (float) deltaTime;

        if (this.simulate_physics) {
            this.acceleration.z += g;
            this.velocity.add(acceleration.x * dt, acceleration.y * dt, acceleration.z * dt);
            acceleration.set(0);

            // Don't move yet — let checkCollision do it with swept test
            Vector3f movement = new Vector3f(velocity).mul(dt);
            board.checkCollisionSwept(this, movement, dt);
        }
    }

    @Override
    public void onCollision(GameObject other, float dt) {
        BoundingBox myBox = this.getCollision();
        BoundingBox otherBox = other.getCollision();

        // Calculate overlap on each axis
        float overlapX = (myBox.w / 2 + otherBox.w / 2) - Math.abs(myBox.getCenter().x - otherBox.getCenter().x);
        float overlapY = (myBox.h / 2 + otherBox.h / 2) - Math.abs(myBox.getCenter().y - otherBox.getCenter().y);
        float overlapZ = (myBox.d / 2 + otherBox.d / 2) - Math.abs(myBox.getCenter().z - otherBox.getCenter().z);

        // Resolve along the axis of least penetration
        if (overlapX < overlapY && overlapX < overlapZ) {
            // Push out along X
            float sign = myBox.getCenter().x > otherBox.getCenter().x ? 1 : -1;
            this.addPosition(new Vector3f(sign * overlapX, 0, 0));
            this.velocity.x *= -damp;

        } else if (overlapY < overlapX && overlapY < overlapZ) {
            // Push out along Y
            float sign = myBox.getCenter().y > otherBox.getCenter().y ? 1 : -1;
            this.addPosition(new Vector3f(0, sign * overlapY, 0));
            this.velocity.y *= -damp;

        } else {
            // Push out along Z (most common — floor/ceiling)
            float sign = myBox.getCenter().z > otherBox.getCenter().z ? 1 : -1;
            this.addPosition(new Vector3f(0, 0, sign * overlapZ));
            this.velocity.z *= -damp;

            // Kill Z velocity if it's tiny (resting contact) to prevent jitter
            if (Math.abs(this.velocity.z) < 0.05f) {
                this.velocity.z = 0;
            }
        }
    }

    public void applyForce(Vector3f force) {
        //use F = Ma to calculate applied acceleration
        this.addAcceleration(force.div(this.mass));
    }

    public void addVelocity(Vector3f v) {
        this.velocity.add(v);
    }

    public void addAcceleration(Vector3f a) {
        this.acceleration.add(a);
    }
}