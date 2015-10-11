package questsheets;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import questsheets.commands.ParentCommand;
import questsheets.reference.Metadata;
import questsheets.reference.Reference;

import java.io.File;
import java.util.Map;

@Mod(name = Reference.NAME, modid = Reference.ID, version = Reference.VERSION_FULL)
public class QuestSheets
{
    public static File configDir;

    private static EntityPlayer commandUser;

    @Mod.Metadata(Reference.ID)
    public static ModMetadata metadata;

    @Mod.Instance(value = Reference.ID)
    public static QuestSheets INSTANCE;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        metadata = Metadata.init(metadata);
        configDir = new File(event.getModConfigurationDirectory(), Reference.ID);
        if (!configDir.exists()) configDir.mkdir();
    }

    @Mod.EventHandler
    @Optional.Method(modid = "HardcoreQuesting")
    public void serverStart(FMLServerStartingEvent event)
    {
        event.registerServerCommand(ParentCommand.instance);
    }

    @NetworkCheckHandler
    public boolean networkCheck(Map<String, String> map, Side side)
    {
        return true;
    }

    public static EntityPlayer getPlayer()
    {
        return commandUser;
    }

    public static void setPlayer(EntityPlayer player)
    {
        commandUser = player;
    }
}
