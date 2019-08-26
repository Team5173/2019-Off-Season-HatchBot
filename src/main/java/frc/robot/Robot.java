package frc.robot;

//Other Imports From FRC Libraries
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.RobotController;

//Camera Related Imports
import edu.wpi.cscore.VideoSource;
import edu.wpi.cscore.VideoMode.PixelFormat;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;
import edu.wpi.first.cameraserver.CameraServer;

//Pneumatic Imports
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Compressor;

//imports from Rev Robotics
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Robot extends TimedRobot {
  DifferentialDrive myDrive;
  //grabberPush
  boolean rTriggerPressed;
  //Hatch Grabber
  boolean lTriggerPressed;

  boolean leftBumperPressed;

  boolean rightBumperPressed;

  CANSparkMax frontLeft, backLeft, frontRight, backRight;

  XboxController Controller;

  UsbCamera Camera1;
  UsbCamera Camera2;
  VideoSink Server;

  PowerDistributionPanel powerPanel;

  private Compressor C;
  private DoubleSolenoid frontLifter, backLifter, hatchGrabber, grabberPush;

  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  public void robotInit() {

    //Camera 1
    Camera1 = CameraServer.getInstance().startAutomaticCapture();
    Camera1.setConnectionStrategy(VideoSource.ConnectionStrategy.kAutoManage);
    Camera1.setVideoMode(PixelFormat.kMJPEG, 320, 240, 8);
    Camera1.setBrightness(30);

    //Camera 2
    Camera2 = CameraServer.getInstance().startAutomaticCapture();
    Camera2.setConnectionStrategy(VideoSource.ConnectionStrategy.kAutoManage);
    Camera2.setVideoMode(PixelFormat.kMJPEG, 320, 240, 8);
    Camera2.setBrightness(30);

    powerPanel = new PowerDistributionPanel();

    //Drive System
    frontLeft = new CANSparkMax(4, MotorType.kBrushless);
    backLeft = new CANSparkMax(1, MotorType.kBrushless);
    frontRight = new CANSparkMax(2, MotorType.kBrushless);
    backRight = new CANSparkMax(3, MotorType.kBrushless);

    SpeedControllerGroup leftDrive = new SpeedControllerGroup(frontLeft, backLeft);
    SpeedControllerGroup rightDrive = new SpeedControllerGroup(frontRight, backRight);
    
    myDrive = new DifferentialDrive(leftDrive, rightDrive);

    //Xbox Controller
    Controller = new XboxController(0);

    //Pnuematics
    frontLifter = new DoubleSolenoid(0, 1);
    backLifter = new DoubleSolenoid(2, 3);
    hatchGrabber = new DoubleSolenoid(4, 5);
    grabberPush = new DoubleSolenoid(6, 7);

    C = new Compressor();

    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    
  }
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    System.out.println("Auto selected: " + m_autoSelected);
    C.start();
    grabberPush.set(Value.kReverse);
  }

  public void autonomousPeriodic() {
    teleop();
    }

  public void teleopInit() {
    super.teleopInit();
    C.start();
  }

  public void robotPeriodic() {
  }

  public void teleop() {
    myDrive.arcadeDrive(Controller.getRawAxis(1)*-0.7, Controller.getRawAxis(0)*0.7);

    //This pulls controller axis value
    if(Controller.getRawButton(8)){
      //Determines if this line of code is not true
      if(rTriggerPressed != true){
        //in the case that the past line of code was not true then set true
        rTriggerPressed = true;
        //If the value was pulled forward then set the Grabber to reverse and vice versa
        if(frontLifter.get() == Value.kForward){
            frontLifter.set(Value.kReverse);
        }
        else {
          frontLifter.set(Value.kForward);
        }
      }
      //Everything is determined false if the previous things were alread false
    } else{
      rTriggerPressed = false;
    }

        //This pulls controller axis value
        if(Controller.getBumper(Hand.kLeft)){
          //Determines if this line of code is not true
          if(leftBumperPressed != true){
            //in the case that the past line of code was not true then set true
            leftBumperPressed = true;
            //If the value was pulled forward then set the Grabber to reverse and vice versa
            if(grabberPush.get() == Value.kForward){
                grabberPush.set(Value.kReverse);
            }
            else {
              grabberPush.set(Value.kForward);
            }
          }
          //Everything is determined false if the previous things were alread false
        } else{
          leftBumperPressed = false;
        }

        //This pulls controller axis value
        if(Controller.getRawButton(7)){
          //Determines if this line of code is not true
          if(rightBumperPressed != true){
            //in the case that the past line of code was not true then set true
            rightBumperPressed = true;
            //If the value was pulled forward then set the Grabber to reverse and vice versa
            if(backLifter.get() == Value.kForward){
                backLifter.set(Value.kReverse);
            }
            else {
              backLifter.set(Value.kForward);
            }
          }
          //Everything is determined false if the previous things were alread false
        } else{
          rightBumperPressed = false;
        }

    //This pulls controller axis value
    if(Controller.getBumper(Hand.kRight)){
      //Determines if this line of code is not true
      if(lTriggerPressed != true){
        //in the case that the past line of code was not true then set true
        lTriggerPressed = true;
        //If the value was pulled forward then set the Grabber to reverse and vice versa
        if(hatchGrabber.get() == Value.kForward){
            hatchGrabber.set(Value.kReverse);
        }
        else {
          hatchGrabber.set(Value.kForward);
        }
      }
      //Everything is determined false if the previous things were alread false
    } else{
      lTriggerPressed = false;
    }

    SmartDashboard.putNumber("frontLeftRPM", frontLeft.getEncoder().getVelocity());
    SmartDashboard.putNumber("frontRightRPM", frontRight.getEncoder().getVelocity());
    SmartDashboard.putNumber("backLeftRPM", backLeft.getEncoder().getVelocity());
    SmartDashboard.putNumber("backRightRPM", backRight.getEncoder().getVelocity());

    SmartDashboard.putBoolean("hatchGrabberState", (hatchGrabber.get() == Value.kForward));
    SmartDashboard.putBoolean("grabberPushState", (grabberPush.get() == Value.kForward));

    SmartDashboard.putNumber("Roborio Voltage", RobotController.getBatteryVoltage());
    SmartDashboard.putNumber("PDP Battery Voltage", powerPanel.getVoltage());

    }


  public void teleopPeriodic() {
    teleop();
  }

  public void testPeriodic() {
  }
}

