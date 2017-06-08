package no.kh498.boxy3;

import com.badlogic.gdx.math.Rectangle;

/**
 * @author karl henrik
 * @since 0.1.0
 */
public class Player {

    private final Rectangle rectangle;
    double deltaX;
    double deltaY;
    private double drag;
    private boolean canJump;
    private boolean onGround;
    private final int movement;
    private final float maxJumpHeightModifier;

    public Player() {
        this.rectangle = new Rectangle();
        this.deltaX = 0;
        this.deltaY = 0;
        this.drag = 0.75f;
        this.movement = 200;
        this.maxJumpHeightModifier = 2.5f;
    }

    public Rectangle getRectangle() {
        return this.rectangle;
    }
    public double getDrag() {
        return this.drag;
    }
    public void setDrag(final double drag) {
        if (drag > 1 || drag < -1) {
            throw new IllegalArgumentException("The drag can only be between 1 and -1!");
        }
        this.drag = drag;
    }
    public boolean canJump() {
        return this.canJump;
    }
    public void setCanJump(final boolean canJump) {
        this.canJump = canJump;
    }
    public boolean isOnGround() {
        return this.onGround;
    }
    public void setOnGround(final boolean onGround) {
        this.onGround = onGround;
    }
    public int getMovement() {
        return this.movement;
    }
    public float getMaxJumpHeightModifier() {
        return this.maxJumpHeightModifier;
    }
}
