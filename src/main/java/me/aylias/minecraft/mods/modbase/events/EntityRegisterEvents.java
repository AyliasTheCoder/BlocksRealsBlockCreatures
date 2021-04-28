package me.aylias.minecraft.mods.modbase.events;

import me.aylias.minecraft.mods.modbase.ModBase;
import me.aylias.minecraft.mods.modbase.entities.HayHorse;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityRegisterEvents {

    public static EntityType<HayHorse> HAY_HORSE = EntityType.Builder.<HayHorse>create(HayHorse::new, EntityClassification.CREATURE).size(1.3964844F, 1.6F).trackingRange(10).build(ModBase.MODID + ":hay_horse");

    @SubscribeEvent
    public static void registerEntities(final RegistryEvent.Register<EntityType<?>> e) {
        e.getRegistry().registerAll(
                HAY_HORSE.setRegistryName(ModBase.MODID, "hay_horse")
        );
    }

    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent e) {
        e.put(HAY_HORSE, AttributeModifierMap.createMutableAttribute().createMutableAttribute(Attributes.MAX_HEALTH, 25)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 16.0)
                .createMutableAttribute(Attributes.ARMOR)
                .create());
    }



}
