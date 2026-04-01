package game.utils;


import game.graphics.Model;
import game.graphics.VertexBuffer;
import org.joml.Quaternionf;
import org.joml.Vector3f;

//a collider represented by 8 interconnected points
//Y+ = forward, Z+ = upwards
//also represents a box mesh for simplicity
public class BoxCollider {
    public Transform transform;


    public Model vertices = new Model(VertexBuffer.loadObj("src/main/resources/models/cube.obj"));

    public void setVertices(Model m) {
        this.vertices = m;
    }

    public BoxCollider(float w, float h, float d) {


        this.transform = new Transform();
        transform.setScale(w, d, h);
    }

    public BoxCollider(Transform transform) {
        this.transform = new Transform(transform);
    }

    public BoxCollider copy() {
        return new BoxCollider(transform);
    }

    public float getWidth() {
        return transform.scale().x;
    }

    public float getHeight() {
        return transform.scale().z;
    }

    public float getDepth() {
        return transform.scale().y;
    }

    public void setWidth(float w) {
        transform.setScale(w, transform.scale().y, transform.scale().z);
    }

    public void setHeight(float h) {
        transform.setScale(transform.scale().x, transform.scale().y, h);
    }

    public void setDepth(float d) {
        transform.setScale(transform.scale().x, d, transform.scale().z);
    }

    public Vector3f xyz() {
        return transform.getTranslation();
    }

    public float x() {
        return transform.translation().x;
    }

    public float y() {
        return transform.translation().y;
    }

    public float z() {
        return transform.translation().z;
    }




    //a collision
    /*
    * provides information about where the collision is hit (of course this is usually more like a whole area,
    * so its averaged from all detected points of collision)
    * */
    // Updated record — normal points away from 'other', toward 'this'
    public record Collision(
            boolean hit,
            float hitX,   float hitY,   float hitZ,       // averaged contact point
            float normalX, float normalY, float normalZ,  // collision normal
            float penetrationDepth
    ) {
        /** Sentinel for no collision — avoids allocating on the hot path. */
        public static final Collision NONE = new Collision(false, 0,0,0, 0,0,0, 0);

        public Vector3f hitPoint() { return new Vector3f(hitX, hitY, hitZ); }
        public Vector3f normal()   { return new Vector3f(normalX, normalY, normalZ); }
    }


    public Collision getCollision(BoxCollider other) {
        Vector3f[] axesA = getLocalAxes();
        Vector3f[] axesB = other.getLocalAxes();

        // 15 SAT axes: 3 from A, 3 from B, 9 edge cross-products
        Vector3f[] axes = new Vector3f[15];
        for (int i = 0; i < 3; i++) {
            axes[i]     = axesA[i];
            axes[3 + i] = axesB[i];
        }
        int idx = 6;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                axes[idx++] = new Vector3f(axesA[i]).cross(axesB[j]);
            }
        }

        float    minOverlap = Float.MAX_VALUE;
        Vector3f minAxis    = null;

        for (Vector3f axis : axes) {
            // Skip near-zero axes — these arise from parallel edges and aren't valid separators
            if (axis.lengthSquared() < 1e-10f) continue;
            axis.normalize();

            float[] pA = projectOnto(axis);
            float[] pB = other.projectOnto(axis);

            float overlap = Math.min(pA[1], pB[1]) - Math.max(pA[0], pB[0]);
            if (overlap <= 0f) return Collision.NONE; // found a separating axis — no collision

            if (overlap < minOverlap) {
                minOverlap = overlap;
                minAxis    = new Vector3f(axis);
            }
        }

        if (minAxis == null) return Collision.NONE;

        // Convention: normal points from 'other' toward 'this'
        if (new Vector3f(getCenter()).sub(other.getCenter()).dot(minAxis) < 0f) {
            minAxis.negate();
        }

        Vector3f contact = computeContactPoint(other);

        return new Collision(
                true,
                contact.x,  contact.y,  contact.z,
                minAxis.x,  minAxis.y,  minAxis.z,
                minOverlap
        );
    }

