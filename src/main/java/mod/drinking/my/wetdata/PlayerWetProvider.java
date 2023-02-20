package mod.drinking.my.wetdata;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerWetProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<PlayerWet> IS_WET = CapabilityManager.get(new CapabilityToken<>() {});

    private PlayerWet wet = null;
    private final LazyOptional<PlayerWet> optional = LazyOptional.of(this::createPlayerWet);

    private PlayerWet createPlayerWet() {
        if(this.wet == null){
            this.wet = new PlayerWet();
        }
        return this.wet;
    }

    @Override
    public @NotNull <A> LazyOptional<A> getCapability(@NotNull Capability<A> cap, @Nullable Direction side) {
        if(cap == IS_WET){
            return optional.cast();
        }
        return LazyOptional.empty();
    }
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPlayerWet().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerWet().loadNBTData(nbt);
    }
}
