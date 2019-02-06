/**
 *
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Standard autonomous OpMode for Team 15666.
 */

@Autonomous(name = "Dead Rec Auto", group = "Linear Opmode")
public class Team15666Auto extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor right_drive;
    private DcMotor left_drive;
    private DcMotor elevator;
    private Servo claw;

    @Override
    public void runOpMode() {
        initSystems();
        updateTelemetry("Initialized");

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            this.elevator.setPower(0.1);

            if (runtime.seconds() < 2.8) {
                right_drive.setPower(-1.0);
                left_drive.setPower(-1.0);
                claw.setPosition(0.7);
            } else {
                right_drive.setPower(0.0);
                left_drive.setPower(0.0);
                claw.setPosition(Team15666Devices.ClawOpenPosition);
            }

            updateTelemetry("Active");
        }
    }

    private void initSystems() {
        this.right_drive = hardwareMap.dcMotor.get("right_drive");
        this.right_drive.setDirection(DcMotorSimple.Direction.REVERSE);
        this.left_drive = hardwareMap.dcMotor.get("left_drive");
        this.elevator = hardwareMap.dcMotor.get("arm_1");
        this.claw = hardwareMap.servo.get("servo_1");
    }

    private void updateTelemetry(final String status) {
        this.telemetry.addData("Status", status);
        this.telemetry.update();
    }
}
