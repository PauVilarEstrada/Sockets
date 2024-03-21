package P11_SistemsDistribuitProcesamentsParalel;

import java.io.Serializable;

public class Result implements Serializable {
    private int nodeID;
    private int result;

    public Result(int nodeID, int result) {
        this.nodeID = nodeID;
        this.result = result;
    }

    public int getNodeID() {
        return nodeID;
    }

    public int getResult() {
        return result;
    }
}
