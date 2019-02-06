package org.firstinspires.ftc.teamcode;

import java.util.ArrayList;

public interface CameraSystem extends System {
    Position getPosition();
    Orientation getOrientation();
    ArrayList<Mineral> getMinerals();

    class Position {
        // Distances in inches, origin at center of field
        float x;
        float y;
        float z;
    }

    class Orientation {
        // Angles in degrees
        float x;
        float y;
        float z;
    }

    enum Mineral {
        GOLD,
        SILVER
    }
}
