package P13_Similars.P2_MonitoreigDeTraficEnTempsReal;

import java.io.*;
import java.net.*;

public class TrafficData implements Serializable {
    private static final long serialVersionUID = 1L;
    private int velocidad;
    private int densidad;

    public TrafficData(int velocidad, int densidad) {
        this.velocidad = velocidad;
        this.densidad = densidad;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    public int getDensidad() {
        return densidad;
    }

    public void setDensidad(int densidad) {
        this.densidad = densidad;
    }
}
