package org.firstinspires.ftc.teamcode;

public class PulleyArmController implements Controller {
    private final GamepadSystem input;
    private final PulleyArmSystem arm;

    private boolean clawOpen = true;
    private float prevClawTrigger = 0.0f;

    public PulleyArmController(
            final GamepadSystem input,
            final PulleyArmSystem arm) {
        this.input = input;
        this.arm = arm;
    }

    @Override
    public void update(final double elapsed) {
        final float elevatorOutIn = this.input.getJoyLeftY();
        final float pulleyUpDown = this.input.getJoyRightY();
        final float clawTrigger = this.input.getTrigRight();

        float elevatorPower = elevatorOutIn >= 0.0
                ? elevatorOutIn * Team15666Devices.ElevatorScalePowerUp
                : elevatorOutIn * Team15666Devices.ElevatorScalePowerDown;

        if (elevatorPower >= -Team15666Devices.ElevatorPowerHalt
                && elevatorPower <= Team15666Devices.ElevatorPowerHalt) {
            elevatorPower = Team15666Devices.ElevatorPowerHalt;
        }

        if (this.prevClawTrigger < Team15666Devices.ClawTriggerThreshold
                && clawTrigger >= Team15666Devices.ClawTriggerThreshold) {
            clawOpen = !clawOpen;
        }

        final float clawOpenClose = clawOpen
                ? Team15666Devices.ClawOpenPosition
                : Team15666Devices.ClawClosePosition;
        this.prevClawTrigger = clawTrigger;

        this.arm.getPulley().setPower(pulleyUpDown);
        this.arm.getElevator().setPower(elevatorPower);
        this.arm.getClaw().setPosition(clawOpenClose);
    }

    @Override
    public String getTelemetry() {
        return null;
    }
}
