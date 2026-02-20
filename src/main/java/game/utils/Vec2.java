package game.utils;
import static java.lang.Math.*;

public final class Vec2 implements Cloneable {
    public float x = 0;
    public float y = 0;

    public Vec2() {}

    public Vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vec2(float[] ray) {
        if (ray.length == 0) {
            return;
        }

        this.x = ray[0];
        if (ray.length >= 2) {
            this.y = ray[1];
        }
    }

    //theta is in radians
    public static Vec2 fromPolar(float length, float theta) {
        return new Vec2((float) (length * Math.cos(theta)), (float) (length * Math.sin(theta)));
    }

    //returns this
    public Vec2 add(Vec2 other) {
        this.x += other.x;
        this.y += other.y;

        return this;
    }

    //returns this
    public Vec2 sub(Vec2 other) {
        this.x -= other.x;
        this.y -= other.y;
        return this;
    }

    //returns this
    public Vec2 mul(Vec2 other) {
        this.x *= other.x;
        this.y *= other.y;
        return this;
    }

    //returns this
    public Vec2 div(Vec2 other) {
        this.x /= other.x;
        this.y /= other.y;
        return this;
    }

    //returns this
    public Vec2 scale(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }

    //returns this
    public Vec2 shrink(float scalar) {
        scalar = 1/scalar;

        return this.scale(scalar);
    }

    public float length2() {
        return this.x * this.x + this.y * this.y;
    }

    public float length() {
        return (float) Math.sqrt(this.length2());
    }

    //returns this
    public Vec2 rotate(float theta) {
        this.x = (float) (this.x * cos(theta) - y * sin(theta));
        this.y = (float) (this.x * sin(theta) + y * cos(theta));

        return this;
    }

    @Override
    public Vec2 clone() {
        try {
            return (Vec2) super.clone();
        } catch (Exception e) {
            throw new AssertionError();
        }
    }
}
