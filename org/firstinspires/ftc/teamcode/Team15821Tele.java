/**
 *
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Standard teleop OpMode for Team 15821.
 */

@TeleOp(name = "Standard Tele", group = "Linear Opmode")
public class Team15821Tele extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();

    private GamepadSystem driver = null;
    private GamepadSystem operator = null;
    private DriveSystem leftDrive = null;
    private DriveSystem rightDrive = null;
    private RollerArmSystem arm = null;

    private PovDriveController driveControl = null;
    private RollerArmController armControl = null;

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
        this.driver = new LogitechGamepadSystem(Team15821Devices.getDriverGamepad(this));
        this.operator = new LogitechGamepadSystem(Team15821Devices.getOperatorGamepad(this));
        this.leftDrive = new MotorDriveSystem(
                this.hardwareMap.get(Team15821Devices.DriveLeftClass, Team15821Devices.DriveLeftName),
                Team15821Devices.DriveLeftDirection);
        this.rightDrive = new MotorDriveSystem(
                this.hardwareMap.get(Team15821Devices.DriveRightClass, Team15821Devices.DriveRightName),
                Team15821Devices.DriveRightDirection);
        this.arm = new RollerArmSystem(
                this.hardwareMap.get(Team15821Devices.TowerClass, Team15821Devices.TowerName),
                Team15821Devices.TowerDirection,
                this.hardwareMap.get(Team15821Devices.RollerClass, Team15821Devices.RollerName),
                Team15821Devices.RollerDirection);
        this.driveControl = new PovDriveController(this.driver, this.leftDrive, this.rightDrive);
        this.armControl = new RollerArmController(this.operator, this.arm);
    }

    private void updateSystems() {
        this.driveControl.update(this.runtime.seconds());
        this.armControl.update(this.runtime.seconds());
    }

    private void updateTelemetry(final String status) {
        this.telemetry.addData("Status", status);
        this.telemetry.addData("Driver", this.driver.getTelemetry());
        this.telemetry.addData("Operator", this.operator.getTelemetry());
        this.telemetry.addData("POV Drive", this.driveControl.getTelemetry());
        this.telemetry.addData("Arm", this.armControl.getTelemetry());
        this.telemetry.update();
    }
}