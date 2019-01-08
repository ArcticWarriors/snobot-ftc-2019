/**
 *
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Point-of-View drive system. The left joystick on the driver gamepad controls speed and the
 * right joystick controls steering.
 */
public class PovDriveSystem implements DriveSystem {
    public final DcMotor left;
    public final DcMotor right;
    public final float turnScale;

    public PovDriveSystem(
            final DcMotor left,
            final DcMotor.Direction leftDirection,
            final DcMotor right,
            final DcMotor.Direction rightDirection,
            final float turnScale) {
        this.left = left;
        this.left.setDirection(leftDirection);
        this.right = right;
        this.right.setDirection(rightDirection);
        this.turnScale = turnScale;
    }

    @Override
    public void update() {

    }

    @Override
    public void update(final ControllerSystem controls) {
        if (controls.getDriverGamepad() == null) {
            return;
        }

        final float forwardBack = controls.getDriverGamepad().left_stick_y;
        final float leftRight = controls.getDriverGamepad().right_stick_x;
        final float leftPower = (forwardBack + leftRight) * this.turnScale;
        final float rightPower = (forwardBack - leftRight) * this.turnScale;
        this.left.setPower(leftPower);
        this.right.setPower(rightPower);
    }

    @Override
    public String getTelemetry() {
        return this.left.getPower() + "," + this.right.getPower();
    }
}
