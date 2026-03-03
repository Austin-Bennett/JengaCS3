package game;

import game.utils.BoundingBox;
import game.utils.Normalized;
import game.utils.RayHitInfo;
import org.joml.Vector3f;
import physics.PhysicsObject;

import java.util.ArrayList;

public class JengaBoard {

    //the list of all GameObjects that need to be updated
    ArrayList<GameObject> objects = new ArrayList<>();

    //jimmy
    public JengaBoard() {
        // implement as seen fit
    }

    //jimmy
    public void addObject(GameObject obj) {
        obj.board = this;
        objects.add(obj);
    }

    //jimmy
    public void update(float deltaTime) {
        //call physics objects updates FIRST

        //use pattern matching:
        for (int i = 0; i < objects.size(); i++) {
            GameObject p;
            p = objects.get(i);
            if (p instanceof PhysicsObject po) {
                po.updatePhysics(deltaTime);
            }
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
    public GameObject castRay(Vector3f start, @Normalized Vector3f direction) {
        //for each GameObject, use .intersectsRay(start, direction) and return the closest object
        float min = Float.POSITIVE_INFINITY;
        GameObject minobj = null;
        for(GameObject o: objects){
            RayHitInfo hitmewithyourbestshot;
            hitmewithyourbestshot = o.getCollision().intersectsRay(start, direction);
            if(hitmewithyourbestshot.hit()){
                if(hitmewithyourbestshot.dist()<min){
                    min = hitmewithyourbestshot.dist();
                    minobj = o;
                }
            }
        }
        return minobj;
    }

    //jimmy
    public void checkCollision(GameObject obj) {
        /*
         * for each GameObject o:
         *       first make sure o != obj, so we dont count self-collision
         *       use GameObject.isColliding(obj.getCollision) to check collision
         *       call onCollision (should a normal force to physics objects)
         * */
        for(GameObject o: objects){
            if(o != obj){
                if(o.isColliding(obj.getCollision())){
                    o.onCollision(obj);
                    obj.onCollision(o);
                }
            }
        }
    }


    //parker
    public void draw() {

    }
}
