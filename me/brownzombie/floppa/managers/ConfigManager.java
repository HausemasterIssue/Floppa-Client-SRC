package me.brownzombie.floppa.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import me.brownzombie.floppa.Floppa;
import me.brownzombie.floppa.modules.Module;
import me.brownzombie.floppa.modules.hud.HudModuleTemplate;
import me.brownzombie.floppa.settings.Setting;

public class ConfigManager {

    public File FloppaDir = new File("Floppa");
    public File Modules;
    public File HudElements;
    public ConcurrentHashMap modules = new ConcurrentHashMap();
    public ConcurrentHashMap elements = new ConcurrentHashMap();

    public ConfigManager() {
        if (!this.FloppaDir.exists()) {
            this.FloppaDir.mkdirs();
        }

        this.Modules = new File("Floppa" + File.separator + "Settings" + File.separator + "Modules");
        if (!this.Modules.exists()) {
            this.Modules.mkdirs();
        }

        this.HudElements = new File("Floppa" + File.separator + "Settings" + File.separator + "HudElements");
        if (!this.HudElements.exists()) {
            this.HudElements.mkdirs();
        }

        this.loadModules();
        this.loadElements();
    }

    public void addModule(Module module) {
        ConcurrentHashMap map = new ConcurrentHashMap();

        map.put("enabled", "" + module.isToggled());
        map.put("bind", "" + module.getKey());
        map.put("visible", "" + module.getVisible());
        Iterator iterator = module.settings.iterator();

        while (iterator.hasNext()) {
            Setting setting = (Setting) iterator.next();

            if (setting.value != null) {
                map.put(setting.name, setting.value.toString());
            }
        }

        this.modules.put(module.getName(), map);
    }

    public void addElement(HudModuleTemplate element) {
        ConcurrentHashMap map = new ConcurrentHashMap();

        map.put("enabled", "" + element.isToggled());
        map.put("x", "" + element.getX());
        map.put("y", "" + element.getY());
        this.elements.put(element.getName(), map);
    }

    public void save() {
        Floppa.moduleManager.getModules().forEach(this::addModule);
        Floppa.moduleManager.getElements().forEach(this::addElement);
        this.saveAllModules();
        this.saveAllElements();
    }

