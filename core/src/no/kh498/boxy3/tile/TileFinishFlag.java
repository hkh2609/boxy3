package no.kh498.boxy3.tile;

import no.kh498.boxy3.Player;
import no.kh498.boxy3.Tile;

/**
 * @author karl henrik
 * @since 0.1.0
 */
public class TileFinishFlag extends AbstractTile {

    public TileFinishFlag() {}
    
    @Override
    public void onPlayerTouch(Player player) {
        System.out.println("Touched the flag!!");
    }
    @Override
    public Tile getTile() {
        return Tile.FINISH_FLAG;
    }
}
