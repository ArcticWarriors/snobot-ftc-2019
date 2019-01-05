/**
 *
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Standard teleop OpMode for Team 15666.
 */

@TeleOp(name = "Standard Tele", group = "Linear Opmode")
public class Team15666Tele extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();

    private ControllerSystem controllerSystem = null;
    private DriveSystem driveSystem = null;
    private ArmSystem armSystem = null;

    @Override
    public void runOpMode() {
        initSystems();
        updateTelemetry("Initialized");

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            updateSystems();
            updateTelemetry("Active");
        }
    }

    private void initSystems() {
        this.controllerSystem = new PairControllerSystem(
                Team15666Devices.getDriverGamepad(this),
                Team15666Devices.getOperatorGamepad(this));
        this.driveSystem = new PovDriveSystem(
                this.hardwareMap.get(Team15666Devices.DriveLeftClass, Team15666Devices.DriveLeftName),
                Team15666Devices.DriveLeftDirection,
                this.hardwareMap.get(Team15666Devices.DriveRightClass, Team15666Devices.DriveRightName),
                Team15666Devices.DriveRightDirection);
        this.armSystem = new ElevatorArmSystem(
                this.hardwareMap.get(Team15666Devices.PulleyClass, Team15666Devices.PulleyName),
                Team15666Devices.PulleyDirection,
                this.hardwareMap.get(Team15666Devices.ElevatorClass, Team15666Devices.ElevatorName),
                Team15666Devices.ElevatorDirection,
                this.hardwareMap.get(Team15666Devices.ClawClass, Team15666Devices.ClawName));
    }

    private void updateSystems() {
        this.driveSystem.update(this.controllerSystem);
        this.armSystem.update(this.controllerSystem);
    }

    private void updateTelemetry(final String status) {
        updateTelemetry(
                status,
                this.controllerSystem.getTelemetry(),
                this.driveSystem.getTelemetry(),
                this.armSystem.getTelemetry());
    }

    private void updateTelemetry(
            final String status, final String control, final String drive, final String arm) {
        this.telemetry.addData("Status", status);
        this.telemetry.addData("Control", control);
        this.telemetry.addData("Drive", drive);
        this.telemetry.addData("Arm", arm);
        this.telemetry.update();
    }

    class ElevatorArmSystem implements ArmSystem {
        public final DcMotor pulley;
        public final DcMotor elevator;
        public final Servo claw;

        private boolean clawOpen = true;
        private float prevClawTrigger = 0.0f;

        public ElevatorArmSystem(
                final DcMotor pulley,
                final DcMotor.Direction pulleyDirection,
                final DcMotor elevator,
                final DcMotor.Direction elevatorDirection,
                final Servo claw) {
            this.pulley = pulley;
            this.pulley.setDirection(pulleyDirection);
            this.elevator = elevator;
            this.elevator.setDirection(elevatorDirection);
            this.claw = claw;
        }

        @Override
        public void update(final ControllerSystem controls) {
            if (controls.getOperatorGamepad() == null) {
                return;
            }

            final float pulleyUpDown = controls.getOperatorGamepad().right_stick_y;
            final float elevatorOutIn = controls.getOperatorGamepad().left_stick_y;
            final float clawTrigger = controls.getOperatorGamepad().right_trigger;

            float elevatorPower = elevatorOutIn >= 0.0
                    ? elevatorOutIn * Team15666Devices.ElevatorScalePowerUp
                    : elevatorOutIn * Team15666Devices.ElevatorScalePowerDown;

            if (elevatorPower >= -Team15666Devices.ElevatorPowerHalt
                    && elevatorPower <= Team15666Devices.ElevatorPowerHalt) {
                elevatorPower = Team15666Devices.ElevatorPowerHalt;
            }

            if (prevClawTrigger < Team15666Devices.ClawTriggerThreshold
                    && clawTrigger >= Team15666Devices.ClawTriggerThreshold) {
                clawOpen = !clawOpen;
            }

            final float clawOpenClose = clawOpen
                    ? Team15666Devices.ClawOpenPosition
                    : Team15666Devices.ClawClosePosition;
            prevClawTrigger = clawTrigger;

            this.pulley.setPower(pulleyUpDown);
            this.elevator.setPower(elevatorPower);
            this.claw.setPosition(clawOpenClose);
        }

        @Override
        public String getTelemetry() {
            return this.pulley.getPower()
                    + "," + this.elevator.getPower()
                    + "," + this.claw.getPosition();
        }
    }
}
