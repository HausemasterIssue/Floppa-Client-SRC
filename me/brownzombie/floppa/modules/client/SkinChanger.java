package me.brownzombie.floppa.modules.client;

import com.google.gson.Gson;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import javax.imageio.ImageIO;
import me.brownzombie.floppa.modules.Module;
import me.brownzombie.floppa.settings.Setting;

public class SkinChanger extends Module {

    public static SkinChanger INSTANCE;
    public File tmp;
    public Setting skinName = new Setting("NameToSteal", "imposter");

    public SkinChanger() {
        super("SkinChanger", "Changes your skin.", Module.Category.CLIENT);
        SkinChanger.INSTANCE = this;
    }

    public void onEnable() {
        BufferedImage image = null;

        try {
            this.tmp = new File("Floppa" + File.separator + "tmp");
            if (!this.tmp.exists()) {
                this.tmp.mkdirs();
            }

            Gson e = new Gson();
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + (String) this.skinName.getValue());
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            Map map = (Map) e.fromJson(reader, Map.class);
            ConcurrentHashMap valsMap = new ConcurrentHashMap();
            Iterator uuid = map.entrySet().iterator();

            while (uuid.hasNext()) {
                Entry url2 = (Entry) uuid.next();
                String key = (String) url2.getKey();
                String val = (String) url2.getValue();

                valsMap.put(key, val);
            }

            reader.close();
            String uuid1 = (String) valsMap.get("id");
            URL url21 = new URL("https://mc-heads.net/skin/" + uuid1);

            image = ImageIO.read(url21);
            ImageIO.write(image, "png", new File("Floppa/tmp/sus.png"));
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }

    }

    public void onDisable() {
        this.deleteSkinChangerFiles();
    }

    public void deleteSkinChangerFiles() {
        File[] afile = SkinChanger.mc.gameDir.listFiles();
        int i = afile.length;

        for (int j = 0; j < i; ++j) {
            File file = afile[j];

            if (!file.isDirectory() && file.getName().contains("-skinchanger")) {
                file.delete();
            }
        }

    }
}
