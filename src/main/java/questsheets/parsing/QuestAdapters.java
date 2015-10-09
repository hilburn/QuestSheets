package questsheets.parsing;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.io.IOException;

public class QuestAdapters
{
    public static final TypeAdapter<ItemStack> ITEM_STACK_TYPE_ADAPTER = new TypeAdapter<ItemStack>()
    {
        private static final String ID = "id";
        private static final String DAMAGE = "damage";
        private static final String STACK_SIZE = "amount";
        private static final String NBT = "nbt";

        @Override
        public void write(JsonWriter out, ItemStack value) throws IOException
        {
            if (value == null || value.getItem() == null)
            {
                out.nullValue();
                return;
            }
            String id = GameData.getItemRegistry().getNameForObject(value.getItem());
            out.beginObject();
            out.name(ID).value(id);
            if (value.getItemDamage() != 0)
            {
                out.name(DAMAGE).value(value.getItemDamage());
            }
            if (value.stackSize != 1)
            {
                out.name(STACK_SIZE).value(value.stackSize);
            }
            if (value.hasTagCompound() && !value.getTagCompound().hasNoTags())
            {
                NBT_TAG_COMPOUND_TYPE_ADAPTER.write(out.name(NBT), value.getTagCompound());
            }
            out.endObject();
        }

        @Override
        public ItemStack read(JsonReader in) throws IOException
        {
            if (in.peek() == JsonToken.NULL)
            {
                return null;
            }
            String id = "";
            int damage = 0, size = 1;
            NBTTagCompound tag = null;
            in.beginObject();
            while (in.peek() != JsonToken.NULL)
            {
                String name = in.nextName();
                if (name.equalsIgnoreCase(ID))
                {
                    id = in.nextString();
                } else if (name.equalsIgnoreCase(DAMAGE))
                {
                    damage = in.nextInt();
                } else if (name.equalsIgnoreCase(STACK_SIZE))
                {
                    size = in.nextInt();
                } else if (name.equalsIgnoreCase(NBT))
                {
                    tag = NBT_TAG_COMPOUND_TYPE_ADAPTER.read(in);
                }
            }
            String modid = "minecraft", name = "";
            int colon = id.indexOf(':');

            if (colon == -1) name = id;
            else
            {
                modid = id.substring(0, colon);
                name = id.substring(colon + 1);
            }
            Item item = getItemFromID(modid, name);
            if (item == null)
            {
                return null;
            }
            ItemStack result = new ItemStack(item, size, damage);
            result.setTagCompound(tag);
            return result;
        }

        private Item getItemFromID(String mod, String name)
        {
            Block block;
            Item item;
            if ((item = GameRegistry.findItem(mod, name)) != null) return item;
            else if ((block = GameRegistry.findBlock(mod, name)) != null) return Item.getItemFromBlock(block);
            else return null;
        }
    };

    public static final TypeAdapter<NBTTagCompound> NBT_TAG_COMPOUND_TYPE_ADAPTER = new TypeAdapter<NBTTagCompound>()
    {
        @Override
        public void write(JsonWriter out, NBTTagCompound value) throws IOException
        {

        }

        @Override
        public NBTTagCompound read(JsonReader in) throws IOException
        {
            return null;
        }
    };
}
