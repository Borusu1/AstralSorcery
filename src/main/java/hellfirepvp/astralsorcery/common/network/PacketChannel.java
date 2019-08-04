/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2019
 *
 * All rights reserved.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.network;

import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.client.ClientProxy;
import hellfirepvp.astralsorcery.common.network.base.ASPacket;
import hellfirepvp.astralsorcery.common.network.channel.BufferedReplyChannel;
import hellfirepvp.astralsorcery.common.network.channel.SimpleSendChannel;
import hellfirepvp.astralsorcery.common.network.packet.client.*;
import hellfirepvp.astralsorcery.common.network.packet.server.*;
import hellfirepvp.astralsorcery.common.util.reflection.ReflectionHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.function.Supplier;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: PacketChannel
 * Created by HellFirePvP
 * Date: 21.04.2019 / 19:34
 */
public class PacketChannel {

    private static int packetIndex = 0;
    private static final String NET_COMM_VERSION = "0"; //AS network version

    public static final SimpleSendChannel CHANNEL = new BufferedReplyChannel(
            ReflectionHelper.createInstance(
                    new ResourceLocation(AstralSorcery.MODID, "net_channel"),
                    () -> NET_COMM_VERSION,
                    NET_COMM_VERSION::equals,
                    NET_COMM_VERSION::equals));

    public static void registerPackets() {
        // DEDICATED_SERVER -> CLIENT
        registerMessage(PktAttunementAltarState::new);
        registerMessage(PktCraftingTableFix::new);
        registerMessage(PktFinalizeLogin::new);
        registerMessage(PktPlayLiquidFountain::new);
        registerMessage(PktOreScan::new);
        registerMessage(PktPlayEffect::new);
        registerMessage(PktPlayLiquidInteraction::new);
        registerMessage(PktProgressionUpdate::new);
        registerMessage(PktShootEntity::new);
        registerMessage(PktSyncCharge::new);
        registerMessage(PktSyncData::new);
        registerMessage(PktSyncKnowledge::new);
        registerMessage(PktSyncPerkActivity::new);
        registerMessage(PktSyncStepAssist::new);
        registerMessage(PktUpdateGateways::new);
        registerMessage(PktOpenGui::new);

        // CLIENT -> DEDICATED_SERVER
        registerMessage(PktAttuneConstellation::new);
        registerMessage(PktBurnParchment::new);
        registerMessage(PktClearBlockStorageStack::new);
        registerMessage(PktDiscoverConstellation::new);
        registerMessage(PktElytraCapeState::new);
        registerMessage(PktEngraveGlass::new);
        registerMessage(PktPerkGemModification::new);
        registerMessage(PktPlayerStatus::new);
        registerMessage(PktRemoveKnowledgeFragment::new);
        registerMessage(PktRequestPerkSealAction::new);
        registerMessage(PktRequestSeed::new);
        registerMessage(PktRequestSextantTarget::new);
        registerMessage(PktRequestTeleport::new);
        registerMessage(PktRotateTelescope::new);
        registerMessage(PktSetSextantTarget::new);
        registerMessage(PktUnlockPerk::new);
    }

    private static <T extends ASPacket<T>> void registerMessage(Supplier<T> pktSupplier) {
        T packet = pktSupplier.get();
        CHANNEL.messageBuilder((Class<T>) packet.getClass(), packetIndex++)
                .encoder(packet.encoder())
                .decoder(packet.decoder())
                .consumer(packet.handler())
                .add();
    }

    public static PacketDistributor.TargetPoint pointFromPos(World world, Vec3i pos, double range) {
        return new PacketDistributor.TargetPoint(pos.getX(), pos.getY(), pos.getZ(), range, world.getDimension().getType());
    }

    @OnlyIn(Dist.CLIENT)
    public static boolean canBeSentToServer() {
        return ClientProxy.connected;
    }

}
