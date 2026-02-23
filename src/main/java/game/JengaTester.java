package game;

import game.utils.BoundingBox;
import org.joml.Vector3f;
import physics.PhysicsObject;

import java.util.Comparator;

import static game.utils.FloatUtils.flteq;

public class JengaTester {

    //write test functions in here

    //ex: i wrote this to test the bounding box
    public static void testBoundingBox() {
        BoundingBox b1 = new BoundingBox(); //zeroed bounding box
        BoundingBox b2 = new BoundingBox(10, 30, 55, 10, 17, 20); //manual setup
        BoundingBox b3 = new BoundingBox(10, 20, 30); //10*20*30 dimensions, at origin
        BoundingBox b4 = new BoundingBox(3, 5, 9, 10); //10*10*10 cube at 3, 5, 9
        BoundingBox b5 = new BoundingBox(1); //1*1*1 cube at origin
        BoundingBox b6 = new BoundingBox(new Vector3f(1, 3, 5), 10, 20, 30); //10*20*30 at 1, 3, 5
        BoundingBox b7 = b5.withPosition(new Vector3f(1, 1, 1));

        System.out.println(b1);
        System.out.println(b2);
        System.out.println(b3);
        System.out.println(b4);
        System.out.println(b5);
        System.out.println(b6);
        System.out.println(b7);

        System.out.println(b2.intersects(b3));

        //cast a ray towards b2's center
        var start = new Vector3f();
        var dir = b2.getCenter().sub(start).normalize();

        System.out.println(b2.intersectsRay(start, dir));

        //test going the opposite direction
        System.out.println(b2.intersectsRay(start, dir.negate()));
    }

    public static void main(String[] args) {
        //test methods in here
        testBoundingBox();

        System.out.println("All tests passed!");
    }
}
