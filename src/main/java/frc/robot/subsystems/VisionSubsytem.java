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
        // Fully set up the cameras
        tagCamera = new PhotonCamera(aprilTagCameraName);
        driveCamera = new PhotonCamera(diverCameraName);
        // Driver mode maks it so the camera is just a normal camera
        // TAGS WILL NOT BE READ WHILE IN DRIVE RMODE
        driveCamera.setDriverMode(true);
        tagCamera.setDriverMode(true);
    }

    // This is used to switch the camera's mode when called
    // True = drivermode
    // False = look for tags (Idk what it is called :P)
    public void setAprilTagDriverMode(boolean driverMode){
        tagCamera.setDriverMode(driverMode);
    }

    @Override
    public void periodic() {
        // This is only return the tag while not in driver mode
        latestResult = tagCamera.getLatestResult();

        // I feel like I don't need to exspain this but
        // aimed is the camera sees the tag
        // inRange the tag is within a certain range
        // ready both of them put together 
        boolean aimed   = isAimedAtGoal();
        boolean inRange = isInRangeOfGoal();
        boolean ready   = aimed && inRange;

        // Put the booleans on the dashboard
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

        // This will put the tag numbers on the dashboard that the camera can see
        String visibleTags = latestResult.hasTargets()
            ? latestResult.getTargets().stream()
                .map(t -> String.valueOf(t.getFiducialId()))
                .reduce((a, b) -> a + ", " + b)
                .orElse("None")
            : "None" ;
        SmartDashboard.putString("Visible Tags", visibleTags);

        // Most likly you will not need all of the tags on the field so
        // this is to make sure that you only are using the ones you need.
        int[] allianceIds = getAllianceTagIds();
        SmartDashboard.putString("Alliance Tag IDs", Arrays.toString(allianceIds));
    }

    // Same thing only using the tag you have in the Constants.java for each side of the field
    private int[] getAllianceTagIds() {
        var alliance = DriverStation.getAlliance();
        if (alliance.isPresent() && alliance.get() == Alliance.Red) {
            return redTagIds;
        }
        return blueTagIds;
    }

    // This is for finding the closest tag that the camera can see the best
    public Optional<PhotonTrackedTarget> getBestGoalTarget() {
        if (!latestResult.hasTargets()) return Optional.empty();

        int[] ids = getAllianceTagIds();
        return latestResult.getTargets().stream()
            .filter(t -> Arrays.stream(ids).anyMatch(id -> id == t.getFiducialId()))
            .max((a, b) -> Double.compare(a.getArea(), b.getArea()));
    }
    
    // Get the degree of the best visible tag
    public Optional<Double> getGoalYawDeg() {
        return getBestGoalTarget().map(PhotonTrackedTarget::getYaw);
    }

    // Gets the distance (in meters) with accounting for the camera's position on the robot
    // and the angle of the camera then moves that number to estimate the distance from the
    // exit of the shooter
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

    // Is the goal tag in the camera view
    public boolean isAimedAtGoal() {
        var yaw = getGoalYawDeg();
        return yaw.isPresent() && Math.abs(yaw.get()) <= aimToleranceDeg;
    }

    // Is the tag close enough
    public boolean isInRangeOfGoal() {
        var dist = getGoalDistanceMeters();
        return dist.isPresent() && dist.get() <= (desiredShotRangeMeters + rangeToleranceM);
    }

    // Is goal tag in camera view AND is close enough
    public boolean isReadyRoShoot() {
        return isAimedAtGoal() && isInRangeOfGoal();
    }
}
