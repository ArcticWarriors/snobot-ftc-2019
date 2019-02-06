/**
 *
 */

package org.firstinspires.ftc.teamcode;

        import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
        import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
        import com.qualcomm.robotcore.util.ElapsedTime;

        import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
        import org.firstinspires.ftc.robotcore.external.navigation.VuforiaRoverRuckus;
        import org.firstinspires.ftc.robotcore.external.tfod.TfodRoverRuckus;

/**
 * Camera autonomous OpMode for Team 15821.
 */

@Autonomous(name = "Camera Auto", group = "Linear Opmode")
public class Team15821AutoCamera extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();

    private final VuforiaRoverRuckus vuforia = new VuforiaRoverRuckus();
    private final TfodRoverRuckus tensorflow = new TfodRoverRuckus();

    private CameraSystem camera = null;
    private DriveSystem leftDrive = null;
    private DriveSystem rightDrive = null;
    private RollerArmSystem arm = null;

    private CameraDriveController driveControl = null;

    @Override
    public void runOpMode() {
        initSystems();
        updateTelemetry("Initialized");

        waitForStart();
        runtime.reset();

        if (opModeIsActive()) {
            this.vuforia.activate();
            this.tensorflow.activate();

            while (opModeIsActive()) {
                update();
                updateTelemetry("Active");
            }

            this.tensorflow.deactivate();
            this.vuforia.deactivate();
        }

        this.tensorflow.close();
        this.vuforia.close();
    }

    private void initSystems() {
        this.vuforia.initialize(
                "",
                VuforiaLocalizer.CameraDirection.BACK,
                true,
                false,
                VuforiaLocalizer.Parameters.CameraMonitorFeedback.AXES,
                0, 0, 0,
                0, 0, 0,
                true);
        this.tensorflow.initialize(
                this.vuforia,
                0.4f,
                true,
                true);

        this.camera = new PhoneCameraSystem(this.vuforia, this.tensorflow);
        this.leftDrive = new MotorDriveSystem(
                this.hardwareMap.get(Team15821Devices.DriveLeftClass, Team15821Devices.DriveLeftName),
                Team15821Devices.DriveLeftDirection);
        this.rightDrive = new MotorDriveSystem(
                this.hardwareMap.get(Team15821Devices.DriveRightClass, Team15821Devices.DriveRightName),
                Team15821Devices.DriveRightDirection);
        this.driveControl = new CameraDriveController(this.camera, this.leftDrive, this.rightDrive);
    }

    private void update() {
        this.driveControl.update(this.runtime.seconds());
    }

    private void updateTelemetry(final String status) {
        this.telemetry.addData("Status", status);
        this.telemetry.addData("Camera", this.driveControl.getTelemetry());
        this.telemetry.update();
    }
}
