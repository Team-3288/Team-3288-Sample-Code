// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.util.Units;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    public static class OperatorConstants {
    //Controller Ports
    //Can change on the Driver Station
    public static final int driverPort = 0; 
    public static final int operatorPort = 1;
  }

  public static class ShooterConstants {
    //Motor CAN ID
    public static final int shooterIDSparkMax = 5;
    public static final int followerIDSparkMax = 6;
    public static final int shooterIDSparkFlex = 7;
    public static final int followerIDSparkFlex = 8;
    public static final int shooterIDCTRE = 9;
    public static final int followerIDCTRE = 10;

    // The base line target for the motors RPM
    public static final double exampleTargetRPM = 1000;
    public static final double exampleTargetVolts = 6;
  }

  public static class JoystickButtons {
    public static final int squareButton = 1;
    public static final int xButton = 2;
    public static final int oButton = 3;
    public static final int triangleButton = 4;
    public static final int leftBumper = 5;
    public static final int rightBumper = 6;
    public static final int leftTrigger = 7;
    public static final int rightTrigger = 8;
    public static final int shareButton = 9;
    public static final int optionsButton = 10;
    public static final int leftJoystickButton = 11;
    public static final int rightJoystickButton = 12;
  }

  public static class VisionConstants {
    public static final String aprilTagCameraName = "AprilTags";
    public static final String diverCameraName = "Driver";

    // Camera position relative to the shooter/robot center.
    // X = forward, Y = left, Z = up (WPILib convention).
    // Camera is 9 inches to the RIGHT of the shooter (-Y), same height, tilted up 3 degrees (-pitch).
    public static final Transform3d kRobotToCamera = new Transform3d(
        Units.inchesToMeters(0),      // X: forward/back offset
        -Units.inchesToMeters(9),    // Y: left/right offset
        Units.inchesToMeters(0),    // Z: up/down offset
        new Rotation3d(0, Units.degreesToRadians(-5), 0) // tilted up 3 degrees
    );

    public static final double aimToleranceDeg = 0;
    public static final double rangeToleranceM = 0;

    public static final int[] redTagIds = {};
    public static final int[] blueTagIds = {};

    public static final double desiredShotRangeMeters = 0;
  }
}
