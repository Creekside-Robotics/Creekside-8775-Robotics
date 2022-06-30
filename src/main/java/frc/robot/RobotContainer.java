// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.CalibrateArm;
import frc.robot.commands.ManualDrive;
import frc.robot.commands.MoveArm;
import frc.robot.commands.RunFlywheel;
import frc.robot.commands.RunIntake;
import frc.robot.commands.SetArmPosition;
import frc.robot.commands.TestArmMovement;
import frc.robot.subsystems.*;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...

  // Joysticks
  public static Joystick driveStick = new Joystick(0);
  public static Joystick climbStick = new Joystick(1);

  // Buttons for the drive stick
  JoystickButton shootTrigger = new JoystickButton(driveStick, 1);
  JoystickButton intakeButton = new JoystickButton(driveStick, 2);

  // Climb Joystick Buttons
  JoystickButton calibrateRed = new JoystickButton(climbStick, 7);
  JoystickButton getIntoPosition = new JoystickButton(climbStick, 8);
  JoystickButton calibrateYellow = new JoystickButton(climbStick, 9);
  JoystickButton twoBarClimb = new JoystickButton(climbStick, 10);
  JoystickButton calibrateTilt = new JoystickButton(climbStick, 11);
  JoystickButton fourBarClimb = new JoystickButton(climbStick, 12);
  JoystickButton activateArmMovement = new JoystickButton(climbStick, 1);

  // The robot's subsystems
  Drivetrain drivetrain = new Drivetrain();
  Intake intake = new Intake(-1);
  Shooter shooter = new Shooter(1);
  ArmComponent redArm = new SIMArm(Constants.redArmId, false, true, Constants.redRevInRan, 1, 0, "R");
  ArmComponent yellowArm = new SIMArm(Constants.yellowArmId, false, false, Constants.yellowRevInRan, 2, 3, "Y");
  ArmComponent tiltArm = new NeoArm(Constants.tiltId, true, Constants.tiltRevInRan, "T");

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    //Default commands for the different subsystems
    drivetrain.setDefaultCommand(new ManualDrive(drivetrain));
    intake.setDefaultCommand(new RunIntake(intake, 0));
    shooter.setDefaultCommand(new RunFlywheel(shooter, 0));
    redArm.setDefaultCommand(new MoveArm(redArm, "0"));
    yellowArm.setDefaultCommand(new MoveArm(yellowArm, "0"));
    tiltArm.setDefaultCommand(new MoveArm(tiltArm, "0"));

    //Button bindings for the different subsystems
    shootTrigger.whenHeld(new RunFlywheel(shooter, 4.7));
    intakeButton.whenHeld(new RunIntake(intake, 100.5));
    activateArmMovement.whenHeld(new ParallelCommandGroup(
      new MoveArm(redArm, "Y"),
      new MoveArm(yellowArm, "X"),
      new MoveArm(tiltArm, "Z")
    ));

    //Button bindings for arm calibration and testing
    calibrateRed.whenPressed(new CalibrateArm(redArm));
    getIntoPosition.whenHeld(new SetArmPosition(redArm, 1, false));
    calibrateYellow.whenPressed(new CalibrateArm(yellowArm));
    twoBarClimb.whenHeld(new SetArmPosition(redArm, 0, true));
    calibrateTilt.whenPressed(new CalibrateArm(tiltArm));
    fourBarClimb.whenHeld(new SequentialCommandGroup(
      new ParallelCommandGroup(
        new SetArmPosition(redArm, 0, false),
        new SetArmPosition(yellowArm, 1, false),
        new SetArmPosition(tiltArm, 0, false)
      ),
      new ParallelDeadlineGroup(
        new SetArmPosition(tiltArm, 0.35, false),
        new SetArmPosition(redArm, 0, true),
        new SetArmPosition(yellowArm, 1, true)
      ),
      new ParallelCommandGroup(
        new SetArmPosition(yellowArm, 0, false),
        new SetArmPosition(tiltArm, 0.9, false),
        new SetArmPosition(redArm, 0, false)
      ),
      new ParallelDeadlineGroup(
        new SetArmPosition(redArm, 0.7, false),
        new SetArmPosition(yellowArm, 0, true),
        new SetArmPosition(tiltArm, 0.9, true)
      ),
      new ParallelDeadlineGroup(
        new SetArmPosition(tiltArm, 0.5, false),
        new SetArmPosition(redArm, 0.8, true),
        new SetArmPosition(yellowArm, 0, true)
      ),
      new ParallelCommandGroup(
        new SetArmPosition(tiltArm, 0.5, false),
        new SetArmPosition(redArm, 0, false),
        new SetArmPosition(yellowArm, 1, false)
      ),
      new ParallelDeadlineGroup(
        new SetArmPosition(tiltArm, 0.1, false),
        new SetArmPosition(redArm, 0, true),
        new SetArmPosition(yellowArm, 1, true)
      ),
      new ParallelCommandGroup(
        new SetArmPosition(tiltArm, 0.1, false),
        new SetArmPosition(redArm, 0, false),
        new SetArmPosition(yellowArm, 0, false)
      )
    ));

  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // Autonomous command is yet to be developed
    return null;
  }
}