    public void saveAllModules() {
        try {
            Iterator iterator = this.modules.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.setPrettyPrinting().create();
                BufferedWriter writer = Files.newBufferedWriter(Paths.get("Floppa/Settings/Modules/" + (String) entry.getKey() + ".json", new String[0]), new OpenOption[0]);
                HashMap map = new HashMap();
                Iterator iterator1 = ((ConcurrentHashMap) entry.getValue()).entrySet().iterator();

                while (iterator1.hasNext()) {
                    Entry value = (Entry) iterator1.next();
                    String key = (String) value.getKey();
                    String val = (String) value.getValue();

                    map.put(key, val);
                }

                gson.toJson(map, writer);
                writer.close();
            }
        } catch (Exception exception) {
            ;
        }

    }

    public void saveAllElements() {
        try {
            Iterator iterator = this.elements.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.setPrettyPrinting().create();
                BufferedWriter writer = Files.newBufferedWriter(Paths.get("Floppa/Settings/HudElements/" + (String) entry.getKey() + ".json", new String[0]), new OpenOption[0]);
                HashMap map = new HashMap();
                Iterator iterator1 = ((ConcurrentHashMap) entry.getValue()).entrySet().iterator();

                while (iterator1.hasNext()) {
                    Entry value = (Entry) iterator1.next();
                    String key = (String) value.getKey();
                    String val = (String) value.getValue();

                    map.put(key, val);
                }

                gson.toJson(map, writer);
                writer.close();
            }
        } catch (Exception exception) {
            ;
        }

    }

    public void loadModules() {
        try {
            Stream e = Files.walk(Paths.get("Floppa/Settings/Modules/", new String[0]), new FileVisitOption[0]);
            Throwable throwable = null;

            try {
                e.filter((x$0) -> {
                    return Files.isRegularFile(x$0, new LinkOption[0]);
                }).forEach((path) -> {
                    try {
                        Gson ex = new Gson();
                        BufferedReader reader = Files.newBufferedReader(Paths.get("Floppa/Settings/Modules/" + path.getFileName().toString(), new String[0]));
                        Map map = (Map) ex.fromJson(reader, Map.class);
                        ConcurrentHashMap valsMap = new ConcurrentHashMap();
                        Iterator iterator = map.entrySet().iterator();

                        while (iterator.hasNext()) {
                            Entry entry = (Entry) iterator.next();
                            String key = (String) entry.getKey();
                            String val = (String) entry.getValue();

                            valsMap.put(key, val);
                        }

                        this.modules.put(path.getFileName().toString().substring(0, path.getFileName().toString().indexOf(".json")), valsMap);
                        reader.close();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }

                });
            } catch (Throwable throwable1) {
                throwable = throwable1;
                throw throwable1;
            } finally {
                if (e != null) {
                    if (throwable != null) {
                        try {
                            e.close();
                        } catch (Throwable throwable2) {
                            throwable.addSuppressed(throwable2);
                        }
                    } else {
                        e.close();
                    }
                }

            }
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }

    }

    public void loadElements() {
        try {
            Stream e = Files.walk(Paths.get("Floppa/Settings/HudElements/", new String[0]), new FileVisitOption[0]);
            Throwable throwable = null;

            try {
                e.filter((x$0) -> {
                    return Files.isRegularFile(x$0, new LinkOption[0]);
                }).forEach((path) -> {
                    try {
                        Gson ex = new Gson();
                        BufferedReader reader = Files.newBufferedReader(Paths.get("Floppa/Settings/HudElements/" + path.getFileName().toString(), new String[0]));
                        Map map = (Map) ex.fromJson(reader, Map.class);
                        ConcurrentHashMap valsMap = new ConcurrentHashMap();
                        Iterator iterator = map.entrySet().iterator();

                        while (iterator.hasNext()) {
                            Entry entry = (Entry) iterator.next();
                            String key = (String) entry.getKey();
                            String val = (String) entry.getValue();

                            valsMap.put(key, val);
                        }

                        this.elements.put(path.getFileName().toString().substring(0, path.getFileName().toString().indexOf(".json")), valsMap);
                        reader.close();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }

                });
            } catch (Throwable throwable1) {
                throwable = throwable1;
                throw throwable1;
            } finally {
                if (e != null) {
                    if (throwable != null) {
                        try {
                            e.close();
                        } catch (Throwable throwable2) {
                            throwable.addSuppressed(throwable2);
                        }
                    } else {
                        e.close();
                    }
                }

            }
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }

    }

    public void initModule(Module module) {
        if (this.modules.containsKey(module.getName())) {
            Iterator iterator = ((ConcurrentHashMap) this.modules.get(module.getName())).entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entryValue = (Entry) iterator.next();
                String key = (String) entryValue.getKey();
                String value = (String) entryValue.getValue();

                if (key.equalsIgnoreCase("enabled")) {
                    if (value.equals("true")) {
                        module.toggle();
                    }
                } else if (key.equalsIgnoreCase("bind")) {
                    module.setKey(Integer.parseInt(value));
                } else {
                    Iterator iterator1 = module.settings.iterator();

                    while (iterator1.hasNext()) {
                        Setting val = (Setting) iterator1.next();

                        if (val.name.equalsIgnoreCase(key)) {
                            val.setValue(val.parse(value));
                        }

                        if (val.name.equalsIgnoreCase(key) && val.getType().equals("class java.lang.String")) {
                            val.setValue(value);
                        }
                    }
                }
            }
        }

    }

    public void initElement(HudModuleTemplate element) {
        if (this.elements.containsKey(element.getName())) {
            Iterator iterator = ((ConcurrentHashMap) this.elements.get(element.getName())).entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entryValue = (Entry) iterator.next();
                String key = (String) entryValue.getKey();
                String value = (String) entryValue.getValue();

                if (key.equalsIgnoreCase("enabled") && value.equals("true")) {
                    element.toggle();
                }

                if (key.equalsIgnoreCase("x")) {
                    element.setX(Integer.parseInt(value));
                }

                if (key.equalsIgnoreCase("y")) {
                    element.setY(Integer.parseInt(value));
                }
            }
        }

    }
}
