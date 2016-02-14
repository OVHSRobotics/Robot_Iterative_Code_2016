
package org.usfirst.frc.team4619.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	String autoSelected;
	SendableChooser chooser;

	//creates four motors for drive train
	VictorSP frontleft = new VictorSP(0);
	VictorSP frontright = new VictorSP(1);
	VictorSP backleft = new VictorSP(2);
	VictorSP backright = new VictorSP(3);

	//creates two motors for shooter 
	VictorSP leftShooter = new VictorSP(5);
	VictorSP rightShooter = new VictorSP(4);
	
	Servo kicker = new Servo(6);

	//creates a motor for actuator
	SpeedController actuator = new CANTalon(0);
	
	Joystick xBoxController = new Joystick(0);
	
	RobotDrive robotDrive = new RobotDrive(frontleft,backleft,frontright,backright);
	
	//creates variables for shooting
	double intakeSpeed = 0.6;
	double shootSpeed = 1;
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		chooser = new SendableChooser();
		chooser.addDefault("Default Auto", defaultAuto);
		chooser.addObject("My Auto", customAuto);
		SmartDashboard.putData("Auto choices", chooser);
		leftShooter.setInverted(true);
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString line to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the switch structure below with additional strings.
	 * If using the SendableChooser make sure to add them to the chooser code above as well.
	 */ 
	public void autonomousInit() {
		autoSelected = (String) chooser.getSelected();
		//		autoSelected = SmartDashboard.getString("Auto Selector", defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		switch(autoSelected) {
		case customAuto:
			//Put custom auto code here   
			break;
		case defaultAuto:
		default:
			//Put default auto code here
			break;
		}
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		//robotDrive.tankDrive(xBoxController, rightJoystick, true);
		robotDrive.arcadeDrive(-xBoxController.getRawAxis(1), -xBoxController.getRawAxis(4), true);

		//Shoots the ball
		if (xBoxController.getRawAxis(3)>0)
		{
			leftShooter.set(-shootSpeed);
			rightShooter.set((shootSpeed));
		}
		//intakes the ball
		else if (xBoxController.getRawAxis(2)>0)
		{
			leftShooter.set((intakeSpeed));
			rightShooter.set(-intakeSpeed);
		}
		else
		{
			leftShooter.set(0);
			rightShooter.set(0);
		}
		
		if (xBoxController.getRawButton(1))
		{
			kicker.set(1);
		}
		else
		{
			kicker.set(0);
		}

		//changes the intake speed
		if (xBoxController.getRawButton(3)&&intakeSpeed<=1)
		{
			intakeSpeed += .01;
			System.out.println("intake:" + intakeSpeed);
		}
		else if (xBoxController.getRawButton(4)&& intakeSpeed>=0)
		{
			intakeSpeed -= .01;
			System.out.println("intake:" + intakeSpeed);
		}

		//actuates the shooting arms
		if(xBoxController.getRawButton(5))
		{
			actuator.set(.2);
		}
		else if (xBoxController.getRawButton(6))
		{
			actuator.set(-.2);
		}
		else
		{
			actuator.set(0);
		}
		
		//changes the shooting speed
		if (xBoxController.getRawButton(7)&&shootSpeed<=1)
		{
			shootSpeed += .01;
			System.out.println("shoot:" + shootSpeed);
		}
		else if (xBoxController.getRawButton(8)&& shootSpeed>0)
		{
			shootSpeed = Math.max(0, shootSpeed - 0.01);
			System.out.println("shoot:" + shootSpeed);
		}


	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {

	}

}


