package frc.robot;

//Other Imports From FRC Libraries
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

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

  CANSparkMax leftDrive, rightDrive;

  XboxController Controller;

  UsbCamera Camera1;
  UsbCamera Camera2;
  VideoSink Server;

  PowerDistributionPanel powerPanel;

  private Compressor C;
  private DoubleSolenoid frontLifter, backLifter, hatchGrabber;

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

    //Drive System
    leftDrive = new CANSparkMax(0, MotorType.kBrushless);
    rightDrive = new CANSparkMax(1, MotorType.kBrushless);
    rightDrive.setInverted(true);
    myDrive = new DifferentialDrive(leftDrive, rightDrive);

    //Xbox Controller
    Controller = new XboxController(0);
    
  }
  public void teleopInit() {
    super.teleopInit();
  }

  public void robotPeriodic() {
  }

  public void teleop() {
    myDrive.arcadeDrive(Controller.getRawAxis(1)*-1, Controller.getRawAxis(0));
  }

  public void teleopPeriodic() {
    teleop();
  }

  public void testPeriodic() {
  }
}

