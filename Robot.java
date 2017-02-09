package org.usfirst.frc.team5510.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//
// IF YOU WANT TO DEPLOY CODE TO ROBORIO, GO TO RUN AT THE TOP AND PRESS RUN AS WPILIB JAVA
//


public class Robot extends IterativeRobot implements PIDOutput{
	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	final String backUpPlan = "Base Line Auto";
	final String rightStart = "Right Start";
	final String leftStart = "Left Start";
	final String midStart = "Mid Start";
	String autoSelected;
	SendableChooser <String> chooser = new SendableChooser<>();
	
	RobotDrive erroll;
	
	//spm  eed controllers, find the ports on the roboRIO
	Victor rightFront;
	Victor rightBack;
	Victor leftFront;
	Victor leftBack;
	
	Talon gearMotor;
	
	boolean reverseDrive;
	boolean forwardDrive;
	
	Joystick xboxController;
	
	PIDController turnController; 
	double rotateToAngleRate;
	static double kP = 0.001; //increase until oscillations and then half
	static double kI = 0.00; //increase until offset is corrected in time
	static double kD = 0.00; //increase until quick enough
	static final double kF = 0.00; 
	static final double kToleranceDegrees = 2.0f;
 
	
	@Override
	public void robotInit() {
		chooser.addDefault("Default Auto", defaultAuto);
		//chooser.addObject("My Auto", customAuto);
		chooser.addObject("BaseLineAuto", backUpPlan);
		chooser.addObject("Right Start" , rightStart);
		chooser.addObject("Left Start", leftStart);
		chooser.addObject("Mid Start", midStart);
		SmartDashboard.putData("Auto modes", chooser);
		
		xboxController = new Joystick(0);
		
		reverseDrive=false;
		forwardDrive=true;
		
		rightFront = new Victor (1); //port 1
		rightBack = new Victor (2); //port 2
		leftFront = new Victor (3); //port 3
		leftBack = new Victor (4); //port 4
		
		gearMotor = new Talon (0);  
		
		erroll = new RobotDrive(rightFront, rightBack, leftFront, leftBack); 
		
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		//autoSelected = chooser.getSelected();
		autoSelected = SmartDashboard.getString("Auto Selector", defaultAuto);
		chooser.addObject("BaseLineAuto", backUpPlan);
		chooser.addObject("Right Start" , rightStart);
		chooser.addObject("Left Start", leftStart);
		chooser.addObject("Mid Start", midStart);
		System.out.println("Auto selected: " + autoSelected);

	}

	@Override
	public void autonomousPeriodic() {
		switch (autoSelected) {
		case midStart:
			// Put Mid Start code here
			erroll.drive(1.0,0.0);;
			break;
		case leftStart:
			//Put Left Start Code here
			
			break;
		case rightStart:
			//Put Right Start Code here
			
			break;
		default:
			// Put default auto code here
			
			erroll.drive(-0.6,0.0);
			Timer.delay(7.0);
			
			break;
			
		}
	}

	@Override
	public void teleopPeriodic() {
		smartBoardData();
		switchDrive();
		xboxDrive();
		highGoalSucks();
		iceClimbers();
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {

	}
	
	public void pidWrite(double output){
		
	}	
	
	
	private void xboxDrive(){    		//system for determining which speed desired. Joystick.getRawAxis(Axis Number)*Speed[Range:0-1.0]
		if (forwardDrive){
			if ((xboxController.getRawButton(5)) ) { 
				erroll.tankDrive(-xboxController.getRawAxis(5) * -0.5, -xboxController.getRawAxis(1) * -0.5, true);
			}
			else if (xboxController.getRawButton(6)){		//HIT THE NOS
				erroll.tankDrive(-xboxController.getRawAxis(5) * -0.5, -xboxController.getRawAxis(1) * -0.5, true);
			}
			else {
				erroll.tankDrive(xboxController.getRawAxis(5) * 0.7, xboxController.getRawAxis(1) * 0.7, true);
			}
		}
		else{
			if ((xboxController.getRawButton(5)) ) { 
				erroll.tankDrive(-xboxController.getRawAxis(1) * 0.5, -xboxController.getRawAxis(5) * 0.5, true);
				}
			else if (xboxController.getRawButton(6)){		//HIT THE NOS
				erroll.tankDrive(-xboxController.getRawAxis(1) * 0.85, -xboxController.getRawAxis(5) * 0.85, true);
				}
			else {
				erroll.tankDrive(-xboxController.getRawAxis(1) * 0.7, -xboxController.getRawAxis(5) * 0.7, true);
				}
			}
	}
	
	private void switchDrive(){
		if (xboxController.getRawButton(4)){
			if (!reverseDrive){
			forwardDrive=!forwardDrive;
			reverseDrive=true;
			}
		}
			else{
				reverseDrive=false;
			}
	}
	

	
	
	private void highGoalSucks(){ 	// method for dumping balls
		
	}
	
	private void smartBoardData(){
		SmartDashboard.putBoolean("Forward Mode", forwardDrive);
		
	}
	
	private void iceClimbers(){ //method for climbing onto the ship
		
	}

}

