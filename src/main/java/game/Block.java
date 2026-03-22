package game;

import game.graphics.Model;
import game.graphics.VertexBuffer;
import game.utils.BoundingBox;
import org.joml.Vector3f;
import physics.PhysicsObject;

public class Block extends PhysicsObject {

    public static final float BLOCK_MASS = 1.0f; //set this to a good number for mass, or leave as is to tweak later
    public static final BoundingBox BASE_DIMENSIONS = new BoundingBox(3, 1, 1); //set the dimensions here
    public static final Model model = BASE_DIMENSIONS.intoBox();

    public Block() {
        super( BASE_DIMENSIONS.clone(), BLOCK_MASS, model.clone());
    }

    public Block(Vector3f pos) {
        super(BASE_DIMENSIONS.withPosition(pos), BLOCK_MASS, model.clone());
    }

    public Block(BoundingBox override_box) {
        super(override_box, BLOCK_MASS, override_box.intoBox());
    }

    @Override
    public void update(double deltaTime) {
        //any extra frame-by-frame update logic goes in here
    }

    public void rotate90() {
        float ox = collision.w / 2;
        float oy = collision.d / 2;


        collision.x = -oy + (collision.x - ox);
        collision.y = ox + (collision.y - oy);


        float tw = collision.w;
        collision.w = collision.d;
        collision.d = tw;

        this.getTransform().rotate(0, 0, (float) Math.toRadians(90));
    }

    @Override
    public GameObject clone() {
        var res = new Block(this.getPosition());
        res.getTransform().copy(this.getTransform());

        return res;
    }
}
