/*
 * This file is part of MyPet
 *
 * Copyright © 2011-2020 Keyle
 * MyPet is licensed under the GNU Lesser General Public License.
 *
 * MyPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MyPet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.Keyle.MyPet.compat.v1_17_R1.entity.types;

import com.mojang.datafixers.util.Pair;
import de.Keyle.MyPet.MyPetApi;
import de.Keyle.MyPet.api.Util;
import de.Keyle.MyPet.api.entity.EntitySize;
import de.Keyle.MyPet.api.entity.EquipmentSlot;
import de.Keyle.MyPet.api.entity.MyPet;
import de.Keyle.MyPet.api.entity.types.MyGiant;
import de.Keyle.MyPet.compat.v1_17_R1.entity.EntityMyPet;
import de.Keyle.MyPet.compat.v1_17_R1.entity.ai.attack.MeleeAttack;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.EnumHand;
import net.minecraft.world.EnumInteractionResult;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.item.EntityItem;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.World;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import static de.Keyle.MyPet.compat.v1_17_R1.CompatManager.ENTITY_LIVING_broadcastItemBreak;

@EntitySize(width = 6.0f, height = 10.440001F)
public class EntityMyGiant extends EntityMyPet {

	public EntityMyGiant(World world, MyPet myPet) {
		super(world, myPet);
	}

	@Override
	protected String getDeathSound() {
		return "entity.zombie.death";
	}

	@Override
	protected String getHurtSound() {
		return "entity.zombie.hurt";
	}

	@Override
	protected String getLivingSound() {
		return "entity.zombie.ambient";
	}

	@Override
	public void playPetStepSound() {
		makeSound("entity.zombie.step", 0.15F, 1.0F);
	}

	@Override
	public void setPathfinder() {
		super.setPathfinder();
		if (myPet.getDamage() > 0) {
			petPathfinderSelector.replaceGoal("MeleeAttack", new MeleeAttack(this, 0.1F, 8, 20));
		}
	}

	@Override
	public EnumInteractionResult handlePlayerInteraction(EntityHuman entityhuman, EnumHand enumhand, ItemStack itemStack) {
		if (super.handlePlayerInteraction(entityhuman, enumhand, itemStack).a()) {
			return EnumInteractionResult.b;
		}

		if (getOwner().equals(entityhuman) && itemStack != null) {
			if (itemStack.getItem() == Items.pq && getOwner().getPlayer().isSneaking() && canEquip()) {
				boolean hadEquipment = false;
				for (EquipmentSlot slot : EquipmentSlot.values()) {
					ItemStack itemInSlot = CraftItemStack.asNMSCopy(getMyPet().getEquipment(slot));
					if (itemInSlot != null && itemInSlot.getItem() != Items.a) {
						EntityItem entityitem = new EntityItem(this.t, this.locX(), this.locY() + 1, this.locZ(), itemInSlot);
						entityitem.ap = 10;
						entityitem.setMot(entityitem.getMot().add(0, this.Q.nextFloat() * 0.05F, 0));
						this.t.addEntity(entityitem);
						getMyPet().setEquipment(slot, null);
						hadEquipment = true;
					}
				}
				if (hadEquipment) {
					if (itemStack != ItemStack.b && !entityhuman.getAbilities().d) {
						try {
							itemStack.damage(1, entityhuman, (entityhuman1) -> entityhuman1.broadcastItemBreak(enumhand));
						} catch (Error e) {
							// TODO REMOVE
							itemStack.damage(1, entityhuman, (entityhuman1) -> {
								try {
									ENTITY_LIVING_broadcastItemBreak.invoke(entityhuman1, enumhand);
								} catch (IllegalAccessException | InvocationTargetException ex) {
									ex.printStackTrace();
								}
							});
						}
					}
				}
				return EnumInteractionResult.b;
			} else if (MyPetApi.getPlatformHelper().isEquipment(CraftItemStack.asBukkitCopy(itemStack)) && getOwner().getPlayer().isSneaking() && canEquip()) {
				EquipmentSlot slot = EquipmentSlot.getSlotById(EntityInsentient.getEquipmentSlotForItem(itemStack).getSlotFlag());
				ItemStack itemInSlot = CraftItemStack.asNMSCopy(getMyPet().getEquipment(slot));
				if (itemInSlot != null && itemInSlot.getItem() != Items.a && itemInSlot != ItemStack.b && !entityhuman.getAbilities().d) {
					EntityItem entityitem = new EntityItem(this.t, this.locX(), this.locY() + 1, this.locZ(), itemInSlot);
					entityitem.ap = 10;
					entityitem.setMot(entityitem.getMot().add(0, this.Q.nextFloat() * 0.05F, 0));
					this.t.addEntity(entityitem);
				}
				getMyPet().setEquipment(slot, CraftItemStack.asBukkitCopy(itemStack));
				if (itemStack != ItemStack.b && !entityhuman.getAbilities().d) {
					itemStack.subtract(1);
					if (itemStack.getCount() <= 0) {
						entityhuman.getInventory().setItem(entityhuman.getInventory().k, ItemStack.b);
					}
				}
				return EnumInteractionResult.b;
			}
		}
		return EnumInteractionResult.d;
	}

	@Override
	public void updateVisuals() {
		Bukkit.getScheduler().runTaskLater(MyPetApi.getPlugin(), () -> {
			if (getMyPet().getStatus() == MyPet.PetState.Here) {
				for (EquipmentSlot slot : EquipmentSlot.values()) {
					setPetEquipment(slot, CraftItemStack.asNMSCopy(getMyPet().getEquipment(slot)));
				}
			}
		}, 5L);
	}

	public void setPetEquipment(EquipmentSlot slot, ItemStack itemStack) {
		((WorldServer) this.t).getChunkProvider().broadcastIncludingSelf(this, new PacketPlayOutEntityEquipment(getId(), Arrays.asList(new Pair<>(EnumItemSlot.values()[slot.get19Slot()], itemStack))));
	}

	@Override
	public ItemStack getEquipment(EnumItemSlot vanillaSlot) {
		if (Util.findClassInStackTrace(Thread.currentThread().getStackTrace(), "net.minecraft.server.level.EntityTrackerEntry", 2)) {
			EquipmentSlot slot = EquipmentSlot.getSlotById(vanillaSlot.getSlotFlag());
			if (getMyPet().getEquipment(slot) != null) {
				return CraftItemStack.asNMSCopy(getMyPet().getEquipment(slot));
			}
		}
		return super.getEquipment(vanillaSlot);
	}

	@Override
	public MyGiant getMyPet() {
		return (MyGiant) myPet;
	}
}