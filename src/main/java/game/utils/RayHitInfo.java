package game.utils;

import org.joml.Vector3f;

public record RayHitInfo(boolean hit, float dist, Vector3f hit_pos) {

    @Override
    public String toString() {
        if (hit) {
            return String.format("%.2f -> %s", dist, hit_pos.toString());
        } else {
            return "No hit";
        }
    }
}
