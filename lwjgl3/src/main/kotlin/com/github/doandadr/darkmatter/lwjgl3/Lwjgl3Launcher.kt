package com.github.doandadr.darkmatter.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.github.doandadr.darkmatter.DarkMatter

/** Launches the desktop (LWJGL3) application.  */

fun main() {
//        if (StartupHelper.startNewJvmIfRequired()) return; // This handles macOS support and helps on Windows.
    Lwjgl3Application(DarkMatter(), Lwjgl3ApplicationConfiguration().apply {
        setTitle("Dark Matter")
        useVsync(true)
        //// Limits FPS to the refresh rate of the currently active monitor.
        setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate)
        //// If you remove the above line and set Vsync to false, you can get unlimited FPS, which can be
        //// useful for testing performance, but can also be very stressful to some hardware.
        //// You may also need to configure GPU drivers to fully disable Vsync; this can cause screen tearing.
        setWindowedMode(9 * 32, 16 * 32)
        setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png")
    })
}
