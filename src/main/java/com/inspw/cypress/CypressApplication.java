package com.inspw.cypress;

import com.inspw.cypress.game.Game;
import com.inspw.cypress.game.constant.Library;
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery;

import javax.swing.*;

public class CypressApplication {
    public static void main(String[] args) {
        discoverLib();
        Game.instance()
                .configure()
                .start();
    }

    /**
     * Discover native libraries.
     */
    private static void discoverLib() {
        NativeDiscovery nd = new NativeDiscovery();
        if (!nd.discover()) {
            JOptionPane.showMessageDialog(
                    null,
                    Library.VLCLIB_DISCOVERY_FAILED_MESSAGE,
                    Library.VLCLIB_DISCOVERY_FAILED_TITLE,
                    JOptionPane.ERROR_MESSAGE
            );
            System.exit(Library.VLCLIB_DISCOVERY_FAILED_EXIT_STATUS);
        }
    }
}
