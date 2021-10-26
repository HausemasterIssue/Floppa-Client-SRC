package me.brownzombie.floppa.settings;

import java.util.function.Predicate;
import me.brownzombie.floppa.modules.Module;
import me.brownzombie.floppa.util.EnumConverter;

public class Setting {

    public String name;
    public String[] alias;
    public String desc;
    public Module module;
    private Predicate shown;
    private String type;
    public Object defaultValue;
    public Object value;
    public Object min;
    public Object max;

    public void setValue(Object value) {
        this.value = value;
    }

    public Setting(String name, String[] alias, String desc) {
        this.name = name;
        this.alias = alias;
        this.desc = desc;
    }

    public Setting(String name, String[] alias, String desc, Object value) {
        this(name, alias, desc);
        this.value = value;
        this.defaultValue = value;
        this.type = value.getClass().toString();
    }

    public Setting(String name, String[] alias, String desc, Object value, Object min, Object max) {
        this(name, alias, desc, value);
        this.min = min;
        this.max = max;
        this.type = value.getClass().toString();
    }

    public Setting(String name, String desc, Object value) {
        this(name, new String[0], desc);
        this.value = value;
        this.defaultValue = value;
        this.type = value.getClass().toString();
    }

    public Setting(String name, String desc, Object value, Object min, Object max) {
        this(name, new String[0], desc, value);
        this.min = min;
        this.max = max;
        this.type = value.getClass().toString();
    }

    public Setting(String name, Object value) {
        this(name, new String[0], "");
        this.value = value;
        this.defaultValue = value;
        this.type = value.getClass().toString();
    }

    public Setting(String name, Object value, Object min, Object max) {
        this(name, new String[0], "", value);
        this.min = min;
        this.max = max;
        this.type = value.getClass().toString();
    }

    public Setting(String name, Object value, Object min, Object max, Predicate shown) {
        this(name, new String[0], "", value);
        this.min = min;
        this.max = max;
        this.shown = shown;
        this.type = value.getClass().toString();
    }

    public Setting(String name, Object value, Predicate shown) {
        this(name, new String[0], "", value);
        this.shown = shown;
        this.type = value.getClass().toString();
    }

    public Object clamp(Object value, Object min, Object max) {
        return ((Comparable) value).compareTo(min) < 0 ? min : (((Comparable) value).compareTo(max) > 0 ? max : value);
    }

    public Object getValue() {
        return this.value;
    }

    public String getType() {
        return this.type;
    }

    public String getMeta() {
        return this.value == null ? "NULL" : this.value.toString();
    }

    public boolean getShown() {
        return this.shown == null ? false : !this.shown.test(this.getValue());
    }

    public Object parse(String string) {
        if (this.value instanceof Number && !(this.value instanceof Enum)) {
            if (this.value instanceof Integer) {
                return Integer.valueOf(Integer.parseInt(string));
            }

            if (this.value instanceof Float) {
                return Float.valueOf(Float.parseFloat(string));
            }

            if (this.value instanceof Double) {
                return Double.valueOf(Double.parseDouble(string));
            }

            if (this.value instanceof Long) {
                return Long.valueOf(Long.parseLong(string));
            }
        } else {
            if (this.value instanceof Boolean) {
                return Boolean.valueOf(Boolean.parseBoolean(string));
            }

            if (this.value instanceof Enum) {
                EnumConverter converter = new EnumConverter(((Enum) this.value).getClass());

                return converter.doBackward(string);
            }

            if (this.value instanceof String) {
                return this.value;
            }
        }

        return null;
    }
}
