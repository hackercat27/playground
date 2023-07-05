package ca.hackercat.playground.io;

import com.github.strikerx3.jxinput.XInputAxes;
import com.github.strikerx3.jxinput.XInputButtons;
import com.github.strikerx3.jxinput.XInputComponents;
import com.github.strikerx3.jxinput.XInputDevice;
import com.github.strikerx3.jxinput.exceptions.XInputNotLoadedException;

import static ca.hackercat.playground.PlaygroundPanel.LOGGER;

/**
 * Implementation of a key listener that provides simple methods to get input from an XInput device.
 */
public class PGXInputListener {
    private PGXInputListener() throws XInputNotLoadedException {
        device = XInputDevice.getDeviceFor(0);
    }

    public void unstickButtons() {

    }

//    public boolean isButtonPressed(Button button) {
//    }

    /**
     * A list of XInput buttons.
     */
    public enum Button {
        A, B, X, Y,
        BACK, START,
        L_SHOULDER, R_SHOULDER, L_THUMB, R_THUMB,
        UP, DOWN, LEFT, RIGHT,
        GUIDE, L_TRIGGER, R_TRIGGER, UNKNOWN
    }

    /**
     * A list of XInput axes (control stick directions).
     */
    public enum Axis {
        LEFT_Y, LEFT_X,
        RIGHT_Y, RIGHT_X
    }

    private XInputDevice device;

    private static PGXInputListener instance;
    public static PGXInputListener get() {
        if (instance == null) {
            try {
                instance = new PGXInputListener();
            }
            catch (XInputNotLoadedException e) {
                LOGGER.error("XInput is not loaded!");
                instance = null;
            }
        }
        return instance;
    }

    public boolean isButtonHeld(Button button) {
        if (!device.poll()) return false;
        XInputComponents components = device.getComponents();

        XInputButtons buttons = components.getButtons();
        XInputAxes axes = components.getAxes();


        return switch (button) {
            case A -> buttons.a;
            case B -> buttons.b;
            case X -> buttons.x;
            case Y -> buttons.y;
            case BACK -> buttons.back;
            case START -> buttons.start;
            case L_SHOULDER -> buttons.lShoulder;
            case R_SHOULDER -> buttons.rShoulder;
            case L_THUMB -> buttons.lThumb;
            case R_THUMB -> buttons.rThumb;
            case UP -> buttons.up;
            case DOWN -> buttons.down;
            case LEFT -> buttons.left;
            case RIGHT -> buttons.right;
            case GUIDE -> buttons.guide;
            case L_TRIGGER -> axes.ltRaw != 0;
            case R_TRIGGER -> axes.rtRaw != 0;
            case UNKNOWN -> buttons.unknown;
        };
    }

    public float getAxis(Axis axis) {
        if (!device.poll()) return 0;
        XInputComponents components = device.getComponents();
        XInputAxes axes = components.getAxes();

        return applyDeadZone(switch (axis) {
            case LEFT_Y -> -axes.ly;
            case LEFT_X -> axes.lx;
            case RIGHT_Y -> -axes.ry;
            case RIGHT_X -> axes.rx;
        });

    }

    private float stickDeadZone = 0.1f;
    private float applyDeadZone(double axis) {
        double magnitude = Math.max(Math.abs(axis) - stickDeadZone, 0) / (1 - stickDeadZone);
        return (float) Math.copySign(magnitude, axis);
    }
}
