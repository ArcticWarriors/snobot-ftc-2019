package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaRoverRuckus;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TfodRoverRuckus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PhoneCameraSystem implements CameraSystem {
    private final VuforiaRoverRuckus vuforia;
    private final TfodRoverRuckus tensorflow;

    public PhoneCameraSystem(final VuforiaRoverRuckus vuforia, final TfodRoverRuckus tensorflow) {
        this.vuforia = vuforia;
        this.tensorflow = tensorflow;
    }

    @Override
    public Position getPosition() {
        return null;
    }

    @Override
    public Orientation getOrientation() {
        return null;
    }

    @Override
    public ArrayList<Mineral> getMinerals() {
        final List<Recognition> recognitions = this.tensorflow.getRecognitions();
        final ArrayList<Mineral> minerals = new ArrayList<>();

        for (final Recognition recognition : recognitions) {
            if (Objects.equals(recognition.getLabel(), "Gold Mineral")) {
                minerals.add(Mineral.GOLD);
            } else {
                minerals.add(Mineral.SILVER);
            }
        }

        return minerals;
    }

    @Override
    public String getTelemetry() {
        return null;
    }
}
