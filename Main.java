import me.brownzombie.floppa.Floppa;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(
    modid = "floppa",
    name = "Floppa",
    version = "1.0"
)
public class Main {

    @EventHandler
    public void init(FMLInitializationEvent event) throws IllegalAccessException {
        Floppa.instance = new Floppa();
        Floppa.instance.init();
    }
}
