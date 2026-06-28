package tomato.realmshark.items;

import java.util.*;
import java.util.stream.Collectors;

public class Item {
    public final int id;

    public final String name;
    public final String displayName;
    public final SlotType slotType;
    public final int tier;
    public final String description;
    public final int feedPower;

    public final Map<Integer, Projectile> projectiles;
    public final List<Bullet> bullets;
    public final Set<String> labels;

    public final String imgFile;
    public final int imgIndex;
    public final float rof;
    public final int numProj;

    private Item(
            int id,
            String name,
            String displayName,
            SlotType slot,
            int tier,
            String description,
            int feedPower,
            Map<Integer, Projectile> projectiles,
            List<Bullet> bullets,
            Set<String> labels,
            String imgFile,
            int imgIndex,
            float rof,
            int numProj
    ) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.slotType = slot;
        this.tier = tier;
        this.description = description;
        this.feedPower = feedPower;
        this.projectiles = projectiles;
        this.bullets = bullets;
        this.imgFile = imgFile;
        this.imgIndex = imgIndex;
        this.rof = rof;
        this.labels = labels;
        this.numProj = numProj;
    }

    public String name() {
        if (displayName != null && !displayName.isEmpty()) {
            return displayName;
        } else {
            return name;
        }
    }

    public boolean isParseItem() {
        final boolean isNonConsumable = labels != null && !labels.contains("CONSUMABLE");
        final boolean isST_UT = labels != null && (labels.contains("ST") || labels.contains("UT"));
        // detecting tiered gear requires:
        //  - tier label
        //  - armor fix (shifted up)
        //  - not st/ut (because some ut/st get the T0 label for some reason)
        final boolean isTieredGear = labels != null &&
                (labels.contains("T" + tier) || (labels.contains("ARMOR") && labels.contains("T" + (tier+1))))
                && !isST_UT;

        return (isNonConsumable && (isST_UT || isTieredGear));
    }

    @SuppressWarnings({ "UnusedReturnValue", "unused" })
    public static Builder builder() {
        return new Builder();
    }

    @SuppressWarnings({ "UnusedReturnValue", "unused" })
    public static class Builder {
        public Builder() {}

        private int id;
        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        private String name = "";
        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        private String displayName;
        public Builder setDisplayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        private SlotType slotType = SlotType.NO_SLOT;
        public Builder setSlotType(SlotType slotType) {
            this.slotType = slotType;
            return this;
        }

        private int tier = 0;
        public Builder setTier(int tier) {
            this.tier = tier;
            return this;
        }

        private String description;
        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        private int feedPower = 0;
        public Builder setFeedPower(int feedPower) {
            this.feedPower = feedPower;
            return this;
        }

        private Map<Integer, Projectile> projectiles = new HashMap<>();
        public Builder addProjectile(Projectile projectile) {
            projectiles.put(projectile.id, projectile);
            return this;
        }

        public Builder setProjectiles(Projectile... projectiles) {
            this.projectiles = Arrays.stream(projectiles).collect(Collectors.toMap(
                    p -> p.id,
                    p -> p,
                    (existing, dupe) -> existing)
            );
            return this;
        }

        private List<Bullet> bullets = new ArrayList<>();
        public Builder addBullet(Bullet bullet) {
            bullets.add(bullet);
            return this;
        }

        public Builder setBullets(Bullet... bullets) {
            this.bullets = Arrays.asList(bullets);
            return this;
        }

        private Set<String> labels = new HashSet<>();
        public Builder addLabels(String... labels) {
            this.labels.addAll(Arrays.asList(labels));
            return this;
        }

        public Builder setLabels(String... labels) {
            this.labels = Arrays.stream(labels).collect(Collectors.toSet());
            return this;
        }

        private String imgFile;
        public Builder setImgFile(String imgFile) {
            this.imgFile = imgFile;
            return this;
        }

        private int imgIndex;
        public Builder setImgIndex(int imgIndex) {
            this.imgIndex = imgIndex;
            return this;
        }

        private float rof = -1;
        public Builder setRof(float rof) {
            this.rof = rof;
            return this;
        }

        private int numProj = 1;
        public Builder setNumProj(int numProj) {
            this.numProj = numProj;
            return this;
        }

        public Builder patchWeaponData() {
            if (this.rof != -1 && this.bullets.isEmpty()) {
                Bullet bullet = new Bullet();
                bullet.id = 0;
                bullet.numProj = Math.max(this.numProj, 1);
                bullet.rof = this.rof;
                this.bullets.add(bullet);
            } else if (this.rof != -1) {
                for (Bullet bullet : this.bullets) {
                    if (bullet.rof == -1) bullet.rof = this.rof;
                }
            } else if (this.bullets.isEmpty() && !this.projectiles.isEmpty()) {
                for (Projectile proj : this.projectiles.values()) {
                    Bullet bullet = new Bullet();
                    bullet.id = proj.id;
                    bullet.numProj = 1;
                    bullet.rof = 1f;
                    this.bullets.add(bullet);
                }
            }

            for (Bullet bullet : this.bullets) {
                Projectile proj = this.projectiles.get(bullet.id);
                if (proj == null) continue;
                bullet.min = proj.min;
                bullet.max = proj.max;
            }

            return this;
        }

        public Item build() {
            return new Item(
                    id,
                    name,
                    displayName,
                    slotType,
                    tier,
                    description,
                    feedPower,
                    Collections.unmodifiableMap(projectiles),
                    Collections.unmodifiableList(bullets),
                    Collections.unmodifiableSet(labels),
                    imgFile,
                    imgIndex,
                    rof,
                    numProj
            );
        }
    }
}
