/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2019
 *
 * All rights reserved.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.util.data;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.nio.charset.Charset;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: ByteBufUtils
 * Created by HellFirePvP
 * Date: 07.05.2016 / 01:13
 */
public class ByteBufUtils {

    @Nullable
    public static <T> T readOptional(PacketBuffer buf, Function<PacketBuffer, T> readFct) {
        if (buf.readBoolean()) {
            return readFct.apply(buf);
        }
        return null;
    }

    public static <T> void writeOptional(PacketBuffer buf, @Nullable T object, BiConsumer<PacketBuffer, T> applyFct) {
        buf.writeBoolean(object != null);
        if (object != null) {
            applyFct.accept(buf, object);
        }
    }

    public static void writeUUID(PacketBuffer buf, UUID uuid) {
        buf.writeLong(uuid.getMostSignificantBits());
        buf.writeLong(uuid.getLeastSignificantBits());
    }

    public static UUID readUUID(PacketBuffer buf) {
        return new UUID(buf.readLong(), buf.readLong());
    }

    public static void writeString(PacketBuffer buf, String toWrite) {
        byte[] str = toWrite.getBytes(Charset.forName("UTF-8"));
        buf.writeInt(str.length);
        buf.writeBytes(str);
    }

    public static void writeString(ByteBuf buf, String toWrite) {
        byte[] str = toWrite.getBytes(Charset.forName("UTF-8"));
        buf.writeInt(str.length);
        buf.writeBytes(str);
    }

    public static String readString(PacketBuffer buf) {
        int length = buf.readInt();
        byte[] strBytes = new byte[length];
        buf.readBytes(strBytes, 0, length);
        return new String(strBytes, Charset.forName("UTF-8"));
    }

    public static String readString(ByteBuf buf) {
        int length = buf.readInt();
        byte[] strBytes = new byte[length];
        buf.readBytes(strBytes, 0, length);
        return new String(strBytes, Charset.forName("UTF-8"));
    }

    public static void writeResourceLocation(PacketBuffer buf, ResourceLocation key) {
        writeString(buf, key.toString());
    }

    public static ResourceLocation readResourceLocation(PacketBuffer buf) {
        return new ResourceLocation(readString(buf));
    }

    public static <T extends Enum<T>> void writeEnumValue(PacketBuffer buf, T value) {
        buf.writeInt(value.ordinal());
    }

    public static <T extends Enum<T>> T readEnumValue(PacketBuffer buf, Class<T> enumClazz) {
        if (!enumClazz.isEnum()) {
            throw new IllegalArgumentException("Passed class is not an enum!");
        }
        return enumClazz.getEnumConstants()[buf.readInt()];
    }

    public static void writePos(PacketBuffer buf, BlockPos pos) {
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
    }

    public static BlockPos readPos(PacketBuffer buf) {
        int x = buf.readInt();
        int y = buf.readInt();
        int z = buf.readInt();
        return new BlockPos(x, y, z);
    }

    public static void writeVector(PacketBuffer buf, Vector3 vec) {
        buf.writeDouble(vec.getX());
        buf.writeDouble(vec.getY());
        buf.writeDouble(vec.getZ());
    }

    public static Vector3 readVector(PacketBuffer buf) {
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();
        return new Vector3(x, y, z);
    }

    public static void writeItemStack(PacketBuffer byteBuf, @Nonnull ItemStack stack) {
        boolean defined = !stack.isEmpty();
        byteBuf.writeBoolean(defined);
        if(defined) {
            NBTTagCompound tag = new NBTTagCompound();
            stack.write(tag);
            writeNBTTag(byteBuf, tag);
        }
    }

    @Nonnull
    public static ItemStack readItemStack(PacketBuffer byteBuf) {
        boolean defined = byteBuf.readBoolean();
        if(defined) {
            return ItemStack.read(readNBTTag(byteBuf));
        } else {
            return ItemStack.EMPTY;
        }
    }

    public static void writeFluidStack(ByteBuf byteBuf, @Nullable FluidStack stack) {
        boolean defined = stack != null;
        byteBuf.writeBoolean(defined);
        if(defined) {
            NBTTagCompound tag = new NBTTagCompound();
            stack.writeToNBT(tag);
            writeNBTTag(byteBuf, tag);
        }
    }

    public static void writeFluidStack(PacketBuffer pktBuf, @Nullable FluidStack stack) {
        boolean defined = stack != null;
        pktBuf.writeBoolean(defined);
        if(defined) {
            NBTTagCompound tag = new NBTTagCompound();
            stack.writeToNBT(tag);
            writeNBTTag(pktBuf, tag);
        }
    }

    @Nullable
    public static FluidStack readFluidStack(ByteBuf byteBuf) {
        boolean defined = byteBuf.readBoolean();
        if(defined) {
            return FluidStack.loadFluidStackFromNBT(readNBTTag(byteBuf));
        } else {
            return null;
        }
    }

    @Nullable
    public static FluidStack readFluidStack(PacketBuffer pktBuf) {
        boolean defined = pktBuf.readBoolean();
        if(defined) {
            return FluidStack.loadFluidStackFromNBT(readNBTTag(pktBuf));
        } else {
            return null;
        }
    }

    public static void writeNBTTag(ByteBuf byteBuf, @Nonnull NBTTagCompound tag) {
        try (DataOutputStream dos = new DataOutputStream(new ByteBufOutputStream(byteBuf))) {
            CompressedStreamTools.write(tag, dos);
        } catch (Exception exc) {}
    }

    @Nonnull
    public static NBTTagCompound readNBTTag(ByteBuf byteBuf) {
        try (DataInputStream dis = new DataInputStream(new ByteBufInputStream(byteBuf))) {
            return CompressedStreamTools.read(dis);
        } catch (Exception exc) {}
        throw new IllegalStateException("Could not load NBT Tag from incoming byte buffer!");
    }

}
