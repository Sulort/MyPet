/*
 * This file is part of MyPet
 *
 * Copyright © 2011-2019 Keyle
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

package de.Keyle.MyPet.compat.v1_14_R1.entity.types;

import de.Keyle.MyPet.api.Configuration;
import de.Keyle.MyPet.api.entity.EntitySize;
import de.Keyle.MyPet.api.entity.MyPet;
import de.Keyle.MyPet.api.entity.types.MyWither;
import de.Keyle.MyPet.compat.v1_14_R1.entity.EntityMyPet;
import net.minecraft.server.v1_14_R1.*;

@EntitySize(width = 1.9F, height = 3.5F)
public class EntityMyWither extends EntityMyPet {

    private static final DataWatcherObject<Integer> targetWatcher = DataWatcher.a(EntityMyWither.class, DataWatcherRegistry.b);
    private static final DataWatcherObject<Integer> watcher_1 = DataWatcher.a(EntityMyWither.class, DataWatcherRegistry.b);
    private static final DataWatcherObject<Integer> watcher_2 = DataWatcher.a(EntityMyWither.class, DataWatcherRegistry.b);
    private static final DataWatcherObject<Integer> invulnerabilityWatcher = DataWatcher.a(EntityMyWither.class, DataWatcherRegistry.b);

    public EntityMyWither(World world, MyPet myPet) {
        super(EntityTypes.WITHER, world, myPet);
    }

    @Override
    protected String getDeathSound() {
        return "entity.wither.death";
    }

    @Override
    protected String getHurtSound() {
        return "entity.wither.hurt";
    }

    protected String getLivingSound() {
        return "entity.wither.ambient";
    }

    protected void initDatawatcher() {
        super.initDatawatcher();
        getDataWatcher().register(targetWatcher, 0);          // target entityID
        getDataWatcher().register(watcher_1, 0);              // N/A
        getDataWatcher().register(watcher_2, 0);              // N/A
        getDataWatcher().register(invulnerabilityWatcher, 0); // invulnerability (blue, size)
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (Configuration.MyPet.Wither.CAN_GLIDE) {
            if (!this.onGround && this.getMot().y < 0.0D) {
                this.setMot(getMot().d(1, 0.6D, 1));
            }
        }
    }

    @Override
    public void updateVisuals() {
        getDataWatcher().set(invulnerabilityWatcher, getMyPet().isBaby() ? 600 : 0);
    }

    /**
     * -> disable falldamage
     */
    public void b(float f, float f1) {
        if (!Configuration.MyPet.Wither.CAN_GLIDE) {
            super.b(f, f1);
        }
    }

    public MyWither getMyPet() {
        return (MyWither) myPet;
    }
}