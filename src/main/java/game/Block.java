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
        super( BASE_DIMENSIONS.clone(), BLOCK_MASS, model);
    }

    public Block(Vector3f pos) {
        super(BASE_DIMENSIONS.withPosition(pos), BLOCK_MASS, model);
    }

    @Override
    public void update(double deltaTime) {
        //any extra frame-by-frame update logic goes in here
    }

}
