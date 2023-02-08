package mod.drinking.my.sipcount;
import mod.drinking.my.client.ClientSipData;
import net.minecraft.nbt.CompoundTag;
public class PlayerSips {
    private int sips;
    private int totalSips;
    private final int MIN_SIPS = 0;

    public int get_sips(){
        return sips;
    }

    public int get_totalSips() {return totalSips; }

    public void add_sips(int add){
        sips = Math.min(sips + add, MIN_SIPS);
        totalSips = Math.min(totalSips + add, MIN_SIPS);
        ClientSipData.set(sips);
    }

    public void reset_sips(){
        sips = MIN_SIPS;
    }

    public void reset_total_sips(){totalSips = MIN_SIPS; }

    public void copyFrom(PlayerSips source){
        this.sips = source.sips;
    }

    public void saveNBTData(CompoundTag nbt){
        nbt.putInt("sips", sips);
        nbt.putInt("totalSips", totalSips);
    }

    public void loadNBTData(CompoundTag nbt){
        sips = nbt.getInt("sips");
        totalSips = nbt.getInt("totalSips");
    }
}
