package tomato.backend.data.items;

public class Bullet {
    public int id;
    public float rof = -1;
    public int numProj;

    public int min;
    public int max;

    @Override
    public String toString() {
        return "Bullet{" +
                "\n      id=" + id +
                "\n      min=" + min +
                "\n      max=" + max +
                "\n      rof=" + rof +
                "\n      numProj=" + numProj;
    }
}
