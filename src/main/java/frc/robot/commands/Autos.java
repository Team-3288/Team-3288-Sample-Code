package frc.robot.commands;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CommandSwerveDrivetrain;

/** Holds all of the autonomous routines. Add new autos as more static methods here. */
public final class Autos {

  private Autos() {}

  /*
   * Robot-centric means "forward" is whichever way the robot is facing,
   * so no alliance flipping is needed
   */
  private static final SwerveRequest.RobotCentric autoDrive = new SwerveRequest.RobotCentric()
      .withDriveRequestType(DriveRequestType.OpenLoopVoltage);

  private static final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();

  /**
   * Drives straight forward for a set time, then locks the wheels.
   * Distance is roughly speed * time.
   *
   * @param speed meters per second (negative to go backwards)
   * @param seconds how long to drive for
   */
  public static Command driveForward(CommandSwerveDrivetrain drivetrain, double speed, double seconds) {
    return drivetrain.applyRequest(() ->
            autoDrive.withVelocityX(speed)
                .withVelocityY(0)
                .withRotationalRate(0))
        .withTimeout(seconds)
        .andThen(drivetrain.applyRequest(() -> brake));
  }
}
