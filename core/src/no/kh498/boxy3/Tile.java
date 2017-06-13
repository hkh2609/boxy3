package no.kh498.boxy3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import no.kh498.boxy3.tile.AbstractTile;
import no.kh498.boxy3.tile.TileFinishFlag;

import java.util.HashMap;

/**
 * @author karl henrik
 * @since 0.1.0
 */
public enum Tile {
    AIR(-1, 'x', true),
    COBBLESTONE(0, '0', false),
    FINISH_FLAG(1, '1', true, TileFinishFlag.class);


    private final int textureIndex;
    private final char mapChar;
    private final boolean sensor;
    private final Class<? extends AbstractTile> clazz;
    private static final HashMap<Character, Tile> TILE_HASH_MAP = new HashMap<Character, Tile>();

    private final TextureRegion[] textures = BoxyMain.getAllRegions(new Texture(Gdx.files.internal("tile.png")), 4, 4);

    Tile(final int textureIndex, final char mapChar, final boolean sensor) {
        this(textureIndex, mapChar, sensor, null);
    }

    /**
     * @param textureIndex The index of the sprite in the textureAtlas
     * @param mapChar      The character in a *.lvl file will transfer to this tile
     * @param sensor       If the tile should act as a sensor or if it should be a static body
     * @param clazz
     */
    Tile(final int textureIndex, final char mapChar, final boolean sensor, final Class<? extends AbstractTile> clazz) {
        this.textureIndex = textureIndex;
        this.mapChar = mapChar;
        this.sensor = sensor;
        this.clazz = clazz;
    }


    public static Tile fromMapChar(final char mapChar) {
        return TILE_HASH_MAP.get(mapChar);
    }

    public TextureRegion getTexture() {
        if (this.textureIndex == -1) {
            return null;
        }
        return this.textures[this.textureIndex];
    }

    static {
        for (final Tile tile : Tile.values()) {
            if (TILE_HASH_MAP.get(tile.mapChar) != null) {
                throw new IllegalArgumentException("Two tiles have the same textureIndex!");
            }
            TILE_HASH_MAP.put(tile.mapChar, tile);
        }
    }

    public boolean isSensor() {
        return this.sensor;
    }

    AbstractTile tileClazz;

    public AbstractTile getHandler() {
        if (this.clazz == null) {
            return null;
        }
        if (this.tileClazz == null) {
            try {
                this.tileClazz = this.clazz.newInstance();
            } catch (final InstantiationException e) {
                e.printStackTrace();
            } catch (final IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return this.tileClazz;
    }
}
