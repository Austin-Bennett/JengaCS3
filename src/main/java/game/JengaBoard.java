package game;

import game.graphics.ShaderProgram;
import game.utils.BoundingBox;
import game.utils.Normalized;
import game.utils.RayHitInfo;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import physics.PhysicsObject;

import java.util.ArrayList;

public class JengaBoard {

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

        Block unrotated = new Block();
        Block rotated = new Block();
        rotated.rotate90();
        rotated.addPosition(-1, 1, 0);

        {
            var blk = new Block(
                    new BoundingBox(
                            0.1f,
                            1.1f,
                            -2,
                            3.2f,
                            1,
                            3.2f
                    )
            );
            blk.simulate_physics = false;

            this.addObject(blk);
        }

        for (int i = 0; i < 18; i+=2) {
            for (int j = 0; j < 3; j++) {
                var blk = unrotated.clone();
                blk.addPosition(0.1f, j + (0.1f * j), i + 0.05f * i);
                this.addObject(blk);
            }
            for (int j = 0; j < 3; j++) {
                var blk = rotated.clone();
                blk.addPosition(j + (0.1f * j), 0, i+1 + 0.05f * (i+1));
                this.addObject(blk);
            }


        }
    }

    //jimmy
    public void addObject(GameObject obj) {
        obj.board = this;
        objects.add(obj);
    }

    //jimmy
    public void update(float deltaTime) {
        //call physics objects updates FIRST

        if (do_physics) {
            for (int i = 0; i < objects.size(); i++) {
                GameObject p;
                p = objects.get(i);
                if (p instanceof PhysicsObject po) {
                    po.updatePhysics(deltaTime);
                }
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

    //Jimmy
    //the @Normalized annotation means direction should be normalized when passed
    public BoardHit castRay(Vector3f start, @Normalized Vector3f direction, GameObject ignore) {
        //for each GameObject, use .intersectsRay(start, direction) and return the closest object
        BoardHit minobj = new BoardHit(false, 0f, new Vector3f(), null);
        for(GameObject o: objects){
            if (o == ignore) continue;
            RayHitInfo hitmewithyourbestshot;
            hitmewithyourbestshot = o.getCollision().intersectsRay(start, direction);
            if(hitmewithyourbestshot.hit()){
                if(hitmewithyourbestshot.dist()<minobj.dist || !minobj.hit){

                    minobj.hit = true;
                    minobj.dist = hitmewithyourbestshot.dist();
                    minobj.hit_pos = hitmewithyourbestshot.hit_pos();
                    minobj.object = o;
                }
            }
        }


        return minobj;
    }

    //jimmy
    public void checkCollision(GameObject obj, float dt) {
        /*
         * for each GameObject o:
         *       first make sure o != obj, so we dont count self-collision
         *       use GameObject.isColliding(obj.getCollision) to check collision
         *       call onCollision (should a normal force to physics objects)
         * */

        for(GameObject o: objects){
            if(o != obj){
                if(o.isColliding(obj.getCollision())){
                    obj.onCollision(o, dt);
                }
            }
        }
    }

    public void checkCollisionSwept(PhysicsObject obj, Vector3f movement, float dt) {
        BoundingBox swept = obj.getCollision().expandedByMovement(movement);

        float firstCollisionTime = 1.0f; // 1.0 = full movement, no collision
        GameObject firstHit = null;

        for (GameObject o : objects) {
            if (o == obj) continue;
            if (!o.getCollision().intersects(swept)) continue; // broad phase skip

            // Narrow phase: find exact time of collision on each axis
            BoundingBox a = obj.getCollision();
            BoundingBox b = o.getCollision();

            float tEnterX = 0, tExitX = 1;
            float tEnterY = 0, tExitY = 1;
            float tEnterZ = 0, tExitZ = 1;

            if (movement.x != 0) {
                tEnterX = (movement.x > 0 ? b.x - a.right() : b.right() - a.x) / movement.x;
                tExitX  = (movement.x > 0 ? b.right() - a.x : b.x - a.right()) / movement.x;
            } else if (a.x >= b.right() || a.right() <= b.x) {
                continue; // no overlap on X, and not moving on X — skip
            }

            if (movement.y != 0) {
                tEnterY = (movement.y > 0 ? b.y - a.top() : b.top() - a.y) / movement.y;
                tExitY  = (movement.y > 0 ? b.top() - a.y : b.y - a.top()) / movement.y;
            } else if (a.y >= b.top() || a.top() <= b.y) {
                continue;
            }

            if (movement.z != 0) {
                tEnterZ = (movement.z > 0 ? b.z - a.back() : b.back() - a.z) / movement.z;
                tExitZ  = (movement.z > 0 ? b.back() - a.z : b.z - a.back()) / movement.z;
            } else if (a.z >= b.back() || a.back() <= b.z) {
                continue;
            }

            float tEnter = Math.max(Math.max(tEnterX, tEnterY), tEnterZ);
            float tExit  = Math.min(Math.min(tExitX,  tExitY),  tExitZ);

            if (tEnter > tExit || tEnter > 1.0f || tExit < 0) continue;

            if (tEnter < firstCollisionTime) {
                firstCollisionTime = tEnter;
                firstHit = o;
            }
        }

        if (firstHit != null) {
            // Move up to the point of collision, then call onCollision
            obj.addPosition(new Vector3f(movement).mul(firstCollisionTime));
            obj.onCollision(firstHit, dt);
        } else {
            // No collision, move freely
            obj.addPosition(movement);
        }
    }


    //parker
    public void draw(ShaderProgram shader, int model_loc) {
        for (GameObject o: objects) {
            o.draw(shader, model_loc);
        }
    }
}
