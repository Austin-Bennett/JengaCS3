package game.utils;

import org.joml.Vector3f;


/*
* An AABB implementation
* AABB's are super nice because its easy to calculate collision with them
*
* */
public class BoundingBox implements Cloneable {
    //x, y, z, width, height, depth
    float x = 0, y = 0, z = 0, w = 0, h = 0, d = 0;

    public BoundingBox() {}

    //position = origin
    public BoundingBox(float size) {
        this.w = h = d = size;
    }

    public BoundingBox(float w, float h, float d) {
        this.w = w;
        this.h = h;
        this.d = d;

    }

    //cube constructor
    public BoundingBox(float x, float y, float z, float size) {
        this.x = x;
        this.y = y;
        this.z = z;

        this.w = h = d = size;
    }


    //manual constructor
    public BoundingBox(float x, float y, float z, float w, float h, float d) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        this.h = h;
        this.d = d;
    }

    //alternative manual construction with vector
    public BoundingBox(Vector3f pos, float w, float h, float d) {
        this.x = pos.x;
        this.y = pos.y;
        this.z = pos.z;

        this.w = w;
        this.h = h;
        this.d = d;
    }

    public float right() {
        return x + w;
    }

    public float top() {
        return y + h;
    }

    public float back() {
        return z + d;
    }



    //creates a new bounding box with the new position
    public BoundingBox withPosition(Vector3f pos) {
        var res = clone();
        res.x = pos.x;
        res.y = pos.y;
        res.z = pos.z;

        return res;
    }

    public Vector3f bottomLeft() {
        return new Vector3f(x, y, z);
    }

    public Vector3f bottomRight() {
        return new Vector3f(right(), y, z);
    }

    public Vector3f topLeft() {
        return new Vector3f(x, top(), z);
    }

    public Vector3f topRight() {
        return new Vector3f(right(), top(), z);
    }

    public Vector3f bottomRightBack() {
        return new Vector3f(right(), y, back());
    }

    public Vector3f topLeftBack() {
        return new Vector3f(x, top(), back());
    }

    public Vector3f topRightBack() {
        return new Vector3f(right(), top(), back());
    }

    public Vector3f getCenter() {
        return new Vector3f(x + w / 2, y + h / 2, z + d / 2);
    }

    //returns this for method chaining (this.addPos(pos).sub(pos2)
    public BoundingBox addPos(Vector3f pos) {
        this.x += pos.x;
        this.y += pos.y;
        this.z += pos.z;

        return this;
    }

    public BoundingBox subPos(Vector3f pos) {
        this.x -= pos.x;
        this.y -= pos.y;
        this.z -= pos.z;

        return this;
    }

    public boolean intersects(BoundingBox other) {
        if (right() < other.x) return false; //totally to the left
        if (x > other.right()) return false; //totally to the right
        if (top() < other.y) return false; //totally below
        if (y > other.top()) return false; //totally above
        if (back() < other.z) return false; //totally behind
        if (z < other.back()) return false; //totally in front

        return true;
    }

    public RayCollisionInfo intersectsRay(Vector3f start, @Normalized Vector3f dir) {
        // For each axis, compute t at the near and far face
        float tMinX = (x       - start.x) / dir.x;
        float tMaxX = (right() - start.x) / dir.x;
        float tMinY = (y       - start.y) / dir.y;
        float tMaxY = (top()   - start.y) / dir.y;
        float tMinZ = (z       - start.z) / dir.z;
        float tMaxZ = (back()  - start.z) / dir.z;

        // If dir on an axis is negative, the near/far are flipped
        float tEnterX = Math.min(tMinX, tMaxX);
        float tExitX  = Math.max(tMinX, tMaxX);
        float tEnterY = Math.min(tMinY, tMaxY);
        float tExitY  = Math.max(tMinY, tMaxY);
        float tEnterZ = Math.min(tMinZ, tMaxZ);
        float tExitZ  = Math.max(tMinZ, tMaxZ);

        // The ray enters the box when it has entered all 3 slabs
        float tEnter = Math.max(Math.max(tEnterX, tEnterY), tEnterZ);
        // The ray exits the box when it exits any slab
        float tExit  = Math.min(Math.min(tExitX,  tExitY),  tExitZ);

        // Hit if the entry point is before the exit, and the box isn't behind the ray
        var hit = tEnter <= tExit && tExit >= 0 && tEnter >= 0;
        if (hit) {
            Vector3f pos = new Vector3f(start).add(new Vector3f(dir).mul(tEnter));
            return new RayCollisionInfo(
                    true, //hit
                    tEnter, //distance
                    pos //position
            );
        } else {
            return new RayCollisionInfo(
                    false, //hit
                    -1, //distance
                    null //position
            );
        }
    }

    @Override
    public BoundingBox clone() {
        try {
            return (BoundingBox) super.clone();
        } catch (Exception e) {
            //do nothing
            return null;
        }
    }
}
