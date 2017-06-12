package no.kh498.boxy3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author karl henrik
 * @since 0.1.0
 */
public enum Tile {
    AIR(-1),
    COBBLESTONE(0);


    private final int textureIndex;
    private static final Tile[] tileArray = new Tile[Tile.values().length];

    private final TextureRegion[] textures = BoxyMain.getAllRegions(new Texture(Gdx.files.internal("tile.png")), 4, 4);

    static {
        for (final Tile tile : Tile.values()) {
            if (tile.textureIndex == -1) {
                continue;
            }
            if (tileArray[tile.textureIndex] != null) {
                throw new IllegalArgumentException("Two tiles have the same textureIndex!");
            }

            tileArray[tile.textureIndex] = tile;
        }
    }

    Tile(final int textureIndex) {
        this.textureIndex = textureIndex;
    }

    public static Tile fromIndex(final int index) {
        if (index >= Tile.values().length) {
            return null;
        }
        return tileArray[index];
    }

    public TextureRegion getTexture() {
        if (this.textureIndex == -1) {
            return null;
        }
        return this.textures[this.textureIndex];
    }
}
