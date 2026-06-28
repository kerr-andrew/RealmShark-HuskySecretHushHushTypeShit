package tomato.backend.data.entities;

import assets.IdToAsset;
import packets.data.StatData;
import packets.data.WorldPosData;
import packets.data.enums.StatType;
import tomato.backend.data.Entity;
import tomato.backend.data.items.Item;
import tomato.backend.data.items.Items;
import tomato.realmshark.ParseEnchants;
import tomato.realmshark.enums.LootBags;

import java.util.Arrays;

public class Bag {
    public final LootBags bagType;
    public final Entity bagEntity;

    public Bag(Entity bag) {
        this.bagEntity = bag;
        bagType = LootBags.lootBag(bag.objectType);
        if (bagType == null) {
            throw new IllegalArgumentException(bag.objectType + " is not a valid bag type");
        }
    }

    public Item.WithEnchants at(int slot) {
        StatData data = bagEntity.stat.get(StatType.INVENTORY_0_STAT.get() + slot);
        if (data == null || data.statValue < 1) return null;

        Item item = Items.get(data.statValue);
        return item.withEnchants(parseEnchants(bagEntity, slot));
    }

    private static String[] parseEnchants(Entity bagEntity, int slot) {
        StatData enchantData = bagEntity.stat.get(StatType.UNIQUE_DATA_STRING);
        String[] enchants = enchantData != null && enchantData.stringStatValue != null
                ? enchantData.stringStatValue.split(",")
                : new String[] { };

        String enchant = slot < enchants.length &&
                !enchants[slot].isEmpty() &&
                !enchants[slot].equals("AAIE_f_9__3__f8=")
                ? enchants[slot]
                : "";

        return parseEnchants(enchant);
    }

    private static String[] parseEnchants(String enchantData) {
        String parsedEnchants = ParseEnchants.parse(enchantData);
        return Arrays.stream(parsedEnchants.split("\n")).filter(s -> !s.isEmpty()).toArray(String[]::new);
    }

    public String getName() {
        return IdToAsset.objectName(bagType.id);
    }

    public WorldPosData pos() {
        return this.bagEntity.pos;
    }
}
