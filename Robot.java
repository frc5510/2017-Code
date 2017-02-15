package org.usfirst.frc.team5510.robot;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Spark;
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
 SendableChooser <String> autoChooser;
 
 Timer autoTimerTing;
 
 RobotDrive erroll;
 
 double autoStartTime;
 
 //spm  eed controllers, find the ports on the roboRIO
 Victor rightFront;
 Victor rightBack;
 Victor leftFront;
 Victor leftBack;
 
 Spark iceClimber;
 
 Talon ballIntakeOne;
 Talon ballIntakeTwo;
 
 Servo ballIntake1;
 
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
  autoChooser = new SendableChooser<>();
  //autoChooser.addDefault("Default Auto", defaultAuto);
  //chooser.addObject("My Auto", customAuto);
  autoChooser.addObject("BaseLineAuto", backUpPlan);
  autoChooser.addObject("Right Start" , rightStart);
  autoChooser.addObject("Left Start", leftStart);
  autoChooser.addObject("Mid Start", midStart);
  SmartDashboard.putData("Auto modes", autoChooser);
  
  xboxController = new Joystick(0);
  
  reverseDrive=false;
  forwardDrive=true;
  
  leftFront = new Victor (1); //port 1
  leftBack = new Victor (2); //port 2
  rightFront = new Victor (3); //port 3
  rightBack = new Victor (4); //port 4
  
  ballIntake1 = new Servo (6);
  
  //ballIntakeOne = new Talon (6);
  ballIntakeTwo = new Talon (7);
  
  iceClimber = new Spark (5);
  
  erroll = new RobotDrive(leftFront, leftBack, rightFront, rightBack); 
  
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
  autoChooser.addObject("BaseLineAuto", backUpPlan);
  autoChooser.addObject("Right Start" , rightStart);
  autoChooser.addObject("Left Start", leftStart);
  autoChooser.addObject("Mid Start", midStart);
  autoSelected = (String) autoChooser.getSelected();
  autoStartTime = Timer.getFPGATimestamp();
  Timer t = new Timer ();
  t.start();
 }
 @Override
 public void autonomousPeriodic() {
  
  double currTime = Timer.getFPGATimestamp();
  double timeElapsed = currTime - autoStartTime;
  erroll.setSafetyEnabled(false);
  switch (autoSelected) {
  case midStart:
   // Put Mid Start code here
   if (timeElapsed <2.45){
    erroll.drive(-0.2, -0.000003553);
   }
   
   /*else if (timeElapsed < 6){
    erroll.drive(0.0, .0);
   }
   */
   else if (timeElapsed >= 2.5) {
    erroll.drive(0.0, 0.0);
   }

   break;
  case leftStart:
   //Put Left Start Code here
   if (timeElapsed <3){
    erroll.drive(-0.2, -0.000003553); //drives straight
   }
   
   else if (timeElapsed < 6 ){
    erroll.drive(0.0, 0.0); //stops
   }
   
   else if (timeElapsed <7) {
     erroll.drive(0.0, -0.4); //turns to direction of the peg
   }
   
   else if (timeElapsed < 8) {
    erroll.drive(-0.2, -0.000003553); //drives towards peg
   } //be my valentine
   
   else if (timeElapsed < 11 ) {
    erroll.drive(0.0, 0.0);
   }
    
   break;
  case rightStart:
   //Put Right Start Code here
   if (timeElapsed <3){
    erroll.drive(-0.2, -0.000003553); //drives straight 
   }
   
   else if (timeElapsed < 6 ){
    erroll.drive(0.0, 0.0);  //stops
   }
   
   else if (timeElapsed <7){
    erroll.drive(0.0, 0.4); //turns to direction of the side peg
   }
   
   else if (timeElapsed < 8) {
    erroll.drive(-0.2, -0.000003553); //drives to peg
   } //be my valentine 
   
   else if (timeElapsed < 11 ) {
    erroll.drive(0.0, 0.0);
   }
   
   break;
   
  case backUpPlan:
   if (timeElapsed < 3){
    erroll.drive(-0.2, -0.000003553);
   }
   
   else if (timeElapsed > 3) {
    erroll.drive(0.0, 0.0);
   }
   break;
  default:
   // Put default auto code here
   
   break;
   
  }
 }

 
 @Override
 public void teleopPeriodic() {
  erroll.setSafetyEnabled(true);
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
 
 
 private void xboxDrive(){      //system for determining which speed desired. Joystick.getRawAxis(Axis Number)*Speed[Range:0-1.0]
  if (forwardDrive){
   if ((xboxController.getRawButton(5)) ) { 
    erroll.tankDrive(-xboxController.getRawAxis(5) * -0.5, -xboxController.getRawAxis(1) * -0.5, true);
   }
   else if (xboxController.getRawButton(6)){  //HIT THE NOS
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
   else if (xboxController.getRawButton(6)){  //HIT THE NOS
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
 
 
 
 private void highGoalSucks(){  // method for dumping balls
  //ballIntake1.setPosition(0.1);
  ballIntake1.set(0.1);
 }
 
 private void smartBoardData(){
  SmartDashboard.putBoolean("Forward Mode", forwardDrive);
  
 }
 
 private void iceClimbers(){ //method for climbing onto the ship
  if ((xboxController.getRawAxis(2)>0.4) && (xboxController.getRawAxis(3)==0)) {
   iceClimber.set(-0.9); 
  }
  
  if ((xboxController.getRawAxis(2)==0) && (xboxController.getRawAxis(3)>0.4)) {
   iceClimber.set(0.9);
  }
  
  if ((xboxController.getRawAxis(2)==0) && (xboxController.getRawAxis(3)==0)) {
   iceClimber.stopMotor();
  }
 }


} //final bracket
