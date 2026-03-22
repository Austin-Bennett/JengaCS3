package game;

import game.graphics.ShaderProgram;
import game.utils.BoxCollider;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import physics.PhysicsBoard;
import physics.PhysicsObject;

import java.util.ArrayList;

public class JengaBoard extends PhysicsBoard {

    public class BoardHit {
        public boolean hit;
        public float dist;
        public Vector3f hit_pos;
        public GameObject object;


        public BoardHit(boolean hit, float dist, Vector3f hit_pos, GameObject o) {
            this.hit = hit;
            this.dist = dist;
            this.hit_pos = hit_pos;
            this.object = o;
        }
    }

    //the list of all GameObjects that need to be updated
    ArrayList<GameObject> objects = new ArrayList<>();
    public boolean do_physics = false;

    //jimmy
    public JengaBoard() {

        initPhysics();

// Static floor
        PhysicsObject floor = new Block(this, 7f, 1f, 7f, 99999f);
        floor.setPosition(0, 0, 0);
        floor.setStatic(true);

        addObject(floor);

// Tower
        float z_offset = 0.1f;
        float gap  = 0.05f;
        float step = 1.0f + gap;

        for (int layer = 0; layer < 18; layer++) {
            float z = 1.0f + layer * (1.0f + gap);
            boolean rotated = (layer % 2 == 1);

            for (int j = 0; j < 3; j++) {
                Block b = new Block(this, 3f, 1f, 1f, 1f);
                float offset = (j - 1) * step;

                if (rotated) {
                    b.setPosition(offset, 0, z + z_offset);
                    b.setRotation(0, 0, (float) Math.toRadians(90));
                } else {
                    b.setPosition(0, offset, z + z_offset);
                }
                addObject(b);
            }
        }

    }

    //jimmy
    public GameObject addObject(GameObject obj) {
        obj.board = this;
        objects.add(obj);

        return obj;
    }

    //jimmy
    public void update(float deltaTime) {
        //call physics objects updates FIRST

        if (do_physics) {
            stepPhysics(deltaTime);

            for (int i = 0; i < objects.size(); i++) {
                if (!(objects.get(i) instanceof PhysicsObject po)) {
                    continue;
                }

                po.syncTransform();
            }
        }

        if (JengaGame.isPressed(GLFW.GLFW_KEY_P)) {
            do_physics = !do_physics;
        }


        //update all regular objects second
        for(int i=0; i<objects.size(); i++) {
            GameObject p;
            p = objects.get(i);
            p.update(deltaTime);
        }


    }

    //parker
    public void draw(ShaderProgram shader, int model_loc, int normal_loc) {
        for (GameObject o: objects) {
            o.draw(shader, model_loc, normal_loc);
        }
    }
}
