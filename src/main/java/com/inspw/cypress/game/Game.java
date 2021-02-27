package com.inspw.cypress.game;

import com.inspw.cypress.game.constant.Engine;
import com.inspw.cypress.game.engine.GameLoop;
import com.inspw.cypress.game.engine.SingleThreadFixedRateGameLoop;
import com.inspw.cypress.game.input.Input;
import com.inspw.cypress.game.input.UIProvidedInput;
import com.inspw.cypress.game.loader.DirectoryBasedSongLoader;
import com.inspw.cypress.game.loader.SongLoader;
import com.inspw.cypress.game.profile.FileBasedProfileManager;
import com.inspw.cypress.game.profile.ProfileManager;
import com.inspw.cypress.game.rhythm.DefaultRhythmSessionManager;
import com.inspw.cypress.game.rhythm.RhythmSessionManager;
import com.inspw.cypress.game.scenery.DefaultDirector;
import com.inspw.cypress.game.scenery.SceneManager;
import com.inspw.cypress.game.scenery.DefaultSceneManager;
import com.inspw.cypress.game.scenery.scene.PlayScene;
import com.inspw.cypress.game.scenery.scene.ResultScene;
import com.inspw.cypress.game.scenery.scene.SelectScene;
import com.inspw.cypress.game.scenery.scene.StartScene;
import com.inspw.cypress.game.ui.SwingUIInfrastructure;
import com.inspw.cypress.game.ui.UIInfrastructure;
import com.inspw.cypress.game.ui.rendering.RenderingSurface;

public class Game {
    private static Game instance;
    private final SceneManager sceneManager = new DefaultSceneManager();
    private final SongLoader songLoader = new DirectoryBasedSongLoader();
    private final ProfileManager profileManager = new FileBasedProfileManager();
    private final RhythmSessionManager rhythmSessionManager = new DefaultRhythmSessionManager();
    private final UIInfrastructure ui = new SwingUIInfrastructure(sceneManager);
    private final Input input = new UIProvidedInput(ui.inputProvider());
    private final GameLoop gameLoop = new SingleThreadFixedRateGameLoop(
            new DefaultDirector(sceneManager, ui.renderingSurface(), ui.inputProvider()), Engine.UPDATE_RATE);

    /**
     * Obtain the current game instance.
     *
     * @return the game instance
     */
    public static Game instance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    /**
     * Obtain the scene manager.
     *
     * @return the scene manager instance
     */
    public SceneManager sceneManager() {
        return sceneManager;
    }

    /**
     * Obtain the song loader that is used to load song information
     * and charts.
     *
     * @return the song loader instance
     */
    public SongLoader songLoader() {
        return songLoader;
    }

    /**
     * Obtain the profile manager that is used for to save or load
     * persistent player data between app sessions.
     *
     * @return the profile manager instance
     */
    public ProfileManager profileManager() {
        return profileManager;
    }

    /**
     * Obtain the rhythm session manager that is used for managing information
     * in a playing session (such as score, combo, hit information, etc.).
     *
     * @return the rhythm session manager
     */
    public RhythmSessionManager rhythmSessionManager() {
        return rhythmSessionManager;
    }

    /**
     * Alias of {@code ui().getRenderingSurface()}.
     *
     * @return the rendering surface instance
     */
    public RenderingSurface renderingSurface() {
        return ui.renderingSurface();
    }

    /**
     * Obtain the instance for retrieving user input.
     *
     * @return the input instance
     */
    public Input input() {
        return input;
    }

    /**
     * Configure values and settings of required objects.
     *
     * @return the current instance
     */
    public Game configure() {
        ui.configure();

        sceneManager
                .register("start", StartScene.class)
                .register("select", SelectScene.class)
                .register("play", PlayScene.class)
                .register("result", ResultScene.class)
                .changeTo("start");

        profileManager.loadProfile();

        return this;
    }

    /**
     * Start the game loop and show the UI window.
     *
     * @return the current instance
     */
    public Game start() {
        ui.start();
        gameLoop.start();
        return this;
    }
}
