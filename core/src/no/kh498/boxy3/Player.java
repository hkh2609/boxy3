package no.kh498.boxy3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;

/**
 * @author karl henrik
 * @since 0.1.0
 */
public class Player {

    private final Rectangle rectangle;
    double deltaX;
    double deltaY;
    private float drag;
    private final float movement;
    private final float maxJumpHeightModifier;

    private boolean canJump;
    private boolean onGround;

    public Player() {
        this.rectangle = new Rectangle();
        this.deltaX = 0;
        this.deltaY = 0;
        this.drag = 0.75f;
        this.movement = 200;
        this.maxJumpHeightModifier = 2.5f;
    }

    void updatePlayerLocation() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            this.deltaX -= this.movement * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            this.deltaX += this.movement * Gdx.graphics.getDeltaTime();
        }
        if (isJumpKeyPressed()) {
            boolean canJump = true;
            //Disabled automatic rejumping when hitting the ground
            if (this.onGround && !isJumpKeyJustPressed()) {
                canJump = false;
                this.setOnGround(false);
            }
            //Make sure you cannot spam jump key to stay in the air
            else if (!this.onGround && isJumpKeyJustPressed()) {
                this.setCanJump(false);
            }

            if (this.canJump() && canJump) {
                //JUMP!
                this.deltaY += (BoxyMain.HEIGHT - this.rectangle.y) * Gdx.graphics.getDeltaTime();
                //do not add forward momentum when above a certain y value
                if (this.rectangle.y > BoxyMain.GROUND_LEVEL * this.maxJumpHeightModifier) {
                    this.setCanJump(false);
                }
            }
        }
        //gravity
        if (this.rectangle.y > BoxyMain.GROUND_LEVEL) {
            this.deltaY -= this.rectangle.y * Gdx.graphics.getDeltaTime();
        }
        //lowest y value is ground level
        else {
            this.rectangle.y = BoxyMain.GROUND_LEVEL;
            this.setCanJump(true);
            this.setOnGround(true);
        }

        // make sure the player stays within the screen bounds
        if (this.rectangle.x < 0) {
            this.rectangle.x = 0;
        }
        if (this.rectangle.x > BoxyMain.WIDTH - BoxyMain.TILE_RESOLUTION) {
            this.rectangle.x = BoxyMain.WIDTH - BoxyMain.TILE_RESOLUTION;
        }
        if (this.rectangle.y > BoxyMain.HEIGHT - BoxyMain.TILE_RESOLUTION) {
            this.rectangle.y = BoxyMain.HEIGHT - BoxyMain.TILE_RESOLUTION;
        }
    }

    private boolean isJumpKeyPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W);
    }

    private boolean isJumpKeyJustPressed() {
        return Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W);
    }

    public Rectangle getRectangle() {
        return this.rectangle;
    }
    public double getDrag() {
        return this.drag;
    }
    public void setDrag(final float drag) {
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
    public float getMovement() {
        return this.movement;
    }
    public float getMaxJumpHeightModifier() {
        return this.maxJumpHeightModifier;
    }
}
