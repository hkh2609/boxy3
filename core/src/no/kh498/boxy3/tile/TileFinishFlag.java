package no.kh498.boxy3.tile;

/**
 * @author karl henrik
 * @since 0.1.0
 */
public class TileFinishFlag extends AbstractTile {

    public TileFinishFlag() {}

    @Override
    public void onPlayerTouch() {
        System.out.println("Touched the flag!!");
    }
}
