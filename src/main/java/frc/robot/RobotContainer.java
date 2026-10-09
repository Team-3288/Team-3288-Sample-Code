// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;
import static frc.robot.Constants.OperatorConstants.*;
import static frc.robot.Constants.ShooterConstants.*;
import static frc.robot.Constants.JoystickButtons.*;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.Autos;
import frc.robot.commands.ExampleCTRECommand;
import frc.robot.commands.ExampleSparkCommand;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.ExampleCTRESubsystem;
import frc.robot.subsystems.ExampleSparkSubsystem;



/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {

  private final double MaxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // desired top speed

  private final double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity

  /* Setting up bindings for necessary control of the swerve drive platform */
  private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
      .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
      .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors

  private final Telemetry logger = new Telemetry(MaxSpeed);

  // Below is how we get the value for the joysticks
  private double leftX() { return driverController.isConnected() ? driverController.getRawAxis(0) : 0.0; } // LS X

  private double leftY()  { return driverController.isConnected() ? driverController.getRawAxis(1) : 0.0; } // LS Y

  @SuppressWarnings("unused")
  private double rightX() { return driverController.isConnected() ? driverController.getRawAxis(5) : 0.0; } // RS X

  private double rightY() { return driverController.isConnected() ? driverController.getRawAxis(2) : 0.0; } // RS Y

  // This is for if the joystick is pushed down and then changes the joystick value if need be
  // the value after the ? is what it is normally * by and after the : is what the value will be * by after button is pushed
  private double speedMult() { return driverController.isConnected() && driverController.getRawButton(11) ? 0.33 : 0.66; }

  private double fullSpeed() { return driverController.isConnected() && driverController.getRawButton(12) ? 0.66: 1.0;}


  // The robot's subsystems and commands are defined here
  private final ExampleCTRESubsystem exampleCTRESubsystem = new ExampleCTRESubsystem();
  private final ExampleSparkSubsystem exampleSparkSubsystem = new ExampleSparkSubsystem();

  public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();

  //Driver controller
  private final static GenericHID driverController = new GenericHID(driverPort);

  //Operator controller
  private final static GenericHID operatorController = new GenericHID(operatorPort);

  //Auto chooser
  private final SendableChooser<Command> autoChooser = new SendableChooser<>();


  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();

    // Autos
    autoChooser.setDefaultOption("Drive Forward", Autos.driveForward(drivetrain, 1.0, 2.0));
    autoChooser.addOption("Do Nothing", Commands.none());
    SmartDashboard.putData("Auto Chooser", autoChooser);
  }


  private void configureBindings() {
    /* This is the drive command I have been using
     * 
     * If the Bot is going backwards when you try and 
     * go forward check to make sure your on the right 
     * alliance on the drive station or if that is right
     * change the x *= -1.0 to x *= 1.0 do the same for y
     */
    drivetrain.setDefaultCommand(
      drivetrain.applyRequest(()-> {
        
        double x = leftY();
        double y = leftX();
        double rot = rightY();

        var alliance = DriverStation.getAlliance();
        if (alliance.isPresent() && alliance.get() == DriverStation.Alliance.Blue) {
          x *= -1.0;
          y *= -1.0;
        } else {
          rot *= -1.0;
        }
        return drive.withVelocityX(x * MaxSpeed * (speedMult() / fullSpeed()))
          .withVelocityY(y * MaxSpeed * (speedMult() / fullSpeed()))
          .withRotationalRate(rot * MaxAngularRate * (speedMult()) + 0.14);
      })
      );

    // This is important DO NO DELETE
    drivetrain.registerTelemetry(logger::telemeterize);

    JoystickButton runSparkExample = new JoystickButton(operatorController, oButton);

    JoystickButton runCTREExample = new JoystickButton(operatorController, triangleButton);

    runSparkExample.whileTrue(new ExampleCTRECommand(exampleCTRESubsystem, exampleTargetRPM));

    runCTREExample.whileTrue(new ExampleSparkCommand(exampleSparkSubsystem, exampleTargetVolts));
  }


  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }
}

