// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

// Base class of arm subsystem
public abstract class ArmComponent extends SubsystemBase {
  /** Creates a new ArmComponent. */
  protected CANSparkMax mainMotor;
  protected String key;


  public ArmComponent(int deviceId, MotorType type, boolean reverse, String key) {
    this.mainMotor = new CANSparkMax(deviceId, type);
    this.mainMotor.setInverted(reverse);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber(key, getPosition());
  }

  public void setSpeed(double speed){
    this.mainMotor.set(speed);
  }

  public abstract void resetPosition();

  public abstract double getPosition();

  public abstract double getEncoderRate();
}
