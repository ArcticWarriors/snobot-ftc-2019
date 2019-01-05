/**
 *
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

public interface ControlSystem extends System {
    Gamepad getDriverGamepad();
    Gamepad getOperatorGamepad();
}
