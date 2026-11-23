package main;

import data.DataStorage;
import java.awt.CardLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

public class Main {
    JFrame window;
    CardLayout cardLayout;
    JPanel container;
    GamePanel gamePanel;
    Settings settingsScreen;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

    public Main() {
        window = new JFrame("Purranormal");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);

        // Create screens
        TitleScreen titleScreen = new TitleScreen(
            this::startNewGame,
            this::loadGame
        );
        titleScreen.setOnSettingsClick(this::showSettingsScreen);

        gamePanel = new GamePanel(this::showTitleScreen, this::showEndingScreen);
        titleScreen.setOnButtonClick(() -> gamePanel.playSound(sound.SoundEffects.Cue.CLICK));

        settingsScreen = new Settings(
            this::showTitleScreen,        
            volume -> {
                gamePanel.setVolume(volume);  
            }
        );
        settingsScreen.setVolume(45);
        settingsScreen.setOnButtonClick(() -> gamePanel.playSound(sound.SoundEffects.Cue.CLICK));

        // Add to card layout
        container.add(titleScreen, "TitleScreen");
        container.add(gamePanel, "GamePanel");
        container.add(settingsScreen, "SettingsScreen");
        EndingScreen endingScreen = new EndingScreen(this::startNewGame, () ->
                window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING)));
        endingScreen.setOnButtonClick(() -> gamePanel.playSound(sound.SoundEffects.Cue.CLICK));
        container.add(endingScreen, "EndingScreen");
        window.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { gamePanel.close(); }
        });

        window.add(container);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    public void showTitleScreen() {
        gamePanel.stopGameLoop();
        cardLayout.show(container, "TitleScreen");
    }

    public void showEndingScreen() {
        gamePanel.stopGameLoop();
        cardLayout.show(container, "EndingScreen");
    }

    public void showSettingsScreen() {
        cardLayout.show(container, "SettingsScreen");
    }

    public void startNewGame() {
        gamePanel.setupGame();
        gamePanel.startGameLoop();
        cardLayout.show(container, "GamePanel");
        gamePanel.requestFocusInWindow();
    }

    public void loadGame(DataStorage data) {
        gamePanel.loadGame(data);
        cardLayout.show(container, "GamePanel");
        gamePanel.requestFocusInWindow();
        gamePanel.startGameLoop();
    }
}
