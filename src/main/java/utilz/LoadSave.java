package utilz;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

public class LoadSave {
    public static final String PLAYER_ATLAS = "player_sprites.png";
    public static final String LEVEL_ATLAS = "outside_sprites.png";
    public static final String MENU_BUTTONS = "button_atlas.png";
    public static final String MENU_BACKGROUND = "menu_background.png";
    public static final String PAUSE_BACKGROUND = "pause_menu.png";
    public static final String SOUND_BUTTONS = "sound_button.png";
    public static final String URM_BUTTONS = "urm_buttons.png";
    public static final String VOLUME_BUTTONS = "volume_buttons.png";
    public static final String MENU_BACKGROUND_IMG = "background_menu.png";
    public static final String PLAYING_BG_IMG = "playing_bg_img.png";
    public static final String BIG_CLOUDS = "big_clouds.png";
    public static final String SMALL_CLOUDS = "small_clouds.png";
    public static final String CRABBY_SPRITE = "crabby_sprite.png";
    public static final String STATUS_BAR = "health_power_bar.png";
    public static final String COMPLETED_IMG = "completed_sprite.png";
    public static final String POTION_ATLAS = "potions_sprites.png";
    public static final String CONTAINER_ATLAS = "objects_sprites.png";
    public static final String TRAP_ATLAS = "trap_atlas.png";
    public static final String CANNON_ATLAS = "cannon_atlas.png";
    public static final String CANNON_BALL = "ball.png";
    public static final String DEATH_SCREEN = "death_screen.png";
    public static final String OPTIONS_MENU = "options_background.png";
    public static final String PINKSTAR_ATLAS = "pink_star_atlas.png";
    public static final String QUESTION_ATLAS = "question_atlas.png";
    public static final String EXCLAMATION_ATLAS = "exclamation_atlas.png";
    public static final String SHARK_ATLAS = "shark_atlas.png";
    public static final String CREDITS = "credits_list.png";
    public static final String GRASS_ATLAS = "grass_atlas.png";
    public static final String TREE_ONE_ATLAS = "tree_one_atlas.png";
    public static final String TREE_TWO_ATLAS = "tree_two_atlas.png";
    public static final String GAME_COMPLETED = "game_completed.png";
    public static final String RAIN_PARTICLE = "rain_particle.png";
    public static final String WATER_TOP = "water_atlas_animation.png";
    public static final String WATER_BOTTOM = "water.png";
    public static final String SHIP = "ship.png";

    public static BufferedImage GetSpriteAtlas(String fileName) {
        BufferedImage img;
        try (InputStream is = LoadSave.class.getResourceAsStream("/" + fileName)) {
            img = ImageIO.read(Objects.requireNonNull(is));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return img;
    }

    public static BufferedImage[] GetAllLevels() {
        URL url = Objects.requireNonNull(LoadSave.class.getResource("/levels"));
        File file;

        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        File[] files = Objects.requireNonNull(file.listFiles());
        File[] filesSorted = new File[files.length];

        for (int i = 0; i < filesSorted.length; i++) {
            for (File value : files) {
                if (value.getName().equals((i + 1) + ".png")) {
                    filesSorted[i] = value;
                }
            }
        }

        BufferedImage[] images = new BufferedImage[filesSorted.length];

        for (int i = 0; i < images.length; i++) {
            try {
                if (filesSorted[i] != null) {
                    images[i] = ImageIO.read(filesSorted[i]);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return images;
    }
}