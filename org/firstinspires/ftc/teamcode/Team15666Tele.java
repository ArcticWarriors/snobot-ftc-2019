/**
 *
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Standard teleop OpMode for Team 15666.
 */

@TeleOp(name = "Standard Tele", group = "Linear Opmode")
public class Team15666Tele extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();

    private GamepadSystem driver = null;
    private GamepadSystem operator = null;
    private DriveSystem leftDrive = null;
    private DriveSystem rightDrive = null;
    private PulleyArmSystem arm = null;

    private PovDriveController driveControl = null;
    private PulleyArmController armControl = null;

    @Override
    public void runOpMode() {
        initSystems();
        updateTelemetry("Initialized");

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            update();
            updateTelemetry("Active");
        }
    }

    private void initSystems() {
        this.driver = new LogitechGamepadSystem(Team15666Devices.getDriverGamepad(this));
        this.operator = new LogitechGamepadSystem(Team15666Devices.getOperatorGamepad(this));
        this.leftDrive = new MotorDriveSystem(
                this.hardwareMap.get(Team15666Devices.DriveLeftClass, Team15666Devices.DriveLeftName),
                Team15666Devices.DriveLeftDirection);
        this.rightDrive = new MotorDriveSystem(
                this.hardwareMap.get(Team15666Devices.DriveRightClass, Team15666Devices.DriveRightName),
                Team15666Devices.DriveRightDirection);
        this.arm = new PulleyArmSystem(
                this.hardwareMap.get(Team15666Devices.PulleyClass, Team15666Devices.PulleyName),
                Team15666Devices.PulleyDirection,
                this.hardwareMap.get(Team15666Devices.ElevatorClass, Team15666Devices.ElevatorName),
                Team15666Devices.ElevatorDirection,
                this.hardwareMap.get(Team15666Devices.ClawClass, Team15666Devices.ClawName));
        this.driveControl = new PovDriveController(this.driver, this.leftDrive, this.rightDrive);
        this.armControl = new PulleyArmController(this.operator, this.arm);
    }

    private void update() {
        this.driveControl.update(this.runtime.seconds());
        this.armControl.update(this.runtime.seconds());
    }

    private void updateTelemetry(final String status) {
        this.telemetry.addData("Status", status);
        this.telemetry.update();
    }
}
