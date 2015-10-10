package questsheets;

import cpw.mods.fml.common.Mod;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import questsheets.reference.Reference;

@Mod(name = Reference.NAME, modid = Reference.ID, version = Reference.VERSION_FULL, dependencies = "required-after:HardcoreQuesting")
public class QuestSheets
{
    public static EntityPlayer getPlayer()
    {
        return new EntityClientPlayerMP(null, null, null, null, null);
    }
}
