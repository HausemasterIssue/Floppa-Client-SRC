package me.brownzombie.floppa.util;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;

public class SkinStorageManipulationer {

    public static ResourceLocation getTexture() {
        BufferedImage bufferedImage = null;

        try {
            bufferedImage = ImageIO.read(new File("Floppa/tmp/sus.png"));
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        DynamicTexture texture = new DynamicTexture(bufferedImage);
        SkinStorageManipulationer.WrappedResource wr = new SkinStorageManipulationer.WrappedResource(FMLClientHandler.instance().getClient().getTextureManager().getDynamicTextureLocation("sus.png", texture));

        return wr.location;
    }

    static class WrappedResource {

        final ResourceLocation location;

        WrappedResource(ResourceLocation location) {
            this.location = location;
        }
    }
}