// ---------------------------------------------------------------------------
// Private helpers
// ---------------------------------------------------------------------------

    /** Center of this box in world space. */
    private Vector3f getCenter() {
        return transform.getTranslation();
    }

    /** The 3 world-space local axes (X, Y, Z) derived from this box's rotation. */
    private Vector3f[] getLocalAxes() {
        Quaternionf rot = transform.getRotation();
        return new Vector3f[]{
                rot.positiveX(new Vector3f()),
                rot.positiveY(new Vector3f()),
                rot.positiveZ(new Vector3f())
        };
    }

    /** All 8 world-space corner vertices of this OBB. */
    private Vector3f[] getWorldVertices() {
        Vector3f   c  = getCenter();
        Vector3f[] ax = getLocalAxes();

        Vector3f ex = new Vector3f(ax[0]).mul(transform.scale().x * 0.5f);
        Vector3f ey = new Vector3f(ax[1]).mul(transform.scale().y * 0.5f);
        Vector3f ez = new Vector3f(ax[2]).mul(transform.scale().z * 0.5f);

        return new Vector3f[]{
                new Vector3f(c).add(ex).add(ey).add(ez),
                new Vector3f(c).add(ex).add(ey).sub(ez),
                new Vector3f(c).add(ex).sub(ey).add(ez),
                new Vector3f(c).add(ex).sub(ey).sub(ez),
                new Vector3f(c).sub(ex).add(ey).add(ez),
                new Vector3f(c).sub(ex).add(ey).sub(ez),
                new Vector3f(c).sub(ex).sub(ey).add(ez),
                new Vector3f(c).sub(ex).sub(ey).sub(ez),
        };
    }

    /** Projects all 8 vertices onto [axis], returns {min, max}. */
    private float[] projectOnto(Vector3f axis) {
        float min = Float.MAX_VALUE, max = -Float.MAX_VALUE;
        for (Vector3f v : getWorldVertices()) {
            float p = v.dot(axis);
            if (p < min) min = p;
            if (p > max) max = p;
        }
        return new float[]{min, max};
    }

    /** Returns true if [worldPoint] lies inside this OBB. */
    public boolean containsPoint(Vector3f worldPoint) {
        Vector3f   local = new Vector3f(worldPoint).sub(getCenter());
        Vector3f[] ax    = getLocalAxes();
        return Math.abs(local.dot(ax[0])) <= transform.scale().x * 0.5f
                && Math.abs(local.dot(ax[1])) <= transform.scale().y * 0.5f
                && Math.abs(local.dot(ax[2])) <= transform.scale().z * 0.5f;
    }

    /**
     * Averages the penetrating vertices between the two boxes to produce a
     * single representative contact point.
     *
     * Tries vertices of [other] inside this box first, then this box's vertices
     * inside [other], then falls back to the midpoint between centers.
     */
    private Vector3f computeContactPoint(BoxCollider other) {
        Vector3f sum   = new Vector3f();
        int      count = 0;

        for (Vector3f v : other.getWorldVertices()) {
            if (containsPoint(v)) { sum.add(v); count++; }
        }

        if (count == 0) {
            for (Vector3f v : getWorldVertices()) {
                if (other.containsPoint(v)) { sum.add(v); count++; }
            }
        }

        return count > 0
                ? sum.div(count)
                : new Vector3f(getCenter()).add(other.getCenter()).div(2f);
    }


    public float raycast(Vector3f origin, Vector3f direction) {
        Transform tr = this.transform;

        // Inverse rotation: for a unit quaternion, conjugate == inverse.
        Quaternionf invRot = tr.getRotation().conjugate();

        // Transform ray origin into box local space:
        //   localOrigin = invRot * (origin - boxCenter) / boxScale
        Vector3f localOrigin = invRot.transform(
                new Vector3f(origin).sub(tr.getTranslation()));
        localOrigin.div(tr.getScale());

        // Transform ray direction into box local space:
        //   localDir = invRot * direction / boxScale
        // Dividing by scale keeps t consistent with the world-space ray.
        Vector3f localDir = invRot.transform(new Vector3f(direction));
        localDir.div(tr.getScale());

        // Slab test against the local AABB [-0.5, +0.5]^3
        float tMin = Float.NEGATIVE_INFINITY;
        float tMax = Float.POSITIVE_INFINITY;

        float[] o = { localOrigin.x, localOrigin.y, localOrigin.z };
        float[] d = { localDir.x,    localDir.y,    localDir.z    };

        for (int i = 0; i < 3; i++) {
            if (Math.abs(d[i]) < 1e-8f) {
                // Ray is parallel to this slab pair — miss if outside
                if (o[i] < -0.5f || o[i] > 0.5f) return -1f;
            } else {
                float t1 = (-0.5f - o[i]) / d[i];
                float t2 = ( 0.5f - o[i]) / d[i];

                if (t1 > t2) { float tmp = t1; t1 = t2; t2 = tmp; }

                tMin = Math.max(tMin, t1);
                tMax = Math.min(tMax, t2);

                if (tMin > tMax) return -1f; // slabs don't overlap
            }
        }

        // Whole interval is behind the ray origin
        if (tMax < 0f) return -1f;

        // Return the first t that is in front of the ray
        return tMin >= 0f ? tMin : tMax;
    }

    /**
     * Convenience: returns the intersection point in world space, or null on miss.
     */
    public Vector3f raycastPoint(Vector3f origin, Vector3f direction) {
        float t = raycast(origin, direction);
        if (t < 0f) return null;
        return new Vector3f(direction).mul(t).add(origin);
    }
}

