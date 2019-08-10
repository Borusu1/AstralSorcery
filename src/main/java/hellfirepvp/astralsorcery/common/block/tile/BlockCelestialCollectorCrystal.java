/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2019
 *
 * All rights reserved.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.block.tile;

import hellfirepvp.astralsorcery.common.item.block.ItemBlockCelestialCollectorCrystal;
import hellfirepvp.astralsorcery.common.item.block.ItemBlockCollectorCrystal;
import net.minecraft.item.BlockItem;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: BlockCelestialCollectorCrystal
 * Created by HellFirePvP
 * Date: 10.08.2019 / 23:04
 */
public class BlockCelestialCollectorCrystal extends BlockCollectorCrystal {

    public BlockCelestialCollectorCrystal() {
        super(CollectorCrystalType.CELESTIAL_CRYSTAL);
    }

    @Override
    public Class<? extends ItemBlockCollectorCrystal> getItemBlockClass() {
        return ItemBlockCelestialCollectorCrystal.class;
    }
}
