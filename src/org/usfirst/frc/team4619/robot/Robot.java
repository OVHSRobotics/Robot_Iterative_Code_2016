
package org.usfirst.frc.team4619.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Timer;
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

	//creates 4 double solenoid valves for drive base and climber
	DoubleSolenoid leftDoubleSolenoidValve;
	DoubleSolenoid rightDoubleSolenoidValve;

	//creates air compressor
	Compressor airCompressor;

	//creates controllers
	Joystick xBoxController;

	//creates speed controllers
	SpeedController frontleft, 
	frontright, 
	backleft, 
	backright, 
	leftShooter, 
	rightShooter, 
	actuator;

	//creates encoders for driving
	Encoder leftEncoderDrive;
	Encoder rightEncoderDrive;
	//double count = 0;

	//creates drive train
	RobotDrive robotDrive;

	//creates servo for pushing the ball
	Servo kicker;

	//create variable for 0 when press a trigger
	int pressed = 0;

	//creates variables for shooting
	double intakeSpeed = 0.8;
	double shootSpeed = 1;
	double actuationSpeed = .25;
	int motorNotSpin = 0;
	double halfRotation = .7;
	int noRotation = 0;
	double servoPower = 0;

	//create variables for xbox buttons
	int A = 1;
	int B = 2;
	int X = 3;
	int Y = 4;
	int LBumper = 5;
	int RBumper = 6;
	int Back = 7;
	int Start = 8;

	//creates variables for xbox axes
	int leftJoyStickYAxis = 1;
	int leftTrigger = 2;
	int rightTrigger = 3;
	int rightJoyStickXAxis = 4;

	//encoder variables
	double p = .005, i = 0, d = 0;

	String after;
	String before;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		chooser = new SendableChooser();
		chooser.addDefault("Default Auto", defaultAuto);
		chooser.addObject("My Auto", customAuto);
		SmartDashboard.putData("Auto choices", chooser);
		leftDoubleSolenoidValve = new DoubleSolenoid(0,1);
		rightDoubleSolenoidValve = new DoubleSolenoid(6,7);
		frontleft = new VictorSP(2);
		frontright = new VictorSP(1);
		backleft = new VictorSP(0);
		backright = new VictorSP(3);
		leftShooter = new VictorSP(4);
		rightShooter = new VictorSP(5);
		kicker = new Servo(6);
		actuator = new CANTalon(1);
		leftEncoderDrive = new Encoder(2,3);
		rightEncoderDrive = new Encoder(0,1);
		//leftEncoderDrive.reset();
		//rightEncoderDrive.reset();
		robotDrive = new RobotDrive(frontleft,backleft,frontright,backright);
		xBoxController = new Joystick(0);
		airCompressor = new Compressor();
		/**((CANTalon) actuator).ClearIaccum();
		before = "before: " + ((CANTalon) actuator).getEncPosition();
		((CANTalon) actuator).set(0);
		after = "After: " + ((CANTalon) actuator).getEncPosition();**/
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
		/**leftEncoderDrive.reset();
		rightEncoderDrive.reset();
		frontleft = new VictorSP(2);
		frontright = new VictorSP(1);
		backleft = new VictorSP(0);
		backright = new VictorSP(3);**/
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
		/**
		if(count <= 2457.6) {
			frontleft.set(.25);
			frontright.set(.25);
			backleft.set(.25);
			backright.set(.25);
			count = (leftEncoderDrive.get() * rightEncoderDrive.get()) / 2;
		}**/
		/**while (count <= 2457.6) {
			frontleft.set(.05);
			frontright.set(.05);
			backleft.set(.05);
			backright.set(.05);
			count = (leftEncoderDrive.get() * rightEncoderDrive.get()) / 2;
		}**/

	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		//robotDrive.tankDrive(xBoxController, rightJoystick, true);
		robotDrive.arcadeDrive(xBoxController.getRawAxis(leftJoyStickYAxis),
				xBoxController.getRawAxis(rightJoyStickXAxis), true);
		System.out.println("Left Grayhill: " + leftEncoderDrive.get() + "\nRight Grayhill: " + rightEncoderDrive.get());
		//1228.8 per rotation

		//Shoots the ball
		if (xBoxController.getRawAxis(rightTrigger)>pressed)
		{
			leftShooter.set(-shootSpeed);
			rightShooter.set(-shootSpeed);
		}
		//intakes the ball
		else if (xBoxController.getRawAxis(leftTrigger)>pressed)
		{
			leftShooter.set(intakeSpeed);
			rightShooter.set(intakeSpeed);
		}
		else
		{
			leftShooter.set(motorNotSpin);
			rightShooter.set(motorNotSpin);
		}
		//changes the intake speed
		/**if (xBoxController.getRawButton(7)&&intakeSpeed+.05<=1)
		{
			intakeSpeed += .05;
			System.out.println("intake:" + intakeSpeed);
		}
		else if (xBoxController.getRawButton(8)&& intakeSpeed-.05>=0)
		{
			intakeSpeed -= .05;
			System.out.println("intake:" + intakeSpeed);
		}
		 **/

		if (xBoxController.getRawButton(A))
		{
			servoPower = halfRotation;
		}
		else 
		{
			servoPower = noRotation;
		}
		kicker.set(servoPower);

		//outputs encoder value ofr actuating arm
		((CANTalon) actuator).setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Absolute);
		SmartDashboard.putNumber("Actuator Encoder: ", ((CANTalon) actuator).getEncPosition());

		/**
		//encoder
		((CANTalon) actuator).configNominalOutputVoltage(0, 0);
		((CANTalon) actuator).configPeakOutputVoltage(12,-12);
		((CANTalon) actuator).setAllowableClosedLoopErr((3*4096)/360);
		((CANTalon) actuator).setPID(p,i,d);
		((CANTalon) actuator).changeControlMode(TalonControlMode.Position);

		if(xBoxController.getRawButton(RBumper)) {
			((CANTalon) actuator).set(30);
		} 
		else if(xBoxController.getRawButton(LBumper)) {
			((CANTalon) actuator).set(0);
		}
		,,
		.


		if(xBoxController.getRawButton(Start)) {
			setShooterAngle(30);
		}
		else if(xBoxController.getRawButton(Back)) {
			setShooterAngle(0);
		}**/

		//actuates the shooting arms
		if(xBoxController.getRawButton(RBumper))
		{
			actuator.set(actuationSpeed);
		}
		else if (xBoxController.getRawButton(LBumper))
		{
			actuator.set(-actuationSpeed);
		}
		else
		{
			actuator.set(motorNotSpin);
		}

		//created compressor 
		airCompressor.setClosedLoopControl(true);

		// Switching gear box with pneumatic
		if(xBoxController.getRawButton(B)){
			leftDoubleSolenoidValve.set(DoubleSolenoid.Value.kForward);
			rightDoubleSolenoidValve.set(DoubleSolenoid.Value.kForward);
		}else if(xBoxController.getRawButton(X)){
			leftDoubleSolenoidValve.set(DoubleSolenoid.Value.kReverse);
			rightDoubleSolenoidValve.set(DoubleSolenoid.Value.kReverse);
		}

	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {

	}

	/**
	public void setShooterAngle(int wantedAngle) {
		int angle = (wantedAngle*4096*16)/(360*22);
		((CANTalon) actuator).setPosition(angle);
	}**/

}
