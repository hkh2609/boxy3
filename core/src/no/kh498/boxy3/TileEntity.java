package no.kh498.boxy3;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

/**
 * @author karl henrik
 * @since 0.1.0
 */
public class TileEntity {

    private final Tile tile;
    private final int x;
    private final int y;

    public TileEntity(final int tileIndex, final int x, final int y) {
        this(Tile.fromIndex(tileIndex), x, y);
    }

    public TileEntity(final Tile tile, final int x, final int y) {
        this.tile = tile;
        this.x = x;
        this.y = y;

        // Create our body definition
        final BodyDef playerBodyDef = new BodyDef();
        // Set its world position
        playerBodyDef.position.set(new Vector2(x - BoxyMain.TILE_RESOLUTION / 2, y - BoxyMain.TILE_RESOLUTION / 2));


        // Create a body from the definition and add it to the world
        final Body playerBody = BoxyMain.world.createBody(playerBodyDef);

        // Create a polygon shape
        final PolygonShape playerBox = new PolygonShape();

        playerBox.setAsBox(BoxyMain.TILE_RESOLUTION / 2, BoxyMain.TILE_RESOLUTION / 2);
        // Create a fixture from our polygon shape and add it to our ground body
        playerBody.createFixture(playerBox, 0.0f);
        // Clean up after ourselves
        playerBox.dispose();
    }

    public void draw(final Batch batch) {
        if (this.tile.getTexture() != null) {
            batch.draw(this.tile.getTexture(), this.x - BoxyMain.TILE_RESOLUTION, this.y - BoxyMain.TILE_RESOLUTION,
                       BoxyMain.TILE_RESOLUTION, BoxyMain.TILE_RESOLUTION);
        }
    }

    public Tile getTile() {
        return this.tile;
    }
    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }
}
