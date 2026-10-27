package frc.robot.subsystems;

import java.util.Arrays;
import java.util.Optional;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.VisionConstants.*;

public class VisionSubsytem extends SubsystemBase {
    private final PhotonCamera tagCamera;
    private final PhotonCamera driveCamera;
    private PhotonPipelineResult latestResult = new PhotonPipelineResult();

    public VisionSubsytem(){
        tagCamera = new PhotonCamera(aprilTagCameraName);

        driveCamera = new PhotonCamera(diverCameraName);
        driveCamera.setDriverMode(true);

        tagCamera.setDriverMode(true);
    }

    public void setAprilTagDriverMode(boolean driverMode){
        tagCamera.setDriverMode(driverMode);
    }

    @Override
    public void periodic() {
        latestResult = tagCamera.getLatestResult();

        boolean aimed   = isAimedAtGoal();
        boolean inRange = isInRangeOfGoal();
        boolean ready   = aimed && inRange;

        SmartDashboard.putBoolean("Shot Ready",    ready);
        SmartDashboard.putBoolean("Aimed at Goal", aimed);
        SmartDashboard.putBoolean("In Range",      inRange);

        SmartDashboard.putBoolean("Camera has Targets", latestResult.hasTargets());
        SmartDashboard.putBoolean("Camera in Driver Mode", tagCamera.getDriverMode());

        double distanceM = getGoalDistanceMeters().orElse(-1.0);
        SmartDashboard.putNumber("Distance to Goal (m)", distanceM);
        SmartDashboard.putBoolean("Vision Has Distance", distanceM >= 0);

        getBestGoalTarget().ifPresent(t -> {
            SmartDashboard.putNumber("Yaw to Goal (deg)", t.getYaw());
            SmartDashboard.putNumber("Goal Tag ID",       t.getFiducialId());
        });

        String visibleTags = latestResult.hasTargets()
            ? latestResult.getTargets().stream()
                .map(t -> String.valueOf(t.getFiducialId()))
                .reduce((a, b) -> a + ", " + b)
                .orElse("None")
            : "None" ;
        SmartDashboard.putString("Visible Tags", visibleTags);

        int[] allianceIds = getAllianceTagIds();
        SmartDashboard.putString("Alliance Tag IDs", Arrays.toString(allianceIds));
    }

    private int[] getAllianceTagIds() {
        var alliance = DriverStation.getAlliance();
        if (alliance.isPresent() && alliance.get() == Alliance.Red) {
            return redTagIds;
        }
        return blueTagIds;
    }

    public Optional<PhotonTrackedTarget> getBestGoalTarget() {
        if (!latestResult.hasTargets()) return Optional.empty();

        int[] ids = getAllianceTagIds();
        return latestResult.getTargets().stream()
            .filter(t -> Arrays.stream(ids).anyMatch(id -> id == t.getFiducialId()))
            .max((a, b) -> Double.compare(a.getArea(), b.getArea()));
    }
    
    public Optional<Double> getGoalYawDeg() {
        return getBestGoalTarget().map(PhotonTrackedTarget::getYaw);
    }

    public Optional<Double> getGoalDistanceMeters() {
        return getBestGoalTarget().map(t ->  {
            Pose3d targetInRobotFrame = new Pose3d()
                .transformBy(kRobotToCamera)
                .transformBy((t.getBestCameraToTarget()));
            double x = targetInRobotFrame.getX();
            double y = targetInRobotFrame.getY();
            return Math.sqrt(x * x + y * y);
        });
    }

    public boolean isAimedAtGoal() {
        var yaw = getGoalYawDeg();
        return yaw.isPresent() && Math.abs(yaw.get()) <= aimToleranceDeg;
    }

    public boolean isInRangeOfGoal() {
        var dist = getGoalDistanceMeters();
        return dist.isPresent() && dist.get() <= (desiredShotRangeMeters + rangeToleranceM);
    }

    public boolean isReadyRoShoot() {
        return isAimedAtGoal() && isInRangeOfGoal();
    }
}
