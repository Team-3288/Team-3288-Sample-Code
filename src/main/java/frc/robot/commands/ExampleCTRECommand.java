package frc.robot.commands;

import frc.robot.subsystems.ExampleCTRESubsystem;
import edu.wpi.first.wpilibj2.command.Command;

public class ExampleCTRECommand extends Command {

    // This is the command for a CTRE subsystem 
    // make sure you put in the names right.
    private final ExampleCTRESubsystem ctre;
    private final double rpm;

    public ExampleCTRECommand(ExampleCTRESubsystem ctre, double rpm){
        this.ctre = ctre;
        this.rpm = rpm;
        addRequirements(ctre);
    }

    @Override
    public void initialize(){}

    @Override
    public void execute(){
        ctre.setRPM(rpm);
    }

    @Override
    public void end(boolean interrupted){
        ctre.setRPM(0);
    }

    @Override
    public boolean isFinished(){
        return false;
    }
    
}
