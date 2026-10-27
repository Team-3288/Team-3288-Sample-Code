package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import static frc.robot.Constants.ShooterConstants.*;

public class ExampleCTRESubsystem extends SubsystemBase {

    // This is almost the same as Spark but with a few key differences

    private final TalonFX shooterLeader;
    private final TalonFX shooterFollower;

    private final VelocityVoltage velocityReq = new VelocityVoltage(0);

    public ExampleCTRESubsystem(){
        SmartDashboard.putNumber("Shooter Target RPM", exampleTargetRPM);

        // This is needed because if you are using a CANivore  
        // you need to change rio to the name of the CANivore
        shooterLeader = new TalonFX(shooterIDCTRE, "rio");
        shooterFollower = new TalonFX(followerIDCTRE, "rio");

        TalonFXConfiguration cfg = new TalonFXConfiguration();

        cfg.MotorOutput.NeutralMode = NeutralModeValue.Coast;
        cfg.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

        cfg.CurrentLimits.SupplyCurrentLimitEnable = true;
        cfg.CurrentLimits.SupplyCurrentLimit = 40;

        cfg.ClosedLoopRamps.VoltageClosedLoopRampPeriod = 1.0;

        shooterLeader.getConfigurator().apply(cfg);
        shooterFollower.getConfigurator().apply(cfg);

        // This is how you need to do folloing
        shooterFollower.setControl(
            new Follower(shooterLeader.getDeviceID(), MotorAlignmentValue.Opposed)
        );
 
    }

    public double getTargetRPM() {
        return SmartDashboard.getNumber("Shooter Target RPM", exampleTargetRPM);
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Shooter RPM", getShooterRPM());
    }

    // This is one of the ways you can controll the motors with RPM
    public void setRPM(double rpm){
        double rps = rpm / 60.0;
        shooterLeader.setControl(velocityReq.withVelocity(rps));
    }

    public double getShooterRPM() {
        return shooterLeader.getVelocity().getValueAsDouble() * 60.0;
    }

}
