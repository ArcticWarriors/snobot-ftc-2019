package org.firstinspires.ftc.teamcode;

public class PovDriveController implements Controller {
    private final GamepadSystem input;
    private final DriveSystem left;
    private final DriveSystem right;
    private final float turnScale;

    public PovDriveController(
            final GamepadSystem input,
            final DriveSystem left,
            final DriveSystem right) {
        this(input, left, right, 1.0f);
    }

    public PovDriveController(
            final GamepadSystem input,
            final DriveSystem left,
            final DriveSystem right,
            final float turnScale) {
        this.input = input;
        this.left = left;
        this.right = right;
        this.turnScale = turnScale;
    }

    @Override
    public void update(final double elapsed) {
        final float forwardBack = this.input.getJoyLeftY();
        final float leftRight = this.input.getJoyRightX();
        final float leftPower = (forwardBack + leftRight) * this.turnScale;
        final float rightPower = (forwardBack - leftRight) * this.turnScale;
        this.left.setPower(leftPower);
        this.right.setPower(rightPower);
    }

    @Override
    public String getTelemetry() {
        return "L: " + this.left.getTelemetry() + ", R: " + this.right.getTelemetry();
    }
}
