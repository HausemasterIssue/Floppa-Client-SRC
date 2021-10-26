package me.brownzombie.floppa.mixin.mixins;

import me.brownzombie.floppa.Floppa;
import me.brownzombie.floppa.event.events.EventRenderArmorLayer;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ LayerArmorBase.class})
class MixinLayerArmorBase {

    @Inject(
        method = { "renderArmorLayer"},
        at = {             @At("HEAD")},
        cancellable = true
    )
    public void renderArmorLayer(EntityLivingBase p_Entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, EntityEquipmentSlot slotIn, CallbackInfo p_Info) {
        EventRenderArmorLayer l_Event = new EventRenderArmorLayer(p_Entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, slotIn);

        Floppa.EVENT_BUS.post(l_Event);
        if (l_Event.isCanceled()) {
            p_Info.cancel();
        }

    }
}
