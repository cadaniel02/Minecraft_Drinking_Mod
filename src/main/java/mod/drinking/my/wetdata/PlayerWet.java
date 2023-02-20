package mod.drinking.my.wetdata;
import net.minecraft.nbt.CompoundTag;

public class PlayerWet {
    private boolean wet;

    public boolean is_wet(){
        return this.wet;
    }

    public void setWet(boolean wet){
        this.wet = wet;
    }

    public void saveNBTData(CompoundTag nbt){
        nbt.putBoolean("wet", wet);
    }

    public void loadNBTData(CompoundTag nbt){
        wet = nbt.getBoolean("wet");
    }
}
