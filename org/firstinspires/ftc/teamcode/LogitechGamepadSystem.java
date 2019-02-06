package org.firstinspires.ftc.teamcode;

import java.lang.Math;
import com.qualcomm.robotcore.hardware.Gamepad;

public class LogitechGamepadSystem implements GamepadSystem {
    private final Gamepad gamepad;
    private final float joyLeftDeadZone;
    private final float joyRightDeadZone;

    public LogitechGamepadSystem(final Gamepad gamepad) {
        this(gamepad, 0.1f, 0.1f);
    }

    public LogitechGamepadSystem(
            final Gamepad gamepad,
            final float joyLeftDeadZone,
            final float joyRightDeadZone) {
        this.gamepad = gamepad;
        this.joyLeftDeadZone = joyLeftDeadZone;
        this.joyRightDeadZone = joyRightDeadZone;
    }

    @Override
    public float getJoyLeftX() {
        return applyDeadZone(this.gamepad.left_stick_x, this.joyLeftDeadZone);
    }

    @Override
    public float getJoyLeftY() {
        return applyDeadZone(this.gamepad.left_stick_y, this.joyLeftDeadZone);
    }

    @Override
    public float getJoyRightX() {
        return applyDeadZone(this.gamepad.right_stick_x, this.joyRightDeadZone);
    }

    @Override
    public float getJoyRightY() {
        return applyDeadZone(this.gamepad.right_stick_y, this.joyRightDeadZone);
    }

    @Override
    public float getTrigLeft() {
        return this.gamepad.left_trigger;
    }

    @Override
    public float getTrigRight() {
        return this.gamepad.right_trigger;
    }

    @Override
    public String getTelemetry() {
        return getJoyLeftX()
                + "," + getJoyLeftY()
                + "," + getJoyRightX()
                + "," + getJoyRightY();
    }

    private float applyDeadZone(final float joy, final float deadZone) {
        return Math.abs(joy) <= deadZone ? 0.0f : joy;
    }
}
