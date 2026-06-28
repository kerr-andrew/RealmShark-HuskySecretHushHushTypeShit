package tomato.realmshark.items;

public class Projectile {
    public final int id;
    public final int min;
    public final int max;
    public final boolean piercing;

    private Projectile(int id, int min, int max, boolean piercing) {
        this.id = id;
        this.min = min;
        this.max = max;
        this.piercing = piercing;
    }
    @Override
    public String toString() {
        return "Projectile{" +
                "\n      id=" + id +
                "\n      min=" + min +
                "\n      max=" + max;
    }

    public static class Builder {
        public Builder() { }

        private int id;
        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        private int min;
        public Builder setMin(int min) {
            this.min = min;
            return this;
        }

        private int max;
        public Builder setMax(int max) {
            this.max = max;
            return this;
        }

        private boolean piercing = false;
        public Builder setPiercing(boolean piercing) {
            this.piercing = piercing;
            return this;
        }

        public Projectile build() {
            return new Projectile(
                    this.id,
                    this.min,
                    this.max,
                    this.piercing
            );
        }
    }
}
