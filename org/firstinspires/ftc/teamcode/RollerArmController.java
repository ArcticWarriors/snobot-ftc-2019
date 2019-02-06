package org.firstinspires.ftc.teamcode;

public class RollerArmController implements Controller {
    private final GamepadSystem input;
    private final RollerArmSystem arm;

    public RollerArmController(
            final GamepadSystem input,
            final RollerArmSystem arm) {
        this.input = input;
        this.arm = arm;
    }

    @Override
    public void update(final double elapsed) {
        final float towerUpDown = this.input.getJoyLeftY();
        final float rollerIn = this.input.getTrigRight();
        final float rollerOut = this.input.getTrigLeft();

        if (towerUpDown >= 0.5 || towerUpDown <= -0.5) {
            this.arm.getTower().setPower(towerUpDown);
        } else {
            this.arm.getTower().setPower(0.0);
        }

        if (rollerIn >= Team15821Devices.RollerTriggerThreshold) {
            this.arm.getRoller().setPower(Team15821Devices.RollerPower);
        } else if (rollerOut >= Team15821Devices.RollerTriggerThreshold) {
            this.arm.getRoller().setPower(-Team15821Devices.RollerPower);
        } else {
            this.arm.getRoller().setPower(0.0f);
        }
    }

    @Override
    public String getTelemetry() {
        return "Input: " + this.input.getTelemetry() + ", Arm: " + this.arm.getTelemetry();
    }
}
