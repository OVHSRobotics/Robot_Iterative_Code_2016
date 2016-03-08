
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
	final String spyZoneShoot = "Spy Zone Shoot";
	final String touchDefenses = "Touch Defenses";
	final String pastLowBar = "Low Bar";
	final String defaultCommand = "Default Command";
	String autoSelected;
	SendableChooser chooser;
	
	SendableChooser actuatorChooser;
	String actuatorSelector;
	final String xBoxDefault = "xBox";
	final String attackThree = "attack3";

	//creates 4 double solenoid valves for drive base and climber
	DoubleSolenoid leftDoubleSolenoidValve;
	DoubleSolenoid rightDoubleSolenoidValve;

	StringBuilder sb;
	
	//creates air compressor
	Compressor airCompressor;

	//creates controllers
	Joystick xBoxController;
	Joystick attack3;
	
	//creates speed controllers
	SpeedController frontleft, 
	frontright, 
	backleft, 
	backright, 
	leftShooter, 
	rightShooter; 
	CANTalon actuator;

	//creates encoders for driving
	Encoder leftEncoderDrive;
	Encoder rightEncoderDrive;
	double count = 0;
	double autonomousSpeed = .4;

	//creates drive train
	RobotDrive robotDrive;
	int loopCounter = 0;

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
	double increaseRatio = .002;
	
	//create variables for xbox buttons
	int A = 1;
	int B = 2;
	int X = 3;
	int Y = 4;
	int LBumper = 5;
	int RBumper = 6;
	int Back = 7;
	int Start = 8;
	
	//creates start time
	long startTime;
	
	//creates variables for ATK3 buttons
	int trigger = 1;
	int button2 = 2;
	int button3 = 3;
	int button4 = 4;
	int button5 = 5;
	int button6 = 6;
	int button7 = 7;
	int button8 = 8;
	int button9 = 9;
	int button10 = 10;
	int button11 = 11;
	
	//creates variables for xbox axes
	int leftJoyStickYAxis = 1;
	int leftTrigger = 2;
	int rightTrigger = 3;
	int rightJoyStickXAxis = 4;

	//encoder variables
	double p = .1, i = 0, d = 0, f = 0;

	String after;
	String before;
	private double motornotspinSpeed;
	private double halfrotationSpeed;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		actuatorChooser = new SendableChooser();
		actuatorChooser.addDefault(xBoxDefault, xBoxDefault);
		actuatorChooser.addObject(attackThree, attackThree);
		
		chooser = new SendableChooser();
		chooser.addDefault(defaultCommand, defaultCommand);
		chooser.addObject(touchDefenses, touchDefenses);
		chooser.addObject(spyZoneShoot, spyZoneShoot);
		chooser.addObject(pastLowBar, pastLowBar);
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
		attack3 = new Joystick(1);
		airCompressor = new Compressor();
		sb = new StringBuilder();
	
		//int absolutePosition = actuator.getPulseWidthPosition() & 0xFFF;
		//actuator.setEncPosition(absolutePosition);

		actuator.reset();
		actuator.enable();
		
		actuator.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Absolute);
		actuator.reverseSensor(true);

		actuator.configNominalOutputVoltage(+0f, -0f);
		actuator.configPeakOutputVoltage(+6f,-6f);
		actuator.setAllowableClosedLoopErr(0);

		actuator.setProfile(0);
		actuator.setF(f);
		actuator.setP(p);
		actuator.setI(i);
		actuator.setD(d);

		actuator.changeControlMode(TalonControlMode.Position);
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
		leftEncoderDrive.reset();
		rightEncoderDrive.reset();
		
		startTime = System.currentTimeMillis();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		smartDashboardOutput();
		switch(autoSelected) {
		case defaultCommand:
			if (System.currentTimeMillis() - startTime < 2500) {
				frontleft.set(autonomousSpeed);
				frontright.set(autonomousSpeed);
				backleft.set(autonomousSpeed);
				backright.set(autonomousSpeed);
			}
			else
			{
				frontleft.set(0);
				frontright.set(0);
				backleft.set(0);
				backright.set(0);
			}
			break;
		case spyZoneShoot:
			actuateArm(.45);
			shoot();
			Timer.delay(.01);
			kicker.set(halfRotation);
			break;
		case touchDefenses:
			if (count <= 2457.6) {
				frontleft.set(autonomousSpeed);
				frontright.set(autonomousSpeed);
				backleft.set(autonomousSpeed);
				backright.set(autonomousSpeed);
				count = (leftEncoderDrive.get() * rightEncoderDrive.get()) / 2;
			}
			else {
				frontleft.set(0);
				frontright.set(0);
				backleft.set(0);
				backright.set(0);
			}
			break;
		case pastLowBar:
			if (count <= 9830.4) {
				frontleft.set(autonomousSpeed);
				frontright.set(autonomousSpeed);
				backleft.set(autonomousSpeed);
				backright.set(autonomousSpeed);
				count = (leftEncoderDrive.get() * rightEncoderDrive.get()) / 2;
			}
			else {
				frontleft.set(0);
				frontright.set(0);
				backleft.set(0);
				backright.set(0);
			}
			break;
		}

	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		//robotDrive.tankDrive(xBoxController, rightJoystick, true);
		robotDrive.arcadeDrive(xBoxController.getRawAxis(leftJoyStickYAxis),
				xBoxController.getRawAxis(rightJoyStickXAxis), true);
		//1228.8 per rotation

		//Shoots the ball
		if (xBoxController.getRawAxis(rightTrigger)>pressed)
		{
			shoot();
		}
		//intakes the ball
		else if (xBoxController.getRawAxis(leftTrigger)>pressed)
		{
			intake();
		}
		else
		{
			stopShooter();
		}

		if (xBoxController.getRawButton(A))
		{
			servoPower = halfRotation;
		}
		else 
		{
			servoPower = noRotation;
		}
		kicker.set(servoPower);

		if(loopCounter<50)
			smartDashboardOutput();

		
		//creates buttons for increase/decrese shooting/intake speed
		if(attack3.getRawButton(button11))
		{
			increaseShootingSpeed();
		}
		if(attack3.getRawButton(button10))
		{
			decreaseShootingSpeed();
		}
		if(attack3.getRawButton(button6))
		{
			increaseIntakeSpeed();
		}
		if(attack3.getRawButton(button7))
		{
			decreaseIntakeSpeed();
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
		
		//actuates shooting arms
		switch(actuatorSelector) {
		case xBoxDefault:
			if(xBoxController.getRawButton(RBumper))
			{
				actuateArm(.45);
			}
			else if (xBoxController.getRawButton(LBumper))
			{
				actuateArm(0);
			}
			break;
		case attackThree:
			dialToActuationAngle(attack3.getRawAxis(2));
			break;
		}
		
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {

	}

	public void shoot() {
		leftShooter.set(-shootSpeed);
		rightShooter.set(-shootSpeed);
	}
	
	public void intake() {
		leftShooter.set(intakeSpeed);
		rightShooter.set(intakeSpeed);
	}
	
	public void stopShooter() {
		leftShooter.set(0);
		rightShooter.set(0);
	}
	
	public void increaseShootingSpeed(){
		if (shootSpeed >= 0 && shootSpeed <= 1)
			shootSpeed -= increaseRatio;
	}
	
	public void decreaseShootingSpeed(){
		if (shootSpeed >= 0 && shootSpeed <= 1)
			shootSpeed += increaseRatio;
	}
	public void increaseIntakeSpeed(){
		if (intakeSpeed >= 0 && intakeSpeed <= 1)
			intakeSpeed += increaseRatio;
	}
	public void decreaseIntakeSpeed(){
		if (intakeSpeed >= 0 && intakeSpeed <= 1)
			intakeSpeed -= increaseRatio;
	}
	public void actuateArm(double angle) {
		//encoder
		double motorOutput = actuator.getOutputVoltage()/ actuator.getBusVoltage();

		sb.append("\tout: ");
		sb.append(motorOutput + "\n");
		sb.append("\tpos");
		sb.append(actuator.getPosition() + "\n");

		sb.append("\terrNative");
		sb.append(actuator.getClosedLoopError() + "\n");
		
		actuator.set(angle);

			System.out.println(sb.toString());
			
		}

	public void smartDashboardOutput() {
		//outputs encoder value of actuating arm
		SmartDashboard.putNumber("Actuator Encoder: ", actuator.getEncPosition());

		//outputs encoder value of right motor
		SmartDashboard.putNumber("Right Driving Encoder: ", rightEncoderDrive.get());

		//outputs encoder value of left motor 
		SmartDashboard.putNumber("Left Driving Encoder: ", leftEncoderDrive.get());

		//outputs various speeds
		SmartDashboard.putNumber("Intake Speed: ", intakeSpeed);
		SmartDashboard.putNumber("Shoot Speed: ", shootSpeed);
		SmartDashboard.putNumber("Actuation Speed: ", actuationSpeed);
		SmartDashboard.putNumber("Motor Not Spin Speed: ", motornotspinSpeed);
		SmartDashboard.putNumber("Half Rotation Speed: ", halfrotationSpeed);
		SmartDashboard.putNumber("No Rotation Speed: ", noRotation);
		SmartDashboard.putNumber("Servo Power: ", servoPower);

		SmartDashboard.putString("Autonomous", "Commands");
		loopCounter = 0;
	}
	
	public void dialToActuationAngle(double dial) {
		actuateArm(((((dial*-1)+1)/2)*95)/360);
	}
}
