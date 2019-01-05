/**
 *
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

public interface ControllerSystem extends System {
    Gamepad getDriverGamepad();
    Gamepad getOperatorGamepad();
}
