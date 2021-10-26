package me.brownzombie.floppa.managers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import me.brownzombie.floppa.Floppa;
import me.brownzombie.floppa.modules.Module;
import me.brownzombie.floppa.modules.client.Chat;
import me.brownzombie.floppa.modules.client.ClickGuiModule;
import me.brownzombie.floppa.modules.client.NameChanger;
import me.brownzombie.floppa.modules.client.SkinChanger;
import me.brownzombie.floppa.modules.combat.AutoCrystal;
import me.brownzombie.floppa.modules.combat.AutoCrystalBomb;
import me.brownzombie.floppa.modules.combat.HoleFill;
import me.brownzombie.floppa.modules.combat.InstaMine;
import me.brownzombie.floppa.modules.combat.Luck;
import me.brownzombie.floppa.modules.hud.HudColorsModule;
import me.brownzombie.floppa.modules.hud.HudEditorModule;
import me.brownzombie.floppa.modules.hud.HudModuleTemplate;
import me.brownzombie.floppa.modules.hud.NotificationsModule;
import me.brownzombie.floppa.modules.hud.elements.Coordinates;
import me.brownzombie.floppa.modules.hud.elements.Notifications;
import me.brownzombie.floppa.modules.hud.elements.TextRadar;
import me.brownzombie.floppa.modules.hud.elements.Watermark;
import me.brownzombie.floppa.modules.hud.elements.WatermarkTwo;
import me.brownzombie.floppa.modules.misc.FakePlayer;
import me.brownzombie.floppa.modules.misc.PacketMine;
import me.brownzombie.floppa.modules.misc.RPCModule;
import me.brownzombie.floppa.modules.movement.AutoSprint;
import me.brownzombie.floppa.modules.movement.BigJump;
import me.brownzombie.floppa.modules.offhand.OffhandCrystal;
import me.brownzombie.floppa.modules.offhand.OffhandGap;
import me.brownzombie.floppa.modules.offhand.OffhandTotem;
import me.brownzombie.floppa.modules.offhand.SimpleOffhand;
import me.brownzombie.floppa.modules.player.Burrow;
import me.brownzombie.floppa.modules.player.FastWeb;
import me.brownzombie.floppa.modules.player.SelfWeb;
import me.brownzombie.floppa.modules.render.AmongUs;
import me.brownzombie.floppa.modules.render.BlockHighlight;
import me.brownzombie.floppa.modules.render.CrystalChams;
import me.brownzombie.floppa.modules.render.Finder;
import me.brownzombie.floppa.modules.render.Fullbright;
import me.brownzombie.floppa.modules.render.HandChams;
import me.brownzombie.floppa.modules.render.ToolTips;
import me.brownzombie.floppa.modules.render.Trajectories;
import me.brownzombie.floppa.settings.Setting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Post;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class ModuleManager {

    private final ArrayList elements = new ArrayList();
    private ArrayList modules = new ArrayList();

    public ModuleManager() throws IllegalAccessException {
        this.Add((Module) (new ClickGuiModule()));
        this.Add((Module) (new Chat()));
        this.Add((Module) (new NameChanger()));
        this.Add((Module) (new SkinChanger()));
        this.Add((Module) (new AutoCrystal()));
        this.Add((Module) (new AutoCrystalBomb()));
        this.Add((Module) (new InstaMine()));
        this.Add((Module) (new HoleFill()));
        this.Add((Module) (new Luck()));
        this.Add((Module) (new HudEditorModule()));
        this.Add((Module) (new HudColorsModule()));
        this.Add((Module) (new NotificationsModule()));
        this.Add((HudModuleTemplate) (new Watermark()));
        this.Add((HudModuleTemplate) (new Coordinates()));
        this.Add((HudModuleTemplate) (new Notifications()));
        this.Add((HudModuleTemplate) (new WatermarkTwo()));
        this.Add((HudModuleTemplate) (new TextRadar()));
        this.Add((Module) (new RPCModule()));
        this.Add((Module) (new FakePlayer()));
        this.Add((Module) (new PacketMine()));
        this.Add((Module) (new AutoSprint()));
        this.Add((Module) (new BigJump()));
        this.Add((Module) (new SimpleOffhand()));
        this.Add((Module) (new OffhandTotem()));
        this.Add((Module) (new OffhandCrystal()));
        this.Add((Module) (new OffhandGap()));
        this.Add((Module) (new Burrow()));
        this.Add((Module) (new FastWeb()));
        this.Add((Module) (new SelfWeb()));
        this.Add((Module) (new Fullbright()));
        this.Add((Module) (new HandChams()));
        this.Add((Module) (new BlockHighlight()));
        this.Add((Module) (new Finder()));
        this.Add((Module) (new CrystalChams()));
        this.Add((Module) (new AmongUs()));
        this.Add((Module) (new Trajectories()));
        this.Add((Module) (new ToolTips()));
        ArrayList arraylist = this.modules;
        ConfigManager configmanager = Floppa.configManager;

        Floppa.configManager.getClass();
        arraylist.forEach(configmanager::initModule);
        arraylist = this.elements;
        configmanager = Floppa.configManager;
        Floppa.configManager.getClass();
        arraylist.forEach(configmanager::initElement);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void Add(Module module) throws IllegalAccessException {
        Field[] afield = module.getClass().getDeclaredFields();
        int i = afield.length;

        for (int j = 0; j < i; ++j) {
            Field field = afield[j];

            if (Setting.class.isAssignableFrom(field.getType())) {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }

                Setting val = (Setting) field.get(module);

                val.module = module;
                module.settings.add(val);
            }
        }

        this.modules.add(module);
    }

    public void Add(HudModuleTemplate element) throws IllegalAccessException {
        this.elements.add(element);
    }

    public Module getModule(String name) {
        Iterator iterator = this.modules.iterator();

        Module m;

        do {
            if (!iterator.hasNext()) {
                return null;
            }

            m = (Module) iterator.next();
        } while (!m.getName().equalsIgnoreCase(name));

        return m;
    }

    public HudModuleTemplate getElement(String name) {
        Iterator iterator = this.elements.iterator();

        HudModuleTemplate e;

        do {
            if (!iterator.hasNext()) {
                return null;
            }

            e = (HudModuleTemplate) iterator.next();
        } while (!e.getName().equalsIgnoreCase(name));

        return e;
    }

    public Module getModuleByClass(Class clazz) {
        Iterator iterator = this.modules.iterator();

        Module module;

        do {
            if (!iterator.hasNext()) {
                return null;
            }

            module = (Module) iterator.next();
        } while (!clazz.isInstance(module));

        return module;
    }

    public ArrayList getModules() {
        return this.modules;
    }

    public ArrayList getElements() {
        return this.elements;
    }

    public Setting getSetting(String setting, Module m) {
        Iterator iterator = m.settings.iterator();

        Setting s;

        do {
            if (!iterator.hasNext()) {
                return null;
            }

            s = (Setting) iterator.next();
        } while (!s.name.equals(setting));

        return s;
    }

    public ArrayList getModulesInCategory(Module.Category c) {
        ArrayList mods = new ArrayList();
        Iterator iterator = this.modules.iterator();

        while (iterator.hasNext()) {
            Module m = (Module) iterator.next();

            if (m.getCategory() == c) {
                mods.add(m);
            }
        }

        return mods;
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event) {
        Iterator iterator = ((List) this.modules.stream().filter(Module::isToggled).collect(Collectors.toList())).iterator();

        while (iterator.hasNext()) {
            Module e = (Module) iterator.next();

            e.onUpdate();
        }

        iterator = ((List) this.elements.stream().filter(HudModuleTemplate::isToggled).collect(Collectors.toList())).iterator();

        while (iterator.hasNext()) {
            HudModuleTemplate e1 = (HudModuleTemplate) iterator.next();

            e1.onUpdate();
        }

    }

    @SubscribeEvent
    public void onRenderGameOverLay(RenderGameOverlayEvent event) {
        if (event instanceof Post && event.getType().equals(ElementType.HOTBAR)) {
            Iterator iterator = ((List) this.elements.stream().filter(HudModuleTemplate::isToggled).collect(Collectors.toList())).iterator();

            while (iterator.hasNext()) {
                HudModuleTemplate e = (HudModuleTemplate) iterator.next();

                e.onRenderOverlay(event.getPartialTicks());
            }
        }

    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        Iterator iterator = ((List) this.modules.stream().filter(Module::isToggled).collect(Collectors.toList())).iterator();

        while (iterator.hasNext()) {
            Module m = (Module) iterator.next();

            m.onRender(event.getPartialTicks());
        }

    }
}
