package me.aylias.minecraft.mods.modbase.client.hayhorse;

import me.aylias.minecraft.mods.modbase.ModBase;
import me.aylias.minecraft.mods.modbase.entities.HayHorse;
import net.minecraft.client.renderer.entity.AbstractHorseRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.HorseModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class HayHorseRenderer extends AbstractHorseRenderer<HayHorse, HorseModel<HayHorse>> {

    public HayHorseRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new HorseModel<>(0.0f), 1.1f);
    }

    @Override
    public ResourceLocation getEntityTexture(HayHorse entity) {
        return new ResourceLocation(ModBase.MODID, "textures/entity/hay_horse.png");
    }

    public static class RenderFactory implements IRenderFactory<HayHorse> {

        @Override
        public EntityRenderer<? super HayHorse> createRenderFor(EntityRendererManager manager) {
            return new HayHorseRenderer(manager);
        }
    }
}
