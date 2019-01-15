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

@Autonomous(name = "Depot Only Auto", group = "Linear Opmode")
public class Team15821AutoDepotOnly extends LinearOpMode {
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

        final float lowerLanderTime = 1.8f;
        final float rotateOffLanderTime = lowerLanderTime + 1.0f;
        final float moveAwayFromLanderTime = rotateOffLanderTime + 0.35f;
        final float lowerArmTime = moveAwayFromLanderTime + 1.0f;
        final float adjustDrivingAngleTime = lowerArmTime + 1.0f;
        final float driveToDepotTime = adjustDrivingAngleTime + 6.4f;
        final float ejectMarkerTime = driveToDepotTime + 2.0f;
        final float turnToAvoidTime = ejectMarkerTime + 1.2f;
        final float driveToAvoidTime = turnToAvoidTime + 1.0f;

        while (opModeIsActive()) {
            if (runtime.seconds() < lowerLanderTime) {
                // Lower from lander
                arm_1.setPower(1.0);
                armServo_1.setPower(0.0);
                right_drive.setPower(0.0);
                left_drive.setPower(0.0);
            } else if (runtime.seconds() < rotateOffLanderTime) {
                // Rotate off lander hook
                arm_1.setPower(0.0);
                right_drive.setPower(0.5);
                left_drive.setPower(-0.5);
            } else if (runtime.seconds() < moveAwayFromLanderTime) {
                // Move away from lander
                arm_1.setPower(0.0);
                right_drive.setPower(-0.5);
                left_drive.setPower(-0.5);
            } else if (runtime.seconds() < lowerArmTime) {
                // Lower arm
                arm_1.setPower(-1.0);
                right_drive.setPower(0.0);
                left_drive.setPower(0.0);
            } else if (runtime.seconds() < adjustDrivingAngleTime) {
                // Adjust driving angle
                arm_1.setPower(0.0);
                right_drive.setPower(-0.5);
                left_drive.setPower(0.5);
            } else if (runtime.seconds() < driveToDepotTime) {
                // Drive to depot
                arm_1.setPower(0.0);
                right_drive.setPower(-0.5);
                left_drive.setPower(-0.5);
            } else if (runtime.seconds() < ejectMarkerTime) {
                // Stop and eject the marker
                arm_1.setPower(0.0);
                armServo_1.setPower(-0.7);
                right_drive.setPower(0.0);
                left_drive.setPower(0.0);
            } else if (runtime.seconds() < turnToAvoidTime) {
                // Turn to crater
                arm_1.setPower(0.0);
                armServo_1.setPower(0.0);
                right_drive.setPower(-0.5);
                left_drive.setPower(0.5);
            } else if (runtime.seconds() < driveToAvoidTime) {
                // Drive to crater
                arm_1.setPower(0.0);
                right_drive.setPower(0.7);
                left_drive.setPower(0.7);
            } else {
                // Stop
                arm_1.setPower(0.0);
                armServo_1.setPower(0.0);
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
