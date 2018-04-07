/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team1011.robot;

import autonomous.AutonCircle;
import autonomous.AutonLine;
import autonomous.CrossLineAuto;
import autonomous.DriveShootLeft;
import autonomous.DriveShootRight;
import autonomous.ThreeCubeLeftAuto;
import autonomous.FourCubeLeftCustom;
import autonomous.ThreeCubeRightAuto;
import autonomous.FourCubeRightCustom;
import autonomous.LeftSideLeftScore;
import autonomous.MidSideLeftScore;
import autonomous.MidSideRightScore;
import autonomous.MoveArmTest;
import edu.wpi.cscore.MjpegServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode.PixelFormat;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import systems.SysObj;
import systems.Systems;
import systems.subsystems.Collector;
import systems.subsystems.DriveTrain;
import systems.subsystems.Collector;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends IterativeRobot {
	private static final String kDefaultAuto = "Default";
	private static final String kCustomAuto = "My Auto";
	private String m_autoSelected;
	private SendableChooser<String> m_chooser = new SendableChooser<>();
	private static Systems systems;
	private static Collector collector;
	private static DriveTrain driveTrain;
	private static Thread auton;
	private static UsbCamera visionCam;
	private static CameraServer camServer;
	private static final int MJPG_STREAM_PORT = 115200;
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		
		systems = Systems.getInstance();
		collector = systems.getCollector();
		driveTrain = systems.getDriveTrain();
		collector.enable();
		
		systems.resetEncoders();
		systems.update();
		
		SmartDashboard.putString("DB/String 0", "");
		SmartDashboard.putString("DB/String 1", "");
		SmartDashboard.putString("DB/String 2", "");
		SmartDashboard.putString("DB/String 3", "");
		SmartDashboard.putString("DB/String 4", "");
		SmartDashboard.putString("DB/String 5", "");
		SmartDashboard.putString("DB/String 6", "");
		SmartDashboard.putString("DB/String 7", "");
		SmartDashboard.putString("DB/String 8", "");
		SmartDashboard.putString("DB/String 9", "");
		m_chooser.addDefault("Default Auto", kDefaultAuto);
		m_chooser.addObject("My Auto", kCustomAuto);
		SmartDashboard.putData("Auto choices", m_chooser);
		
		visionCam = new UsbCamera("cam0", 1);
		visionCam.setVideoMode(PixelFormat.kYUYV, 320, 240, 15);  // start ObjectDetect	
		visionCam.setResolution(320, 240);
		visionCam.setFPS(15);
		visionCam.setBrightness(900);
		visionCam.setExposureAuto();
		camServer = CameraServer.getInstance();
		camServer.addCamera(visionCam);
		camServer.startAutomaticCapture();
		
	}
	
	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional comparisons to
	 * the switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		
		systems.inAuto = true;
		String gameData = null;
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		
		int i = 3;
		while(gameData == null && i-->0) 
		{
			System.out.print("Game data was null\r\n");
			gameData = DriverStation.getInstance().getGameSpecificMessage();
		}
		System.out.println("robot.autonomousInit()  Game data is " + gameData);
		
		systems.resetAutoSystems();
		
		m_autoSelected = m_chooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto selected: " + m_autoSelected);
		
		systems.instantiate();
		systems.update();
		
		/*
		while (gameData == null) {
			gameData = DriverStation.getInstance().getGameSpecificMessage();
		}*/
		
		switch (m_autoSelected) {
		case (kCustomAuto):
			auton = new Thread(new CrossLineAuto());
		default:
			if (gameData.charAt(0) == 'L') 
				auton = new Thread(new ThreeCubeLeftAuto());
				//auton = new Thread(new DriveShootLeft());
			else 
				auton = new Thread(new ThreeCubeRightAuto());
				//auton = new Thread(new DriveShootRight());

			//System.out.println("No autonomous selected.");
			break;
			
		}
		
		//auton = new Thread(new CrossLineAuto());
		
		//auton = new Thread(new MoveArmTest());
		
		//driveTrain.driveLine(60, 0, 140);
		
		auton.start();
		
		//systems.getDriveTrain().turnTo(90, 0.95, 5500);
		//new AutonLine(systems.getDriveTrain(), systems.getNavX(), 60,140, 0).run();
		//new FourCubeRightAuto().run();
		//driveTrain.turnToOneSide(195, 0.8, 800, true);
		//driveTrain.driveLine(30, 0, 140);
		//driveTrain.driveAuton(-0.5, 300);
		//new AutonCircle(systems.getDriveTrain(), systems.getNavX(), 200, 3, true, false).run();
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		//System.out.println("Angle: " + systems.getNavXAngle());
		//System.out.println("LeftEncoder: " + systems.getEncoderDistance(SysObj.Sensors.LEFT_ENCODER));
		//System.out.println("RightEncoder: " + systems.getEncoderDistance(SysObj.Sensors.RIGHT_ENCODER));
		//System.out.println("Current Angle: " + systems.getNavXAngle());
	}

	public void disabledInit() 
	{
		
	}
	public void disabledPeriodic() 
	{
		while(isDisabled()) 
		{
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void robotPeriodic() 
	{
		
	}
	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		systems.inAuto = false;
		systems.update();
		//systems.printEncoderInfo(true, false, false, SysObj.Sensors.ARM_ENCODER_1);
		//systems.printEncoderInfo(true, false, false, SysObj.Sensors.ARM_ENCODER_2);
		//systems.printEncoderInfo(true, false, false, SysObj.Sensors.LEFT_ENCODER);
		//systems.printEncoderInfo(true, false, false, SysObj.Sensors.RIGHT_ENCODER);
		/*System.out.println("Left Motor: " + systems.getMotorCurrent(10));
		System.out.println("Right Motor: " + systems.getMotorCurrent(11));*/
		//System.out.println("Robot.teleopPeriodic(): Encoder1: " + (systems.getEncoderDistance(SysObj.Sensors.ARM_ENCODER_1)));
		//System.out.println("Robot.teleopPeriodic(): Encoder2: " + (systems.getEncoderDistance(SysObj.Sensors.ARM_ENCODER_2)));
		//systems.printEncoderInfo(true, false, false, SysObj.Sensors.ARM_ENCODER_1);
		//systems.printEncoderInfo(true, false, false, SysObj.Sensors.ARM_ENCODER_2);
		
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
