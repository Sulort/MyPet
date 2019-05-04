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

import de.Keyle.MyPet.api.entity.EntitySize;
import de.Keyle.MyPet.api.entity.MyPet;
import de.Keyle.MyPet.api.entity.types.MyWanderingTrader;
import de.Keyle.MyPet.compat.v1_14_R1.entity.EntityMyPet;
import net.minecraft.server.v1_14_R1.*;

@EntitySize(width = 0.6F, height = 1.9F)
public class EntityMyWanderingTrader extends EntityMyPet {

    private static final DataWatcherObject<Boolean> AGE_WATCHER = DataWatcher.a(EntityMyWanderingTrader.class, DataWatcherRegistry.i);

    public EntityMyWanderingTrader(World world, MyPet myPet) {
        super(EntityTypes.WANDERING_TRADER, world, myPet);
    }

    protected String getDeathSound() {
        return "entity.villager.death";
    }

    protected String getHurtSound() {
        return "entity.villager.hurt";
    }

    protected String getLivingSound() {
        return "entity.villager.ambient";
    }

    protected void initDatawatcher() {
        super.initDatawatcher();
        getDataWatcher().register(AGE_WATCHER, false);
    }

    public MyWanderingTrader getMyPet() {
        return (MyWanderingTrader) myPet;
    }
}