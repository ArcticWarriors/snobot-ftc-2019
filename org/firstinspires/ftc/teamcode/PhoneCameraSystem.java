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
    public MineralDetection getMineral() {
        final List<Recognition> recognitions = this.tensorflow.getRecognitions();
        MineralDetection mineral = null;

        for (final Recognition recognition : recognitions) {
            if (Objects.equals(recognition.getLabel(), "Gold Mineral")) {
                float location = recognition.getLeft() / recognition.getImageWidth();
                if (location > 0.35 && location < 0.75)
                {
                    mineral = new MineralDetection();
                    mineral.type = MineralType.GOLD;
                    mineral.horizontalLocation = location;
                }
            }
        }

        return mineral;
    }

    @Override
    public String getTelemetry() {
        return null;
    }
}
