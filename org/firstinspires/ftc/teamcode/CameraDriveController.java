package org.firstinspires.ftc.teamcode;

import java.util.HashMap;
import java.util.Objects;

public class CameraDriveController implements Controller {
    private abstract class State {
        public final String name;
        public final float expirationTime;
        public final float left;
        public final float right;
        public final float tower;
        public final float roller;

        private double startTime = 0.0f;

        public State(
                final String name,
                final float expirationTime,
                final float left,
                final float right,
                final float tower,
                final float roller) {
            this.name = name;
            this.expirationTime = expirationTime;
            this.left = left;
            this.right = right;
            this.tower = tower;
            this.roller = roller;
        }

        public void start(final double elapsed) {
            this.startTime = (float) elapsed;
        }

        public double getStateTime(final double elapsed) {
            return elapsed - this.startTime;
        }

        public abstract String getNext(final double elapsed, final CameraSystem input);
    }

    private class StateMachine {
        private final HashMap<String, State> states = new HashMap<>();
        private State currentState = null;

        private final CameraSystem input;
        private final DriveSystem left;
        private final DriveSystem right;
        private final RollerArmSystem arm;

        public StateMachine(
                final CameraSystem input,
                final DriveSystem left,
                final DriveSystem right,
                final RollerArmSystem arm) {
            this.input = input;
            this.left = left;
            this.right = right;
            this.arm = arm;
        }

        public void addState(final State state) {
            this.states.put(state.name, state);

            if (this.currentState == null) {
                this.currentState = state;
            }
        }

        public String getCurrentState() {
            return (this.currentState == null) ? "none" : this.currentState.name;
        }

        public void setFirst(final String name) {
            this.currentState = this.states.get(name);
        }

        public void update(final double elapsed) {
            final String nextStateName = this.currentState.getNext(elapsed, this.input);

            if (!Objects.equals(nextStateName, this.currentState.name)) {
                this.currentState = this.states.get(nextStateName);
                this.currentState.start(elapsed);
            }

            this.left.setPower(this.currentState.left);
            this.right.setPower(this.currentState.right);
            this.arm.getTower().setPower(this.currentState.tower);
            this.arm.getRoller().setPower(this.currentState.roller);
        }
    }

    private final CameraSystem input;
    private final DriveSystem left;
    private final DriveSystem right;
    private final RollerArmSystem arm;

    private final StateMachine stateMachine;

    private String telemetry;

    private boolean foundGold = false;
    private int goldPosition = -1;
    private double alignedWithGoldTime = 1000000000.0;

