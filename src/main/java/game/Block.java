package game;

import game.graphics.ShaderProgram;
import game.utils.BoxCollider;
import physics.PhysicsBoard;
import physics.PhysicsObject;

public class Block extends PhysicsObject {
    public static final float block_w = 3.f;
    public static final float block_h = 1.f;
    public static final float block_d = 1.f;
    public static final float BLOCK_MASS = 1.f;

    public Block(PhysicsBoard physicsBoard) {
        super(physicsBoard, block_w, block_h, block_d, BLOCK_MASS);
    }

    public Block(PhysicsBoard board, float w, float h, float d, float m) {
        super(board, w, h, d, m);
    }


    @Override
    public void update(double deltaTime) {

    }



    @Override
    public GameObject clone() {
        return new Block(this.board,
                this.collider.transform.scale().x,
                this.collider.transform.scale().y,
                this.collider.transform.scale().z,
                (float) this.body.getMass().getMass()
                );
    }
}
