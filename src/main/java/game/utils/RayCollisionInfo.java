package game.utils;

import org.joml.Vector3f;

public record RayCollisionInfo(boolean hit, float dist, Vector3f pos) {

}
