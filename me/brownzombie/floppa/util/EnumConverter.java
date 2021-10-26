package me.brownzombie.floppa.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class EnumConverter {

    private final Class clazz;

    public EnumConverter(Class clazz) {
        this.clazz = clazz;
    }

    public static int currentEnum(Enum clazz) {
        for (int i = 0; i < ((Enum[]) clazz.getClass().getEnumConstants()).length; ++i) {
            Enum e = ((Enum[]) ((Enum[]) clazz.getClass().getEnumConstants()))[i];

            if (e.name().equalsIgnoreCase(clazz.name())) {
                return i;
            }
        }

        return -1;
    }

    public static Enum increaseEnum(Enum clazz) {
        int index = currentEnum(clazz);

        for (int i = 0; i < ((Enum[]) clazz.getClass().getEnumConstants()).length; ++i) {
            Enum e = ((Enum[]) ((Enum[]) clazz.getClass().getEnumConstants()))[i];

            if (i == index + 1) {
                return e;
            }
        }

        return ((Enum[]) ((Enum[]) clazz.getClass().getEnumConstants()))[0];
    }

    public static String getProperName(Enum clazz) {
        return Character.toUpperCase(clazz.name().charAt(0)) + clazz.name().toLowerCase().substring(1);
    }

    public JsonElement doForward(Enum anEnum) {
        return new JsonPrimitive(anEnum.toString());
    }

    public Enum doBackward(JsonElement jsonElement) {
        try {
            return Enum.valueOf(this.clazz, jsonElement.getAsString());
        } catch (IllegalArgumentException illegalargumentexception) {
            return null;
        }
    }

    public Enum doBackward(String enumName) {
        try {
            return Enum.valueOf(this.clazz, enumName);
        } catch (IllegalArgumentException illegalargumentexception) {
            return null;
        }
    }
}
