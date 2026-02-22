package game;

import game.utils.BoundingBox;
import org.joml.Vector3f;


//position is the center of the object
public abstract class GameObject {
    protected BoundingBox collision;
    protected JengaBoard board;


    public GameObject(BoundingBox collision) {
        this.collision = collision;
    }

    public abstract void update(double deltaTime);


    public void addPosition(Vector3f amt) {
        collision.addPos(amt);
    }

    public boolean isColliding(BoundingBox box) {
        return true;
    }

    public abstract void onCollision(GameObject other);

    public Vector3f getPosition() {
        return collision.getCenter();
    }

    public BoundingBox getCollision() {
        return collision;
    }
}
