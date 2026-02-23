package game.utils;

import org.joml.Vector3f;

public record RayCollisionInfo(boolean hit, float dist, Vector3f pos) {


    @Override
    public String toString() {
        if (hit) {
            return String.format("%.2f -> %s", dist, pos.toString());
        } else {
            return "No hit";
        }
    }
}
