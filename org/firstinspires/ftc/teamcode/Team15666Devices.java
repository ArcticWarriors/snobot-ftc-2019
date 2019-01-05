package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

public class Team15666Devices {
    public static final Class<DcMotor> DriveLeftClass = DcMotor.class;
    public static final Class<DcMotor> DriveRightClass = DcMotor.class;
    public static final Class<DcMotor> PulleyClass = DcMotor.class;
    public static final Class<DcMotor> ElevatorClass = DcMotor.class;
    public static final Class<Servo> ClawClass = Servo.class;

    public static final int DriverGamepad = 1;
    public static final int OperatorGamepad = 2;
    public static final String DriveLeftName = "left_drive";
    public static final String DriveRightName = "right_drive";
    public static final String PulleyName = "arm_1";
    public static final String ElevatorName = "arm_2";
    public static final String ClawName = "servo_1";

    public static final DcMotor.Direction DriveLeftDirection = DcMotorSimple.Direction.FORWARD;
    public static final DcMotor.Direction DriveRightDirection = DcMotorSimple.Direction.REVERSE;
    public static final DcMotor.Direction PulleyDirection = DcMotorSimple.Direction.FORWARD;
    public static final DcMotor.Direction ElevatorDirection = DcMotorSimple.Direction.FORWARD;

    public static final float ClawTriggerThreshold = 0.5f;
    public static final float ClawOpenPosition = 1.0f;
    public static final float ClawClosePosition = 0.0f;

    public static Gamepad getDriverGamepad(final OpMode opMode) {
        return getGamepad(opMode, DriverGamepad);
    }

    public static Gamepad getOperatorGamepad(final OpMode opMode) {
        return getGamepad(opMode, OperatorGamepad);
    }

    public static Gamepad getGamepad(final OpMode opMode, final int gamepad) {
        if (gamepad == 1) {
            return opMode.gamepad1;
        } else if (gamepad == 2) {
            return opMode.gamepad2;
        }

        return null;
    }
}
