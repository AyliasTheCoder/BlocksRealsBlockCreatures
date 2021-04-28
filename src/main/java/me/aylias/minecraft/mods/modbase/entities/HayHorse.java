package me.aylias.minecraft.mods.modbase.entities;

import me.aylias.minecraft.mods.modbase.events.EntityRegisterEvents;
import net.minecraft.block.SoundType;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.passive.horse.CoatColors;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.HorseArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.*;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.UUID;

public class HayHorse extends AbstractHorseEntity {

    private static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("556E1665-8B10-40C8-8F9D-CF9B1667F295");

    public HayHorse(EntityType<HayHorse> type, World worldIn) {
        super(type, worldIn);

    }

    protected void func_230273_eI_() {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue((double)this.getModifiedMaxHealth());
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.getModifiedMovementSpeed());
        this.getAttribute(Attributes.HORSE_JUMP_STRENGTH).setBaseValue(this.getModifiedJumpStrength());
    }



    protected void registerData() {
        super.registerData();
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        if (!this.horseChest.getStackInSlot(1).isEmpty()) {
            compound.put("ArmorItem", this.horseChest.getStackInSlot(1).write(new CompoundNBT()));
        }

    }

    public ItemStack func_213803_dV() {
        return this.getItemStackFromSlot(EquipmentSlotType.CHEST);
    }

    private void func_213805_k(ItemStack p_213805_1_) {
        this.setItemStackToSlot(EquipmentSlotType.CHEST, p_213805_1_);
        this.setDropChance(EquipmentSlotType.CHEST, 0.0F);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if (compound.contains("ArmorItem", 10)) {
            ItemStack itemstack = ItemStack.read(compound.getCompound("ArmorItem"));
            if (!itemstack.isEmpty() && this.isArmor(itemstack)) {
                this.horseChest.setInventorySlotContents(1, itemstack);
            }
        }

        this.func_230275_fc_();
    }

    protected void func_230275_fc_() {
        if (!this.world.isRemote) {
            super.func_230275_fc_();
            this.func_213804_l(this.horseChest.getStackInSlot(1));
            this.setDropChance(EquipmentSlotType.CHEST, 0.0F);
        }
    }

    private void func_213804_l(ItemStack p_213804_1_) {
        this.func_213805_k(p_213804_1_);
        if (!this.world.isRemote) {
            this.getAttribute(Attributes.ARMOR).removeModifier(ARMOR_MODIFIER_UUID);
            if (this.isArmor(p_213804_1_)) {
                int i = ((HorseArmorItem)p_213804_1_.getItem()).getArmorValue();
                if (i != 0) {
                    this.getAttribute(Attributes.ARMOR).applyPersistentModifier(new AttributeModifier(ARMOR_MODIFIER_UUID, "Horse armor bonus", (double)i, AttributeModifier.Operation.ADDITION));
                }
            }
        }

    }

    /**
     * Called by InventoryBasic.onInventoryChanged() on a array that is never filled.
     */
    public void onInventoryChanged(IInventory invBasic) {
        ItemStack itemstack = this.func_213803_dV();
        super.onInventoryChanged(invBasic);
        ItemStack itemstack1 = this.func_213803_dV();
        if (this.ticksExisted > 20 && this.isArmor(itemstack1) && itemstack != itemstack1) {
            this.playSound(SoundEvents.ENTITY_HORSE_ARMOR, 0.5F, 1.0F);
        }

    }

    protected void playGallopSound(SoundType p_190680_1_) {
        super.playGallopSound(p_190680_1_);
        if (this.rand.nextInt(10) == 0) {
            this.playSound(SoundEvents.ENTITY_HORSE_BREATHE, p_190680_1_.getVolume() * 0.6F, p_190680_1_.getPitch());
        }

        ItemStack stack = this.horseChest.getStackInSlot(1);
        if (isArmor(stack)) stack.onHorseArmorTick(world, this);
    }

    protected SoundEvent getAmbientSound() {
        super.getAmbientSound();
        return SoundEvents.ENTITY_HORSE_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        super.getDeathSound();
        return SoundEvents.ENTITY_HORSE_DEATH;
    }

    @Nullable
    protected SoundEvent func_230274_fe_() {
        return SoundEvents.ENTITY_HORSE_EAT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        super.getHurtSound(damageSourceIn);
        return SoundEvents.ENTITY_HORSE_HURT;
    }

    protected SoundEvent getAngrySound() {
        super.getAngrySound();
        return SoundEvents.ENTITY_HORSE_ANGRY;
    }

    public ActionResultType func_230254_b_(PlayerEntity p_230254_1_, Hand p_230254_2_) {
        ItemStack itemstack = p_230254_1_.getHeldItem(p_230254_2_);
        if (!this.isChild()) {
            if (this.isTame() && p_230254_1_.isSecondaryUseActive()) {
                this.openGUI(p_230254_1_);
                return ActionResultType.func_233537_a_(this.world.isRemote);
            }

            if (this.isBeingRidden()) {
                return super.func_241395_b_(p_230254_1_, itemstack);
            }
        }

        if (!itemstack.isEmpty()) {
            if (this.isBreedingItem(itemstack)) {
                return this.func_241395_b_(p_230254_1_, itemstack);
            }

            ActionResultType actionresulttype = itemstack.interactWithEntity(p_230254_1_, this, p_230254_2_);
            if (actionresulttype.isSuccessOrConsume()) {
                return actionresulttype;
            }

            if (!this.isTame()) {
                this.makeMad();
                return ActionResultType.func_233537_a_(this.world.isRemote);
            }

            boolean flag = !this.isChild() && !this.isHorseSaddled() && itemstack.getItem() == Items.SADDLE;
            if (this.isArmor(itemstack) || flag) {
                this.openGUI(p_230254_1_);
                return ActionResultType.func_233537_a_(this.world.isRemote);
            }
        }

        if (this.isChild()) {
            return super.func_241395_b_(p_230254_1_, itemstack);
        } else {
            this.mountTo(p_230254_1_);
            return ActionResultType.func_233537_a_(this.world.isRemote);
        }
    }

    /**
     * Returns true if the mob is currently able to mate with the specified mob.
     */
    public boolean canMateWith(AnimalEntity otherAnimal) {
        return false;
    }

    public AgeableEntity func_241840_a(ServerWorld p_241840_1_, AgeableEntity p_241840_2_) {
        HayHorse horseentity = EntityRegisterEvents.HAY_HORSE.create(p_241840_1_);

        this.setOffspringAttributes(p_241840_2_, horseentity);
        return horseentity;
    }

    public boolean func_230276_fq_() {
        return true;
    }

    public boolean isArmor(ItemStack stack) {
        return stack.getItem() instanceof HorseArmorItem;
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
