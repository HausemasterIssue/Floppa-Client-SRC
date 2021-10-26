package me.brownzombie.floppa.util;

public class AnimationUtil {

    public static float clamp(float number, float min, float max) {
        return number < min ? min : Math.min(number, max);
    }
}
