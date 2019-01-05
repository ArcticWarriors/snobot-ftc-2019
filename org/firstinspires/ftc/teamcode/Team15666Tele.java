/**
 *
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Standard teleop OpMode for Team 15666.
 */

@TeleOp(name = "Standard Tele", group = "Linear Opmode")
public class Team15666Tele extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();

    private ControlSystem controlSystem = null;
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
        this.controlSystem = new PairControlSystem(
                Team15666Devices.getDriverGamepad(this),
                Team15666Devices.getOperatorGamepad(this));
        this.driveSystem = new PovDriveSystem(
                this.hardwareMap.get(Team15666Devices.DriveLeftClass, Team15666Devices.DriveLeftName),
                Team15666Devices.DriveLeftDirection,
                this.hardwareMap.get(Team15666Devices.DriveRightClass, Team15666Devices.DriveRightName),
                Team15666Devices.DriveRightDirection);
        this.armSystem = new PulleyElevatorArmSystem(
                this.hardwareMap.get(Team15666Devices.PulleyClass, Team15666Devices.PulleyName),
                Team15666Devices.PulleyDirection,
                this.hardwareMap.get(Team15666Devices.ElevatorClass, Team15666Devices.ElevatorName),
                Team15666Devices.ElevatorDirection);
    }

    private void updateSystems() {
        this.driveSystem.update(this.controlSystem);
        this.armSystem.update(this.controlSystem);
    }

    private void updateTelemetry(final String status) {
        updateTelemetry(
                status,
                this.controlSystem.getTelemetry(),
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

    interface System {
        String getTelemetry();
    }

    interface ControlSystem extends System {
        Gamepad getDriverGamepad();
        Gamepad getOperatorGamepad();
    }

    interface DriveSystem extends System {
        void update(final ControlSystem controls);
    }

    interface ArmSystem extends System {
        void update(final ControlSystem controls);
    }

    /**
     * Control system for two, a driver and an operator.
     */
    class PairControlSystem implements ControlSystem {
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

    /**
     * Point-of-View drive system. The left joystick on the driver gamepad controls speed and the
     * right joystick controls steering.
     */
    class PovDriveSystem implements DriveSystem {
        public final DcMotor left;
        public final DcMotor right;

        public PovDriveSystem(
                final DcMotor left,
                final DcMotor.Direction leftDirection,
                final DcMotor right,
                final DcMotor.Direction rightDirection) {
            this.left = left;
            this.left.setDirection(leftDirection);
            this.right = right;
            this.right.setDirection(rightDirection);
        }

        @Override
        public void update(final ControlSystem controls) {
            final float forwardBack = controls.getDriverGamepad().left_stick_y;
            final float leftRight = controls.getDriverGamepad().right_stick_x;
            this.left.setPower(forwardBack + leftRight);
            this.right.setPower(forwardBack - leftRight);
        }

        @Override
        public String getTelemetry() {
            return this.left.getPower() + "," + this.right.getPower();
        }
    }

    class PulleyElevatorArmSystem implements ArmSystem {
        public final DcMotor pulley;
        public final DcMotor elevator;

        public PulleyElevatorArmSystem(
                final DcMotor pulley,
                final DcMotor.Direction pulleyDirection,
                final DcMotor elevator,
                final DcMotor.Direction elevatorDirection) {
            this.pulley = pulley;
            this.pulley.setDirection(pulleyDirection);
            this.elevator = elevator;
            this.elevator.setDirection(elevatorDirection);
        }

        @Override
        public void update(final ControlSystem controls) {
            final float pulleyUpDown = controls.getOperatorGamepad().right_stick_y;
            final float elevatorOutIn = controls.getOperatorGamepad().left_stick_y;
            this.pulley.setPower(pulleyUpDown);
            this.elevator.setPower(elevatorOutIn);
        }

        @Override
        public String getTelemetry() {
            return this.pulley.getPower() + "," + this.elevator.getPower();
        }
    }
}
