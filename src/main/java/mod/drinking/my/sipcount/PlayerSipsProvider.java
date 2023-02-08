package mod.drinking.my.sipcount;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerSipsProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<PlayerSips> PLAYER_SIPS = CapabilityManager.get(new CapabilityToken<>() {});

    private PlayerSips sips = null;
    private final LazyOptional<PlayerSips> optional = LazyOptional.of(this::createPlayerSips);

    private PlayerSips createPlayerSips() {
        if(this.sips == null){
            this.sips = new PlayerSips();
        }
        return this.sips;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == PLAYER_SIPS){
            return optional.cast();
        }
        return LazyOptional.empty();
    }
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPlayerSips().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerSips().loadNBTData(nbt);
    }
}
