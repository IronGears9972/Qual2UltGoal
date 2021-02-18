/* Copyright (c) 2019 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;
import java.util.Locale;

@Autonomous(name = "Concept: TensorFlow Object Detection Webcam", group = "Concept")
public class ConceptTensorFlowObjectDetectionWebcam extends LinearOpMode {
    private static final String TFOD_MODEL_ASSET = "UltimateGoal.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Quad";
    private static final String LABEL_SECOND_ELEMENT = "Single";

    /* Declare OpMode members. */
    Hardware_20_21 robot = new Hardware_20_21(); // use the class created to define a robot's hardware
    private ElapsedTime runtime = new ElapsedTime();


    static final double FORWARD_SPEED = 0.6;
    static final double TURN_SPEED = 0.5;
    /*
     * IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
     * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
     * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
     * web site at https://developer.vuforia.com/license-manager.
     *
     * Vuforia license keys are always 380 characters long, and look as if they contain mostly
     * random data. As an example, here is a example of a fragment of a valid key:
     *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
     * Once you've obtained a license key, copy the string from the Vuforia web site
     * and paste it in to your code on the next line, between the double quotes.
     */
    private static final String VUFORIA_KEY =
            "Abvr6pj/////AAABmXhem3EDYkB2l9ldqIzfhPY1eAddpO0PM4c17wFb6y3q8p1VMAJM35J7Bt+HKLFUV0ibDyiAYunShwlSyug/FKJpY+GG7pYUaEx44wkOWFkTSGCoE8lcZO4J55tv9rLsqIU0adRZDCkHrUMeV7zQecbuAqnQ+3/iBPdzgNm8trMkaCJ/k/ulJw5uAraNQC25kaom1XGWx6vlftXyTGszAfvFHVvShrn3JBuf5ZYEkIAZ6wtiYoxss3gROnkdGbI7kW2pD1jZdey39WNrHDXSS0Wt4SLl0O0BgFskK+eXAIKxy8f8fgPXauk8fNT4+ZUTus+gBt8xZ6Xs/A8O5CpC4l8sp0w4c/gZW7DpNSsU+Qrk";

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;


    @Override
    public void runOpMode() {
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia();
        initTfod();
        List<Recognition> updatedRecognitions = tfod.getRecognitions();
        String readValue = "none";

        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         **/
        if (tfod != null) {
            tfod.activate();

            // The TensorFlow software will scale the input images from the camera to a lower resolution.
            // This can result in lower detection accuracy at longer distances (> 55cm or 22").
            // If your target is at distance greater than 50 cm (20") you can adjust the magnification value
            // to artificially zoom in to the center of image.  For best results, the "aspectRatio" argument
            // should be set to the value of the images used to create the TensorFlow Object Detection model
            // (typically 1.78 or 16/9).

            // Uncomment the following line if you want to adjust the magnification and/or the aspect ratio of the input images.
            //tfod.setZoom(2.5, 1.78);
        }

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();
        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {
                if (tfod != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.

                    updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        readValue = UpdateDisplay(updatedRecognitions, readValue);
                        //RunFromReadValue(readValue);
                    }
                }
            }
        }


        if (tfod != null) {
            tfod.shutdown();
        }
    }

    private String UpdateDisplay(List<Recognition> updatedRecognitions, String readValue) {
        telemetry.addData("# Object Detected", updatedRecognitions.size());
        // step through the list of recognitions and display boundary info.
        int i = 0;
        for (Recognition recognition : updatedRecognitions) {
            readValue = recognition.getLabel();
            telemetry.addData(String.format("label (%d)", i), readValue);
            telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f", recognition.getLeft(), recognition.getTop());
            telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f", recognition.getRight(), recognition.getBottom());
        }
        telemetry.update();
        return readValue;
    }

    private void RunFromReadValue(String readValue) {
        if (readValue.equals(LABEL_SECOND_ELEMENT)) {
            AutoOne();
        } else if (readValue.equals(LABEL_FIRST_ELEMENT)) {
            AutoFour();
        } else {
            AutoZero();
        }
    }

    private void AutoOne() {

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */

        // Send telemetry message to signify robot waiting;

        float hsvValues[] = {0F, 0F, 0F};


        final float values[] = hsvValues;



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

        while (!opModeIsActive()) {




            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Vertical left encoder position", robot.frontRightMotor.getCurrentPosition());
            telemetry.addData("Vertical right encoder position", robot.rearLeftMotor.getCurrentPosition());
            telemetry.addData("horizontal encoder position", robot.rearRightMotor.getCurrentPosition());
            telemetry.addData("intake", " %d", robot.intakemotor.getCurrentPosition());
            telemetry.addData("launcher1", " %.0f", robot.launcher1.getVelocity() / 28 * 60);
            telemetry.addData("DSRearLeft", String.format("%.01f in", robot.DSRearLeft.getDistance(DistanceUnit.INCH)));
            telemetry.addData("DSLeftBack", String.format("%.01f in", robot.DSLeftBack.getDistance(DistanceUnit.INCH)));
            telemetry.addData("DSLeftFront", String.format("%.01f in", robot.DSLeftFront.getDistance(DistanceUnit.INCH)));
            telemetry.addData("DSRearRight", String.format("%.01f in", robot.DSRearRight.getDistance(DistanceUnit.INCH)));
            Orientation angels = robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            telemetry.addData("imu , first/second/third", "%.1f %.1f %.1f", angels.firstAngle, angels.secondAngle, angels.thirdAngle);
            telemetry.addData("Distance (cm)", String.format(Locale.US, "%.02f", robot.sensorDistance.getDistance(DistanceUnit.CM)));
            telemetry.addData("Alpha", robot.sensorColor.alpha());
            telemetry.addData("Red  ", robot.sensorColor.red());
            telemetry.addData("Green", robot.sensorColor.green());
            telemetry.addData("Blue ", robot.sensorColor.blue());
            telemetry.addData("Hue", hsvValues[0]);
            telemetry.update();


        }

        //STEP 1
        // Drive to the ring and shot

        robot.wobblehand2.setPosition(0.35);
        robot.drivestraight(-25, 0.3);

        robot.robotsleep(0);
        sleep(200);
        //------------------------------------------------------------------------------------------
        //STEP 2
        // Drive turn and drop the wobble
        robot.drivestrafe(-8,0.3);

        robot.launcher1.setPower(0.72);
        sleep(1100);

        robot.launcher1.setPower(0.72);

        robot.conveyor.setPower(1);

        robot.forks.setPosition(0.30);
        robot.kicker.setPosition(0.6);
        sleep(575);

        robot.forks.setPosition(0.05);
        sleep(250);

        robot.kicker.setPosition(1);
        sleep(250);

        robot.robotsleep(0);
        robot.conveyor.setPower(0);
        sleep(200);

        robot.intakemotor.setPower(-0.85);
        robot.intakeservo.setPower(0.8);


        robot.drivestraight(-12, 0.3);
        sleep(500);

        robot.robotsleep(0);
        sleep(300);

        robot.drivestraight(-22, 0.3);

        robot.intakemotor.setPower(0);
        robot.intakeservo.setPower(0);
        robot.launcher1.setPower(0.72);
        sleep(1100);

        robot.launcher1.setPower(0.72);

        robot.conveyor.setPower(1);
        robot.forks.setPosition(0.24);
        sleep(75);

        robot.kicker.setPosition(0.6);
        sleep(175);

        robot.kicker.setPosition(1);
        sleep(600);

        robot.forks.setPosition(0.30);
        sleep(300);

        robot.kicker.setPosition(0.6);
        sleep(50);

        robot.conveyor.setPower(1);
        sleep(1150);

        robot.forks.setPosition(0.05);

        robot.kicker.setPosition(1);

        robot.launcher1.setPower(0);
        robot.conveyor.setPower(0);

        robot.robotsleep(0);
        sleep(1200);

        robot.drivestraight(-50, 0.3);

        robot.robotsleep(0);
        sleep(200);

        robot.drivestrafe(-10,0.3);

        robot.robotsleep(0);
        sleep(200);

        robot.wobble2.setPosition(0.15);
        sleep(500);

        robot.wobblehand2.setPosition(1);

        robot.robotsleep(0);
        sleep(200);

        //=============================================

        robot.drivestrafe(-17,0.3);

        robot.robotsleep(0);
        sleep(200);

        robot.drivestraight(111,0.35);

        robot.robotsleep(0);
        sleep(200);

        robot.drivestrafe(3,0.4);

        //  robot.drivestrafe(9,0.3);

        robot.wobblehand2.setPosition(0.35);
        sleep(550);

        robot.wobble2.setPosition(0.25);
        sleep(350);

        robot.drivestraight(-98,0.55);

        robot.drivestrafe(17,0.8);

        robot.wobblehand2.setPosition(1);
        sleep(400);

        robot.drivestraight(17,1);


    }
    //              ^ 0 Ring

    private void AutoZero() {

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */

        // Send telemetry message to signify robot waiting;

        robot.init(hardwareMap, this);

        OpRunENCODERZero();


        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        //  going strait is - and turning is counterclockwise
        robot.wobblehand2.setPosition(0.35);
        robot.drivestraight(-65,0.3);

        OpRunShooterZero();

        robot.drivestraight(-18,0.3);

        new ConceptTensorFlowObjectDetectionWebcam.OpRunDropWobble2AndPutDoneWobble1Zero().invoke();

        robot.drivestraight(84,0.3);

        robot.drivestrafe(-12.25,0.3);

        robot.robotsleep(0);
        sleep(500);

        robot.wobblehand.setPosition(0);
        sleep(800);

        robot.wobble.setPosition(0.8);
        sleep(500);

        robot.robotsleep(0);
        sleep(500);

        robot.drivestraight(-95,0.35);

        robot.trunangel(86,0.3);

        robot.drivestraight(25,0.3);

        new ConceptTensorFlowObjectDetectionWebcam.OpRunDropWobble1Zero().invoke();

        robot.drivestraight(-18,1);

        robot.trunangel(-60,1);

        robot.drivestraight(16,1);

        OpRunTelemetryZero();


    }
    //              ^ 1 Ring

    private void AutoFour() {
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


        while (!opModeIsActive()) {




            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Vertical left encoder position", robot.frontRightMotor.getCurrentPosition());
            telemetry.addData("Vertical right encoder position", robot.rearLeftMotor.getCurrentPosition());
            telemetry.addData("horizontal encoder position", robot.rearRightMotor.getCurrentPosition());
            telemetry.addData("intake", " %d", robot.intakemotor.getCurrentPosition());
            telemetry.addData("launcher1", " %.0f", robot.launcher1.getVelocity() / 28 * 60);
            telemetry.addData("DSRearLeft", String.format("%.01f in", robot.DSRearLeft.getDistance(DistanceUnit.INCH)));
            telemetry.addData("DSLeftBack", String.format("%.01f in", robot.DSLeftBack.getDistance(DistanceUnit.INCH)));
            telemetry.addData("DSLeftFront", String.format("%.01f in", robot.DSLeftFront.getDistance(DistanceUnit.INCH)));
            telemetry.addData("DSRearRight", String.format("%.01f in", robot.DSRearRight.getDistance(DistanceUnit.INCH)));
            Orientation angels = robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            telemetry.addData("imu , first/second/third", "%.1f %.1f %.1f", angels.firstAngle, angels.secondAngle, angels.thirdAngle);
            telemetry.addData("Distance (cm)", String.format(Locale.US, "%.02f", robot.sensorDistance.getDistance(DistanceUnit.CM)));
            telemetry.addData("Alpha", robot.sensorColor.alpha());
            telemetry.addData("Red  ", robot.sensorColor.red());
            telemetry.addData("Green", robot.sensorColor.green());
            telemetry.addData("Blue ", robot.sensorColor.blue());
            telemetry.update();



        }

        //STEP 1
        // Drive to the ring and shot

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
        robot.drivestrafe(6,0.3);
        robot.robotsleep(0);
        sleep(100);

        robot.drivestraight(-110,0.3);

        robot.wobble2.setPosition(0.15);
        sleep(500);

        robot.wobblehand2.setPosition(1);

        robot.wobble.setPosition(1);
        sleep(700);

        robot.wobblehand.setPosition(1);
        sleep(500);

        robot.robotsleep(0);
        sleep(800);

        robot.drivestraight(130,0.3);

        robot.drivestrafe(-40,0.3);

        robot.wobblehand2.setPosition(0.35);
        sleep(550);

        robot.wobble2.setPosition(0.25);
        sleep(350);

        robot.drivestrafe(40,0.3);

        robot.drivestraight(-120,0.3);

        robot.driveturn(180,0.3);

        robot.drivestraight(10,0.3);

        robot.wobble.setPosition(0.15);
        sleep(500);

        robot.wobblehand.setPosition(1);

        robot.robotsleep(0);
        sleep(800);

        robot.driveturn(-180,0.3);

        robot.drivestraight(-30,0.3);

    }
    //              ^ 4 Rings


    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        //TODO: Maybe take this out
        tfodParameters.useObjectTracker = false;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }


    private void OpRunENCODERZero() {
        robot.frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rearLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rearRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.intakemotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.launcher1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rearLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rearRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.intakemotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.launcher1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    //              ^ Starts all Encoders

    private void OpRunShooterZero() {
        robot.launcher1.setPower(0.77);
        sleep(1100);

        robot.launcher1.setPower(0.77);


        robot.conveyor.setPower(1);
        robot.forks.setPosition(0.24);
        sleep(75);

        robot.kicker.setPosition(0.6);
        sleep(175);

        robot.kicker.setPosition(1);
        sleep(600);

        robot.forks.setPosition(0.30);
        sleep(300);

        robot.kicker.setPosition(0.6);
        sleep(50);

        robot.conveyor.setPower(1);
        sleep(1250);


        robot.launcher1.setPower(0);
        robot.conveyor.setPower(0);
    }
    //              ^ Runs the Shooter and Forks

    private void OpRunTelemetryZero() {
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Vertical left encoder position", robot.frontRightMotor.getCurrentPosition());
        telemetry.addData("Vertical right encoder position", robot.rearLeftMotor.getCurrentPosition());
        telemetry.addData("horizontal encoder position", robot.rearRightMotor.getCurrentPosition());
        telemetry.addData("intake", " %d", robot.intakemotor.getCurrentPosition());
        telemetry.addData("launcher1", " %.0f",robot.launcher1.getVelocity()/28*60);
        telemetry.update();
    }
    //              ^ Outputs text so we can read data

    public class OpRunDropWobble1Zero {
        public void invoke() {
            robot.robotsleep(0);
            sleep(100);

            robot.wobble.setPosition(1);
            sleep(500);

            robot.wobblehand.setPosition(1);
            sleep(500);

            robot.wobble.setPosition(0);

            robot.wobblehand.setPosition(0.35);

            robot.robotsleep(0);
            sleep(700);
        }
    }
    //              ^ Drops our 1st wobble goal and opens 2nd wobble arm

    public class OpRunDropWobble2AndPutDoneWobble1Zero {
        public void invoke() {
            robot.robotsleep(0);
            sleep(100);

            robot.wobble2.setPosition(0.15);
            sleep(800);

            robot.drivestrafe(15,-0.3);

            robot.wobblehand2.setPosition(1);
            sleep(500);

            robot.wobble2.setPosition(1);

            robot.wobblehand2.setPosition(0.35);

            robot.robotsleep(0);
            sleep(100);

            robot.wobble.setPosition(1);
            sleep(700);

            robot.wobblehand.setPosition(1);
            sleep(500);

            robot.robotsleep(0);
            sleep(500);
        }
    }
    //              ^ Grabs 2nd wobble goal and drops in square
}
