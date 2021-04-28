package me.aylias.minecraft.mods.modbase;

import me.aylias.minecraft.mods.modbase.client.RenderRegistry;
import me.aylias.minecraft.mods.modbase.util.RegistryHandler;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ModBase.MODID)
public class ModBase
{
    public static final String MODID = "br_block_creatures";
    private static final Logger LOGGER = LogManager.getLogger();

    public ModBase() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        RegistryHandler.init();

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {}

    private void doClientStuff(final FMLClientSetupEvent event) {
        RenderRegistry.registerEntityRenders();
    }

    /*
    public static final ItemGroup TAB = new ItemGroup("blockcreatures") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(RegistryHandler.EXAMPLEITEM.get());
        }
    };

     */
}
