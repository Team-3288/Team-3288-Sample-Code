package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.RelativeEncoder;

import static frc.robot.Constants.ShooterConstants.*;

public class ExampleSparkSubsystem extends SubsystemBase {

    // This is how you should create your sparkMax / sparkFlex controllers
    private final SparkMax shooterMotor;

    // The code below is more just to keep the warnings down
    private final SparkMax followerMotor;

    // This is how you get the onboard encoder for Rev stuff
    private final RelativeEncoder shooterEncoder;

    public ExampleSparkSubsystem(){
        // This is when you finish setting up your motors
        // MAKE SURE ID AND MOTOR TYPE MATCH
        // If not motor will not go
        shooterMotor = new SparkMax(shooterIDSpark, MotorType.kBrushless);
        followerMotor = new SparkMax(followerIDSpark, MotorType.kBrushless);
        shooterEncoder = shooterMotor.getEncoder();

        // This is how you would set up a Spark config 
        // Use this to have your current limits, invertions, and ramp rate
        SparkMaxConfig shootConfig = new SparkMaxConfig();
        shootConfig.idleMode(IdleMode.kCoast);
        shootConfig.smartCurrentLimit(40);
        shootConfig.inverted(true);
        shootConfig.openLoopRampRate(0.20); //Smooth spin-up, helps brownouts

        // This puts the config on the controller
        shooterMotor.configure(shootConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        // Same as above but make sure to change for each motor
        SparkMaxConfig followerConfig = new SparkMaxConfig();
        followerConfig.idleMode(IdleMode.kCoast);
        followerConfig.smartCurrentLimit(40);
        followerConfig.inverted(true);
        followerConfig.openLoopRampRate(0.20); //Smooth spin-ip, helps brownouts
        followerConfig.follow(shooterIDSpark);

        // Make sure each motor gets it's config applied
        followerMotor.configure(followerConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    // This is for stuff you need to be running while robot is on
    @Override
    public void periodic(){
        // Encoders are the best thing to put in here
        double shooterRPM = shooterEncoder.getVelocity();

        // This puts the values on the SmartDashboard
        SmartDashboard.putNumber("Shooter RPM", shooterRPM);
    }

    // Set the voltage for the shooter
    public void setVoltage(double voltage){
        shooterMotor.setVoltage(voltage);
    }

    // Stop the motor
    public void stopShooter(){
        shooterMotor.set(0);
    }
}
