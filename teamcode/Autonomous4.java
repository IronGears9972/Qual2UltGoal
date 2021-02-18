package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This file illustrates the concept of driving a path based on time.
 * It uses the common Pushbot hardware class to define the drive on the robot.
 * The code is structured as a LinearOpMode
 *
 * The code assumes that you do NOT have encoders on the wheels,
 *   otherwise you would use: PushbotAutoDriveByEncoder;
 *
 *   The desired path in this example is:
 *   - Drive forward for 3 seconds
 *   - Spin right for 1.3 seconds
 *   - Drive Backwards for 1 Second
 *   - Stop and close the claw.
 *
 *  The code is written in a simple form with no optimizations.
 *  However, there are several ways that this type of sequence could be streamlined,
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="Autonomous4", group="Pushbot")

public class Autonomous4 extends LinearOpMode {

    /* Declare OpMode members. */
    Hardware_20_21 robot = new Hardware_20_21(); // use the class created to define a robot's hardware
    private ElapsedTime     runtime = new ElapsedTime();




    @Override
    public void runOpMode() {

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */

        // Send telemetry message to signify robot waiting;
        
        robot.init(hardwareMap, this);

        robot.frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rearLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rearRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.intakemotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.launcher1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.frontLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.frontRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.rearLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.rearRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.intakemotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.launcher1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        robot.wobblehand2.setPosition(0.35);
        robot.drivestraight(-25, 0.3);

        robot.launcher1.setPower(0.74);
        sleep(1100);

        robot.launcher1.setPower(0.74);

        robot.conveyor.setPower(1);

        robot.forks.setPosition(0.24);
        sleep(250);

        robot.kicker.setPosition(0.6);
        sleep(250);

        robot.kicker.setPosition(1);
        sleep(50);

        robot.conveyor.setPower(1);
        sleep(750);

        robot.forks.setPosition(0.30);
        sleep(350);

        robot.kicker.setPosition(0.6);
        sleep(50);

        robot.conveyor.setPower(1);
        sleep(1300);

        robot.launcher1.setPower(0);
        robot.conveyor.setPower(0);

        robot.forks.setPosition(0.05);
        sleep(350);

        robot.kicker.setPosition(1);
        sleep(100);
        robot.robotsleep(0);
        sleep(300);
        //------------------------------------------------------------------------------------------
        //STEP 2
        // Drive turn and drop the wobble
        robot.drivestrafe(-8,0.5);
        robot.robotsleep(0);
        sleep(100);

        robot.intakemotor.setPower(-0.80);
        robot.intakeservo.setPower(0.8);

        robot.drivestraight(-11, 0.1);

        robot.robotsleep(0);
        sleep(200);

        robot.drivestrafe(8,0.5);
        sleep(1800);

        robot.intakemotor.setPower(0);
        robot.intakeservo.setPower(0);

        robot.launcher1.setPower(0.74);
        sleep(1000);

        robot.launcher1.setPower(0.74);

        robot.forks.setPosition(0.24);
        sleep(250);

        robot.kicker.setPosition(0.6);
        sleep(250);

        robot.kicker.setPosition(1);
        sleep(50);

        robot.conveyor.setPower(1);
        sleep(750);

        robot.forks.setPosition(0.30);
        sleep(350);

        robot.kicker.setPosition(0.6);
        sleep(50);

        robot.conveyor.setPower(1);

        robot.robotsleep(0);
        sleep(1200);

        robot.forks.setPosition(0.05);

        robot.launcher1.setPower(0);
        robot.conveyor.setPower(0);


        robot.drivestrafe(-8,0.5);
        robot.robotsleep(0);
        sleep(100);

        robot.intakemotor.setPower(-0.80);
        robot.intakeservo.setPower(0.8);

        robot.drivestraight(-11, 0.1);

        robot.robotsleep(0);
        sleep(200);

        robot.drivestrafe(8,0.5);
        sleep(1800);

        robot.intakemotor.setPower(0);
        robot.intakeservo.setPower(0);

        robot.launcher1.setPower(0.74);
        sleep(1000);

        robot.launcher1.setPower(0.74);

        robot.forks.setPosition(0.24);
        sleep(250);

        robot.kicker.setPosition(0.6);
        sleep(250);

        robot.kicker.setPosition(1);
        sleep(50);

        robot.conveyor.setPower(1);
        sleep(750);

        robot.forks.setPosition(0.30);
        sleep(350);

        robot.kicker.setPosition(0.6);
        sleep(50);

        robot.conveyor.setPower(1);

        robot.robotsleep(0);
        sleep(1200);


        robot.launcher1.setPower(0);
        robot.conveyor.setPower(0);

        robot.drivestraight(-114,0.3);

        robot.robotsleep(0);
        sleep(200);

        robot.drivestrafe(6,0.5);

        robot.wobble2.setPosition(0.15);
        sleep(500);

        robot.wobblehand2.setPosition(1);

        robot.robotsleep(0);
        sleep(200);

        robot.drivestraight(50,1);


    }
}
