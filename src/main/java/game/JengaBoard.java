package game;

import game.utils.BoundingBox;
import game.utils.Normalized;
import org.joml.Vector3f;
import physics.PhysicsObject;

import java.util.ArrayList;

public class JengaBoard {

    //the list of all GameObjects that need to be updated
    ArrayList<GameObject> objects = new ArrayList<>();

    //jimmy
    public JengaBoard() {
        //initialize
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
//        p = objects.get(i);
//        if (p instanceof PhysicsObject po) {
//            po.updatePhysics(deltaTime);
//        }

        //update all regular objects second
        //p.update(deltaTime);
    }

    //jimmy
    //the @Normalized annotation means direction should be normalized when passed
    public GameObject castRay(Vector3f start, @Normalized Vector3f direction) {
        //for each GameObject, use .intersectsRay(start, direction) and return the closest object


        return null;
    }

    //jimmy
    public void checkCollision(GameObject obj) {
        /*
        * for each GameObject o:
        *       first make sure o != obj, so we dont count self-collision
        *       use GameObject.isColliding to check collision
        *       call OnCollision (should a normal force to physics objects)
        * */
    }


    //parker
    public void draw() {

    }
}