    public CameraDriveController(
            final CameraSystem input,
            final DriveSystem left,
            final DriveSystem right,
            final RollerArmSystem arm) {
        this.input = input;
        this.left = left;
        this.right = right;
        this.arm = arm;

        this.stateMachine = new StateMachine(this.input, this.left, this.right, this.arm);

        this.stateMachine.addState(
                new State("Begin", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f) {
                    @Override
                    public String getNext(double elapsed, CameraSystem input) {
                        return "Lower from lander";
                    }
                });
        this.stateMachine.addState(
                new State("Lower from lander", 1.7f, 0.0f, 0.0f, -1.0f, 0.0f) {
                    @Override
                    public String getNext(double elapsed, CameraSystem input) {
                        if (this.getStateTime(elapsed) < this.expirationTime) {
                            return this.name;
                        }

                        return "Rotate off lander";
                    }
                });
        this.stateMachine.addState(
                new State("Rotate off lander", 0.6f, -0.5f, 0.5f, 0.0f, 0.0f) {
                    @Override
                    public String getNext(double elapsed, CameraSystem input) {
                        if (this.getStateTime(elapsed) < expirationTime) {
                            return this.name;
                        }

                        return "Move away from lander";
                    }
                });
        this.stateMachine.addState(
                new State("Move away from lander",0.2f, -0.5f, -0.5f, 0.0f, 0.0f) {
                    @Override
                    public String getNext(double elapsed, CameraSystem input) {
                        if (this.getStateTime(elapsed) < expirationTime) {
                            return this.name;
                        }

                        return "Lower arm";
                    }
                });
        this.stateMachine.addState(
                new State("Lower arm",1.5f, 0.0f, 0.0f, 0.7f, 0.0f) {
                    @Override
                    public String getNext(double elapsed, CameraSystem input) {
                        if (this.getStateTime(elapsed) < expirationTime) {
                            return this.name;
                        }

                        return "Examine mineral";
                    }
                });
        this.stateMachine.addState(
                new State("Examine mineral",1.5f, 0.0f, 0.0f, 0.0f, 0.0f) {
                    private int mineralPosition = 0;

                    @Override
                    public String getNext(double elapsed, CameraSystem input) {
                        if (this.getStateTime(elapsed) < expirationTime) {
                            return this.name;
                        }

                        final boolean foundGold = input.getMineral() != null;

                        if (foundGold) {
                            if (mineralPosition == 0) {
                                return "Raise arm from left";
                            } else if (mineralPosition == 1) {
                                return "Raise arm from center";
                            } else {
                                return "Raise arm from right";
                            }
                        }

                        mineralPosition += 1;

                        return (mineralPosition >= 3) ? "Raise arm from right" : "Scan right";
                    }
                });
        this.stateMachine.addState(
                new State("Scan right",0.5f, 0.6f, -0.6f, 0.0f, 0.0f) {
                    @Override
                    public String getNext(double elapsed, CameraSystem input) {
                        if (this.getStateTime(elapsed) < expirationTime) {
                            return this.name;
                        }

                        return "Examine mineral";
                    }
                });
        this.stateMachine.addState(
                new State("Raise arm from left",1.0f, 0.0f, 0.0f, -0.7f, 0.0f) {
                    @Override
                    public String getNext(double elapsed, CameraSystem input) {
                        if (this.getStateTime(elapsed) < expirationTime) {
                            return this.name;
                        }

                        return "Drive to left mineral";
                    }
                });
        this.stateMachine.addState(
                new State("Raise arm from center",1.0f, 0.0f, 0.0f, -0.7f, 0.0f) {
                    @Override
                    public String getNext(double elapsed, CameraSystem input) {
                        if (this.getStateTime(elapsed) < expirationTime) {
                            return this.name;
                        }

                        return "Drive to center mineral";
                    }
                });
        this.stateMachine.addState(
                new State("Raise arm from right",1.0f, 0.0f, 0.0f, -0.7f, 0.0f) {
                    @Override
                    public String getNext(double elapsed, CameraSystem input) {
                        if (this.getStateTime(elapsed) < expirationTime) {
                            return this.name;
                        }

                        return "Drive to right mineral";
                    }
                });
        this.stateMachine.addState(
                new State("Drive to left mineral",1.7f, -0.8f, -0.8f, 0.0f, 0.0f) {
                    @Override
                    public String getNext(double elapsed, CameraSystem input) {
                        if (this.getStateTime(elapsed) < expirationTime) {
                            return this.name;
                        }

                        return "Turn to depot from left mineral";
                    }
                });
        this.stateMachine.addState(
                new State("Turn to depot from left mineral",1.3f, 0.55f, -0.55f, 0.0f, 0.0f) {
                    @Override
                    public String getNext(double elapsed, CameraSystem input) {
                        if (this.getStateTime(elapsed) < expirationTime) {
                            return this.name;
                        }

                        return "Drive to depot from left mineral";
                    }
                });
        this.stateMachine.addState(
                new State("Drive to depot from left mineral",1.5f, -0.8f, -0.8f, 0.0f, 0.0f) {
                    @Override
                    public String getNext(double elapsed, CameraSystem input) {
                        if (this.getStateTime(elapsed) < expirationTime) {
                            return this.name;
                        }

                        return "Drop the marker from left";
                    }
                });
        this.stateMachine.addState(
                new State("Drive to center mineral",2.8f, -0.8f, -0.8f, 0.0f, 0.0f) {
                    @Override
                    public String getNext(double elapsed, CameraSystem input) {
                        if (this.getStateTime(elapsed) < expirationTime) {
                            return this.name;
                        }

                        return "Drop the marker from center";
                    }
                });
        this.stateMachine.addState(
                new State("Drive to right mineral",1.7f, -0.8f, -0.8f, 0.0f, 0.0f) {
                    @Override
                    public String getNext(double elapsed, CameraSystem input) {
                        if (this.getStateTime(elapsed) < expirationTime) {
                            return this.name;
                        }

                        return "Turn to depot from right mineral";
                    }
                });
        this.stateMachine.addState(
                new State("Turn to depot from right mineral",1.6f, -0.55f, 0.55f, 0.0f, 0.0f) {
                    @Override
                    public String getNext(double elapsed, CameraSystem input) {
                        if (this.getStateTime(elapsed) < expirationTime) {
                            return this.name;
                        }

                        return "Drive to depot from right mineral";
                    }
                });
        this.stateMachine.addState(
                new State("Drive to depot from right mineral", 1.5f,-0.8f, -0.8f, 0.0f, 0.0f) {
                    @Override
                    public String getNext(double elapsed, CameraSystem input) {
                        if (this.getStateTime(elapsed) < expirationTime) {
                            return this.name;
                        }

                        return "Drop the marker from right";
                    }
                });
        this.stateMachine.addState(
                new State("Drop the marker from left",1.5f, 0.0f, 0.0f, 0.0f, -0.8f) {
                    @Override
                    public String getNext(double elapsed, CameraSystem input) {
                        if (this.getStateTime(elapsed) < expirationTime) {
                            return this.name;
                        }

                        return "Align with crater from left";
                    }
                });
        this.stateMachine.addState(
                new State("Align with crater from left",2.1f, -0.6f, 0.6f, 0.0f, 0.0f) {
                    @Override
                    public String getNext(double elapsed, CameraSystem input) {
                        if (this.getStateTime(elapsed) < expirationTime) {
                            return this.name;
                        }

                        return "Drive to crater";
                    }
                });
        this.stateMachine.addState(
                new State("Drop the marker from center",1.6f, 0.0f, 0.0f, 0.0f, -0.8f) {
                    @Override
                    public String getNext(double elapsed, CameraSystem input) {
                        if (this.getStateTime(elapsed) < expirationTime) {
                            return this.name;
                        }

                        return "Align with crater from center";
                    }
                });
        this.stateMachine.addState(
                new State("Align with crater from center",1.2f, -0.6f, 0.6f, 0.0f, 0.0f) {
                    @Override
                    public String getNext(double elapsed, CameraSystem input) {
                        if (this.getStateTime(elapsed) < expirationTime) {
                            return this.name;
                        }

                        return "Drive to crater";
                    }
                });
        this.stateMachine.addState(
                new State("Drop the marker from right", 1.5f,0.0f, 0.0f, 0.0f, -0.8f) {
                    @Override
                    public String getNext(double elapsed, CameraSystem input) {
                        if (this.getStateTime(elapsed) < expirationTime) {
                            return this.name;
                        }

                        return "Align with crater from right";
                    }
                });
        this.stateMachine.addState(
                new State("Align with crater from right",0.5f, -0.6f, 0.6f, 0.0f, 0.0f) {
                    @Override
                    public String getNext(double elapsed, CameraSystem input) {
                        if (this.getStateTime(elapsed) < expirationTime) {
                            return this.name;
                        }

                        return "Drive to crater";
                    }
                });
        this.stateMachine.addState(
                new State("Drive to crater",1.5f, 0.6f, 0.6f, 0.0f, 0.0f) {
                    @Override
                    public String getNext(double elapsed, CameraSystem input) {
                        if (this.getStateTime(elapsed) < expirationTime) {
                            return this.name;
                        }

                        return "Curve to crater";
                    }
                });
        this.stateMachine.addState(
                new State("Curve to crater",3.4f, 0.6f, 0.55f, 0.0f, 0.0f) {
                    @Override
                    public String getNext(double elapsed, CameraSystem input) {
                        if (this.getStateTime(elapsed) < expirationTime) {
                            return this.name;
                        }

                        return "End";
                    }
                });
        this.stateMachine.addState(
                new State("End",0.0f, 0.0f, 0.0f, 0.0f, 0.0f) {
                    @Override
                    public String getNext(double elapsed, CameraSystem input) {
                        return "End";
                    }
                });
    }

    @Override
    public void update(final double elapsed) {
        this.stateMachine.update(elapsed);
        this.telemetry = this.stateMachine.getCurrentState();
    }

    @Override
    public String getTelemetry() {
        return this.telemetry;
    }
}
