package tomato.backend.data.items;

import java.util.Map;
import java.util.HashMap;

public enum SlotType {
    NO_SLOT(-1, "No Slot"),
    SWORD(1, "Sword"),
    DAGGER(2, "Dagger"),
    BOW(3, "Bow"),
    TOME(4, "Tome"),
    SHIELD(5, "Shield"),
    LEATHER(6, "Leather"),
    HEAVY(7, "Heavy"),
    WAND(8, "Wand"),
    RING(9, "Ring"),
    CONSUMABLE(10, "Consumable"),
    SPELL(11, "Spell"),
    SEAL(12, "Seal"),
    CLOAK(13, "Cloak"),
    ROBE(14, "Robe"),
    QUIVER(15, "Quiver"),
    HELM(16, "Helm"),
    STAFF(17, "Staff"),
    POISON(18, "Poison"),
    SKULL(19, "Skull"),
    TRAP(20, "Trap"),
    ORB(21, "Orb"),
    PRISM(22, "Prism"),
    SCEPTER(23, "Scepter"),
    KATANA(24, "Katana"),
    STAR(25, "Star"),
    WAKIZASHI(27, "Wakizashi"),
    LUTE(28, "Lute"),
    MACE(29, "Mace"),
    SHEATH(30, "Sheath"),
    SIGIL(31, "Sigil");

    private static final Map<Integer, SlotType> slots = new HashMap<>();

    final int type;
    final String name;
    SlotType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    static {
        for (SlotType slot : SlotType.values()) {
            slots.put(slot.type, slot);
        }
    }

    public static SlotType from(int slotType) {
        return slots.get(slotType);
    }

    public int getValue() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public boolean isWeapon() {
        switch (this) {
            case SWORD:
            case DAGGER:
            case BOW:
            case WAND:
            case STAFF:
            case KATANA:
                return true;
            default:
                return false;
        }
    }
}
