/**
 *
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * Control system for two, a driver and an operator.
 */
public class PairControlSystem implements ControlSystem {
    public final Gamepad driver;
    public final Gamepad operator;

    public PairControlSystem(final Gamepad driver, final Gamepad operator) {
        this.driver = driver;
        this.operator = operator;
    }

    public Gamepad getDriverGamepad() {
        return this.driver;
    }

    public Gamepad getOperatorGamepad() {
        return this.operator;
    }

    @Override
    public String getTelemetry() {
        return this.driver.left_stick_x
                + "," + this.driver.left_stick_y
                + "," + this.driver.right_stick_x
                + "," + this.driver.right_stick_y
                + " "
                + this.operator.left_stick_x
                + "," + this.operator.left_stick_y
                + "," + this.operator.right_stick_x
                + "," + this.operator.right_stick_y;
    }
}
