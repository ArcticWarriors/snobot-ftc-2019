/**
 *
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Standard teleop OpMode for Team 15821.
 */

@TeleOp(name = "Standard Tele", group = "Linear Opmode")
public class Team15821Tele extends LinearOpMode {
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
                Team15821Devices.getDriverGamepad(this),
                Team15821Devices.getOperatorGamepad(this));
        this.driveSystem = new PovDriveSystem(
                this.hardwareMap.get(Team15821Devices.DriveLeftClass, Team15821Devices.DriveLeftName),
                Team15821Devices.DriveLeftDirection,
                this.hardwareMap.get(Team15821Devices.DriveRightClass, Team15821Devices.DriveRightName),
                Team15821Devices.DriveRightDirection);
        this.armSystem = new TowerRollerArmSystem(
                this.hardwareMap.get(Team15821Devices.TowerClass, Team15821Devices.TowerName),
                Team15821Devices.TowerDirection,
                this.hardwareMap.get(Team15821Devices.RollerClass, Team15821Devices.RollerName));
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

    class TowerRollerArmSystem implements ArmSystem {
        public final DcMotor tower;
        public final Servo roller;

        public TowerRollerArmSystem(
                final DcMotor tower,
                final DcMotor.Direction towerDirection,
                final Servo roller) {
            this.tower = tower;
            this.tower.setDirection(towerDirection);
            this.roller = roller;
        }

        @Override
        public void update(final ControlSystem controls) {
            final float towerUpDown = controls.getOperatorGamepad().left_stick_y;
            //final float rollerInOut = controls.getOperatorGamepad().left_stick_y;
            this.tower.setPower(towerUpDown);
            //this.elevator.setPower(elevatorOutIn);
            //this.roller.setDirection(Servo.Direction.FORWARD);
        }

        @Override
        public String getTelemetry() {
            return this.tower.getPower() + "," + this.roller.getDirection();
        }
    }
}
