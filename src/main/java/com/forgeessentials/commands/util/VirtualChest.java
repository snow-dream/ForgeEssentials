package com.forgeessentials.commands.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import com.forgeessentials.commands.item.CommandVirtualchest;

public class VirtualChest extends InventoryBasic
{

    public static final String VIRTUALCHEST_TAG = "VirtualChestItems";

    private EntityPlayerMP owner;

    public VirtualChest(EntityPlayerMP player)
    {
        super(CommandVirtualchest.name, false, CommandVirtualchest.size);
        owner = player;
    }

    @Override
    public void openInventory()
    {
        loadInventoryFromNBT(owner.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getTagList(VIRTUALCHEST_TAG, 10));
        super.openInventory();
    }

    @Override
    public void closeInventory()
    {
        NBTTagCompound temp = owner.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
        owner.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, temp);
        temp.setTag(VIRTUALCHEST_TAG, saveInventoryToNBT());
        super.closeInventory();
    }

    @Override
    public void markDirty()
    {
        super.markDirty();
        NBTTagCompound temp = owner.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
        temp.setTag(VIRTUALCHEST_TAG, saveInventoryToNBT());
        owner.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, temp);
    }

    public void loadInventoryFromNBT(NBTTagList tag)
    {
        for (int slotIndex = 0; slotIndex < getSizeInventory(); ++slotIndex)
            setInventorySlotContents(slotIndex, (ItemStack) null);
        for (int tagIndex = 0; tagIndex < tag.tagCount(); ++tagIndex)
        {
            NBTTagCompound tagSlot = tag.getCompoundTagAt(tagIndex);
            int var4 = tagSlot.getByte("Slot") & 255;
            if (var4 >= 0 && var4 < getSizeInventory())
                setInventorySlotContents(var4, ItemStack.loadItemStackFromNBT(tagSlot));
        }
    }

    public NBTTagList saveInventoryToNBT()
    {
        NBTTagList var1 = new NBTTagList();

        for (int var2 = 0; var2 < getSizeInventory(); ++var2)
        {
            ItemStack var3 = getStackInSlot(var2);

            if (var3 != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) var2);
                var3.writeToNBT(var4);
                var1.appendTag(var4);
            }
        }

        return var1;
    }

}
