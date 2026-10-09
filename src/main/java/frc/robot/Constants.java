// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;


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
    public static final int shooterIDSpark = 5;
    public static final int followerIDSpark = 6;
    public static final int shooterIDCTRE = 7;
    public static final int followerIDCTRE = 8;

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
}
