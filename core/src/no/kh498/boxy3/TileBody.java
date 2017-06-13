package no.kh498.boxy3;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;

/**
 * @author karl henrik
 * @since 0.1.0
 */
public class TileBody {

    private final Tile tile;
    private final int x;
    private final int y;
    private final int amounts;

    public TileBody(final char tileIndex, final int x, final int y, final int amounts) {
        this(Tile.fromMapChar(tileIndex), x, y, amounts);
    }

    public TileBody(final Tile tile, final int x, final int y, final int amounts) {
        this.tile = tile;
        this.x = x;
        this.y = y;
        this.amounts = amounts;
        if (tile == Tile.AIR) {
            return;
        }

        // Create our body definition
        final BodyDef playerBodyDef = new BodyDef();
        // Set its world position
        playerBodyDef.position.set(new Vector2(x + amounts, y - BoxyMain.TILE_RESOLUTION / 2));


        // Create a body from the definition and add it to the world
        final Body playerBody = BoxyMain.world.createBody(playerBodyDef);

        // Create a polygon shape
        final PolygonShape playerBox = new PolygonShape();

        playerBox.setAsBox(amounts, BoxyMain.TILE_RESOLUTION / 2);
        // Create a fixture from our polygon shape and add it to our ground body
        final Fixture fixture = playerBody.createFixture(playerBox, 0.0f);
        fixture.setUserData(tile);

        fixture.setSensor(tile.isSensor());


        // Clean up after ourselves
        playerBox.dispose();

    }

    public void draw(final Batch batch) {
        if (this.tile.getTexture() != null) {
            for (int i = 1; i <= this.amounts; i++) {
                batch.draw(this.tile.getTexture(), (this.x + BoxyMain.TILE_RESOLUTION) * i,
                           (this.y - BoxyMain.TILE_RESOLUTION), BoxyMain.TILE_RESOLUTION, BoxyMain.TILE_RESOLUTION);
            }

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

    @Override
    public String toString() {
        return "TileBody{" + "tile=" + this.tile + ", x=" + this.x + ", y=" + this.y + ", amounts=" + this.amounts +
               '}';
    }
}
