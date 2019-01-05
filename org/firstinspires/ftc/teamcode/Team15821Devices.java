package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;

public class Team15821Devices {
    public static final Class<DcMotor> DriveLeftClass = DcMotor.class;
    public static final Class<DcMotor> DriveRightClass = DcMotor.class;
    public static final Class<DcMotor> TowerClass = DcMotor.class;
    public static final Class<CRServo> RollerClass = CRServo.class;

    public static final int DriverGamepad = 1;
    public static final int OperatorGamepad = 2;
    public static final String DriveLeftName = "left_drive";
    public static final String DriveRightName = "right_drive";
    public static final String TowerName = "arm_1";
    public static final String RollerName = "armServo_1";

    public static final DcMotor.Direction DriveLeftDirection = DcMotorSimple.Direction.FORWARD;
    public static final DcMotor.Direction DriveRightDirection = DcMotorSimple.Direction.REVERSE;
    public static final DcMotor.Direction TowerDirection = DcMotorSimple.Direction.FORWARD;

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
