package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class PulleyArmSystem implements ArmSystem {
    private final DcMotor pulley;
    private final DcMotor elevator;
    private final Servo claw;

    public PulleyArmSystem(
            final DcMotor pulley,
            final DcMotor.Direction pulleyDirection,
            final DcMotor elevator,
            final DcMotor.Direction elevatorDirection,
            final Servo claw) {
        this.pulley = pulley;
        this.pulley.setDirection(pulleyDirection);
        this.elevator = elevator;
        this.elevator.setDirection(elevatorDirection);
        this.claw = claw;
    }

    public DcMotor getPulley() {
        return this.pulley;
    }

    public DcMotor getElevator() {
        return this.elevator;
    }

    public Servo getClaw() {
        return this.claw;
    }

    @Override
    public String getTelemetry() {
        return this.pulley.getPower()
                + "," + this.elevator.getPower()
                + "," + this.claw.getPosition();
    }
}