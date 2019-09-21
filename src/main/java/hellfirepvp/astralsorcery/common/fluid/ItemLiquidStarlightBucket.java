/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2019
 *
 * All rights reserved.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.fluid;

import hellfirepvp.astralsorcery.common.registry.RegistryItems;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.function.Supplier;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: ItemLiquidStarlightBucket
 * Created by HellFirePvP
 * Date: 20.09.2019 / 22:10
 */
public class ItemLiquidStarlightBucket extends BucketItem {

    public ItemLiquidStarlightBucket(Supplier<? extends Fluid> fluidSupplier) {
        super(fluidSupplier, new Item.Properties()
                .containerItem(Items.BUCKET)
                .maxStackSize(1)
                .group(RegistryItems.ITEM_GROUP_AS));
    }
}
