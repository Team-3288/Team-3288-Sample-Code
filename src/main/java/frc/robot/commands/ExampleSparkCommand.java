package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ExampleSparkSubsystem;

public class ExampleSparkCommand extends Command {

    // This is the command for a spark subsystem 
    // make sure you put in the names right.
    private final ExampleSparkSubsystem spark;
    private final double volts;

    public ExampleSparkCommand(ExampleSparkSubsystem spark, double volts){
        this.spark = spark;
        this.volts = volts;
        addRequirements(spark);
    }

    @Override
    public void initialize(){}

    @Override
    public void execute(){
        spark.setVoltage(volts);
    }

    @Override
    public void end(boolean interrupted){
        spark.stopShooter();
    }

    @Override
    public boolean isFinished(){
        return false;
    }
    
}
