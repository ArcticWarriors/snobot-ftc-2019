/**
 *
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
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
                this.hardwareMap.get(Team15821Devices.RollerClass, Team15821Devices.RollerName),
                Team15821Devices.RollerDirection);
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

    class TowerRollerArmSystem implements ArmSystem {
        public final DcMotor tower;
        public final CRServo roller;

        public TowerRollerArmSystem(
                final DcMotor tower,
                final DcMotor.Direction towerDirection,
                final CRServo roller,
                final CRServo.Direction rollerDirection) {
            this.tower = tower;
            this.tower.setDirection(towerDirection);
            this.roller = roller;
            this.roller.setDirection(rollerDirection);
        }

        @Override
        public void update(final ControlSystem controls) {
            if (controls.getOperatorGamepad() == null) {
                return;
            }

            final float towerUpDown = controls.getOperatorGamepad().left_stick_y;
            final float rollerIn = controls.getOperatorGamepad().right_trigger;
            final float rollerOut = controls.getOperatorGamepad().left_trigger;

            this.tower.setPower(towerUpDown);

            if (rollerIn >= Team15821Devices.RollerTriggerThreshold) {
                this.roller.setPower(Team15821Devices.RollerPower);
            } else if (rollerOut >= Team15821Devices.RollerTriggerThreshold) {
                this.roller.setPower(-Team15821Devices.RollerPower);
            } else {
                this.roller.setPower(0.0f);
            }
        }

        @Override
        public String getTelemetry() {
            return this.tower.getPower() + "," + this.roller.getPower();
        }
    }
}
