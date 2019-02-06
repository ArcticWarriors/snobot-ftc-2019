package org.firstinspires.ftc.teamcode;

public class CameraDriveController implements Controller {
    private final CameraSystem input;
    private final DriveSystem left;
    private final DriveSystem right;

    private String telemetry;

    public CameraDriveController(
            final CameraSystem input,
            final DriveSystem left,
            final DriveSystem right) {
        this.input = input;
        this.left = left;
        this.right = right;
    }

    @Override
    public void update(final double elapsed) {
        boolean sawGold = false;

        for (CameraSystem.Mineral mineral : this.input.getMinerals()) {
            if (mineral == CameraSystem.Mineral.GOLD) {
                sawGold = true;
                break;
            }
        }

        if (sawGold) {
            this.left.setPower(0.75f);
            this.right.setPower(0.75f);
            this.telemetry = "GOLD!";
        } else {
            this.left.setPower(0.0f);
            this.right.setPower(0.0f);
            this.telemetry = "No gold :-(";
        }
    }

    @Override
    public String getTelemetry() {
        return this.telemetry + " " + this.input.getMinerals().size();
    }
}
