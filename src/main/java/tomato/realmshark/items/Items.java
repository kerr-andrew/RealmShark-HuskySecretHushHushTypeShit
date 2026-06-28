package tomato.realmshark.items;

import org.xml.sax.SAXException;
import util.StringXML;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Items {
    private static final String XML_PATH = "assets/xml/equip.xml";
    private static final HashMap<Integer, Item> EQUIPMENT = new HashMap<>();

    /*
      Load Enchant XML data to get names from file.
     */
    static {
        loadItems();
    }

    private static void loadItems() {
        try {
            FileInputStream file = new FileInputStream(Items.XML_PATH);
            String result = new BufferedReader(new InputStreamReader(file)).lines().collect(Collectors.joining("\n"));
            StringXML base = StringXML.getParsedXML(result);
            for (StringXML xml : base) {
                if (!Objects.equals(xml.name, "Object")) continue;
                Item.Builder builder = new Item.Builder();
                for (StringXML info : xml) {
                    if (info.name == null) continue;
                    parseBasicItemData(info, builder);
                    parseWeaponItemData(info, builder);
                }
                builder.patchWeaponData();
                Item item = builder.build();
                EQUIPMENT.put(item.id, item);
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }
    }

    private static void parseBasicItemData(StringXML info, Item.Builder item) {
        switch (info.name) {
            case "id": item.setName(info.value); break;
            case "type": item.setId(Integer.decode(info.value)); break;
            case "SlotType": item.setSlotType(SlotType.from(Integer.decode(info.children.get(0).value))); break;
            case "Tier": item.setTier(Integer.decode(info.children.get(0).value)); break;
            case "Description": item.setDescription(info.children.get(0).value); break;
            case "feedPower": item.setFeedPower(Integer.decode(info.children.get(0).value)); break;
            case "Labels": item.setLabels(info.children.get(0).value.split(",")); break;
            case "DisplayId": item.setDisplayName(info.children.get(0).value); break;
        }
    }

    private static void parseWeaponItemData(StringXML info, Item.Builder item) {
        switch (info.name) {
            case "RateOfFire": item.setRof(Float.parseFloat(info.children.get(0).value)); break;
            case "NumProjectiles": item.setNumProj(Integer.parseInt(info.children.get(0).value)); break;
            case "Subattack": item.addBullet(parseBullet(info)); break;
            case "Projectile": Projectile p = parseProjectile(info); item.addProjectile(p); break;
            case "Texture": {
                for (StringXML texInfo : info.children) {
                    if (texInfo.name == null) continue;
                    switch (texInfo.name) {
                        case "Index": {
                            String value = texInfo.children.get(0).value;
                            item.setImgIndex(Integer.parseInt(texInfo.children.get(0).value, value.startsWith("0x") ? 16 : 10));
                            break;
                        }
                        case "File": item.setImgFile(texInfo.children.get(0).value); break;
                    }
                }
            }
        }
    }

    private static Bullet parseBullet(StringXML info) {
        Bullet bullet = new Bullet();
        for (StringXML bulletInfo : info.children) {
            if (bulletInfo.name == null) continue;
            switch (bulletInfo.name) {
                case "projectileId": bullet.id = Integer.parseInt(bulletInfo.value); break;
                case "RateOfFire": bullet.rof = Float.parseFloat(bulletInfo.children.get(0).value); break;
                case "NumProjectiles": bullet.numProj = Integer.parseInt(bulletInfo.children.get(0).value); break;
            }
        }
        return bullet;
    }

    private static Projectile parseProjectile(StringXML info) {
        Projectile.Builder projectile = new Projectile.Builder();
        for (StringXML projInfo : info.children) {
            if (projInfo.name == null) continue;
            switch(projInfo.name) {
                case "id": projectile.setId(Integer.parseInt(projInfo.value)); break;
                case "MinDamage": projectile.setMin(Integer.parseInt(projInfo.children.get(0).value.replaceAll("\t", ""))); break;
                case "MaxDamage": projectile.setMax(Integer.parseInt(projInfo.children.get(0).value)); break;
                case "ArmorPiercing": projectile.setPiercing(true); break;
                case "Damage": {
                    int damage = Integer.parseInt(projInfo.children.get(0).value);
                    projectile.setMin(damage);
                    projectile.setMax(damage);
                    break;
                }
            }
        }
        return projectile.build();
    }

    public static List<Item> getParseItems() {
        return EQUIPMENT.values()
                .stream()
                .filter(Item::isParseItem)
                .collect(Collectors.toList());
    }

    public static Item get(int id) {
        return EQUIPMENT.get(id);
    }

    public static boolean has(int id) {
        return EQUIPMENT.containsKey(id);
    }

    public static String name(int id) {
        return EQUIPMENT.get(id).name();
    }
}
