package P11_SistemsDistribuitProcesamentsParalel;

import java.io.Serializable;

public class Task implements Serializable {
    private int value;

    public Task(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
