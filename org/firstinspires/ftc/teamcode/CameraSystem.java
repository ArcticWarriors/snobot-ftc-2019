package org.firstinspires.ftc.teamcode;

import java.util.List;

public interface CameraSystem extends System {
    Position getPosition();
    Orientation getOrientation();
    MineralDetection getMineral();

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

    enum MineralType {
        GOLD,
        SILVER
    }

    class MineralDetection {
        MineralType type;
        float horizontalLocation; // 0.0 is left, 1.0 is right
    }
}
