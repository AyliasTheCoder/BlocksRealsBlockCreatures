package me.aylias.minecraft.mods.modbase.client;

import me.aylias.minecraft.mods.modbase.client.hayhorse.HayHorseRenderer;
import me.aylias.minecraft.mods.modbase.events.EntityRegisterEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

@OnlyIn(Dist.CLIENT)
public class RenderRegistry {

    public static void registerEntityRenders() {
        RenderingRegistry.registerEntityRenderingHandler(EntityRegisterEvents.HAY_HORSE, new HayHorseRenderer.RenderFactory());
    }
}
