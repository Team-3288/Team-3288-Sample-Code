package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.RelativeEncoder;

import static frc.robot.Constants.ShooterConstants.*;

public class ExampleSparkSubsystem extends SubsystemBase {

    // This is how you should create your sparkMax / sparkFlex controllers
    private final SparkMax shooterMotorMax;
    private final SparkFlex shooterMotorFlex;

    // The code below is more just to keep the warnings down
    private final SparkMax followerMotorMax;
    private final SparkFlex followerMotorFlex;

    // This is how you get the onboard encoder for Rev stuff
    private final RelativeEncoder shooterEncoderMax;
    private final RelativeEncoder shooterEncoderFlex;

    public ExampleSparkSubsystem(){
        // This is when you finish setting up your motors
        // MAKE SURE ID AND MOTOR TYPE MATCH
        // If not motor will not go
        // This is for SparkMax Motor CONTROLLERS
        shooterMotorMax = new SparkMax(shooterIDSparkMax, MotorType.kBrushless);
        followerMotorMax = new SparkMax(followerIDSparkMax, MotorType.kBrushless);
        shooterEncoderMax = shooterMotorMax.getEncoder();

        // This is how you would set up a SparkMax config 
        // Use this to have your current limits, invertions, and ramp rate
        SparkMaxConfig shootConfigMax = new SparkMaxConfig();
        shootConfigMax.idleMode(IdleMode.kCoast);
        shootConfigMax.smartCurrentLimit(40);
        shootConfigMax.inverted(true);
        shootConfigMax.openLoopRampRate(0.20); //Smooth spin-up, helps brownouts

        // This puts the config on the controller
        shooterMotorMax.configure(shootConfigMax, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        // Same as above but make sure to change for each motor
        SparkMaxConfig followerConfigMax = new SparkMaxConfig();
        followerConfigMax.idleMode(IdleMode.kCoast);
        followerConfigMax.smartCurrentLimit(40);
        followerConfigMax.inverted(true);
        followerConfigMax.openLoopRampRate(0.20); //Smooth spin-ip, helps brownouts
        followerConfigMax.follow(shooterIDSparkMax);

        // Make sure each motor gets it's config applied
        followerMotorMax.configure(followerConfigMax, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        
        // This if for SparkFlex Motor CONTROLLERS
        shooterMotorFlex = new SparkFlex(shooterIDSparkFlex, MotorType.kBrushless);
        followerMotorFlex = new SparkFlex(followerIDSparkFlex, MotorType.kBrushless);
        shooterEncoderFlex = shooterMotorFlex.getEncoder();

        // Here is the same thing but for Spark Flex
        // Note this is the same as a Max but with Flex instead 
        // because why would you just make one config class to do this
        SparkFlexConfig shootConfigFlex = new SparkFlexConfig();
        shootConfigFlex.idleMode(IdleMode.kCoast);
        shootConfigFlex.smartCurrentLimit(40);
        shootConfigFlex.inverted(true);
        shootConfigFlex.openLoopRampRate(0.20); //Smooth spin-up, helps brownouts

        shooterMotorFlex.configure(shootConfigFlex, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        SparkFlexConfig followerConfigFlex = new SparkFlexConfig();
        followerConfigFlex.idleMode(IdleMode.kCoast);
        followerConfigFlex.smartCurrentLimit(40);
        followerConfigFlex.inverted(true);
        followerConfigFlex.openLoopRampRate(0.20); //Smooth spin-ip, helps brownouts
        followerConfigFlex.follow(shooterIDSparkFlex);

        followerMotorFlex.configure(followerConfigFlex, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    // This is for stuff you need to be running while robot is on
    @Override
    public void periodic(){
        // Encoders are the best thing to put in here
        double shooterRPMMax = shooterEncoderMax.getVelocity();
        double shooterRPMFlex = shooterEncoderFlex.getVelocity();

        // This puts the values on the SmartDashboard
        SmartDashboard.putNumber("Shooter RPM on Spark Max", shooterRPMMax);
        SmartDashboard.putNumber("Shooter RPM on Spark Flex", shooterRPMFlex);
    }

    // Set the voltage for the shooter
    public void setVoltage(double voltage){
        shooterMotorMax.setVoltage(voltage);
    }

    // Stop the motor
    public void stopShooter(){
        shooterMotorMax.set(0);
    }
}
