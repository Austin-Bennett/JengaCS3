package game;

import game.graphics.Model;
import game.graphics.ShaderProgram;
import game.utils.BoundingBox;
import game.utils.Transform;
import org.joml.Vector3f;


//position is the center of the object
public abstract class GameObject {
    protected BoundingBox collision;
    protected Model model;
    protected JengaBoard board;


    public GameObject(BoundingBox collision, Model model)
    {
        this.collision = collision;
        this.model = model;
    }

    public abstract void update(double deltaTime);

    public void draw(ShaderProgram shader, int mat_model_loc) {
        shader.setUniformMatrix(mat_model_loc, model.getMatrix());
        model.draw();
    }

    public void addPosition(Vector3f amt) {
        collision.addPos(amt);
        model.transform.translate(amt);
    }



    public boolean isColliding(BoundingBox box) {
        return box.intersects(this.collision);
    }

    public abstract void onCollision(GameObject other);

    public Vector3f getPosition() {
        return collision.getCenter();
    }

    public BoundingBox getCollision() {
        return collision;
    }
}
