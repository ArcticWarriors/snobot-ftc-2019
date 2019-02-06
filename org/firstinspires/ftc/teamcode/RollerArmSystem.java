package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

public class RollerArmSystem implements ArmSystem {
    public final DcMotor tower;
    public final CRServo roller;

    public RollerArmSystem(
            final DcMotor tower,
            final DcMotor.Direction towerDirection,
            final CRServo roller,
            final CRServo.Direction rollerDirection) {
        this.tower = tower;
        this.tower.setDirection(towerDirection);
        this.roller = roller;
        this.roller.setDirection(rollerDirection);
    }

    public DcMotor getTower() {
        return this.tower;
    }

    public CRServo getRoller() {
        return this.roller;
    }

    @Override
    public String getTelemetry() {
        return this.tower.getPower() + "," + this.roller.getPower();
    }
}
