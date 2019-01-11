/**
 *
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Standard autonomous OpMode for Team 15821.
 */

@Autonomous(name = "Crater Auto", group = "Linear Opmode")
public class Team15821AutoCrater extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor right_drive;
    private DcMotor left_drive;
    private DcMotor arm_1;
    private CRServo armServo_1;

    @Override
    public void runOpMode() {
        initSystems();
        updateTelemetry("Initialized");

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            if (runtime.seconds() < 1.8) {
                arm_1.setPower(1.0);
                right_drive.setPower(0.0);
                left_drive.setPower(0.0);
            } else if (runtime.seconds() < 2.5) {
                arm_1.setPower(0.0);
                right_drive.setPower(0.5);
                left_drive.setPower(-0.5);
            } else if (runtime.seconds() < 3.0) {
                arm_1.setPower(0.0);
                right_drive.setPower(-0.5);
                left_drive.setPower(-0.5);
            } else if (runtime.seconds() < 4.0) {
                arm_1.setPower(0.0);
                right_drive.setPower(-0.5);
                left_drive.setPower(0.5);
            } else if (runtime.seconds() < 8.0) {
                arm_1.setPower(0.0);
                right_drive.setPower(-0.5);
                left_drive.setPower(-0.5);
            } else {
                arm_1.setPower(0.0);
                right_drive.setPower(0.0);
                left_drive.setPower(0.0);
            }

            updateTelemetry("Active");
        }
    }

    private void initSystems() {
        this.right_drive = hardwareMap.dcMotor.get("right_drive");
        this.right_drive.setDirection(DcMotorSimple.Direction.REVERSE);
        this.left_drive = hardwareMap.dcMotor.get("left_drive");
        this.arm_1 = hardwareMap.dcMotor.get("arm_1");
        this.armServo_1 = hardwareMap.crservo.get("armServo_1");
    }

    private void updateTelemetry(final String status) {
        this.telemetry.addData("Status", status);
        this.telemetry.update();
    }
}
