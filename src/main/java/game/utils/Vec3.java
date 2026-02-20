package game.utils;

public final class Vec3 {
    public float x, y, z;


    public Vec3() {}

    public Vec3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3(float[] ray) {
        if (ray.length == 0) {
            return;
        }
        if (ray.length == 1) {
            this.x = ray[0];
            return;
        }
        this.y = ray[1];
        if (ray.length >= 3) {
            this.z = ray[2];
        }
    }

    //todo: rotation


}
