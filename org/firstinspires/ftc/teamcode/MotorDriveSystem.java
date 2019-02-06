package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

public class MotorDriveSystem implements DriveSystem {
    private final DcMotor motor;

    public MotorDriveSystem(final DcMotor motor, final DcMotor.Direction direction) {
        this.motor = motor;
        this.motor.setDirection(direction);
    }

    @Override
    public void setPower(float power) {
        this.motor.setPower(power);
    }

    @Override
    public String getTelemetry() {
        return String.valueOf(this.motor.getPower());
    }
}
