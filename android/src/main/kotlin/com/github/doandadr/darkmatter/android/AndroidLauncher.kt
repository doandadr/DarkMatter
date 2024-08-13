package com.github.doandadr.darkmatter.android

import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration

/** Launches the Android application.  */
class AndroidLauncher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val configuration: AndroidApplicationConfiguration = AndroidApplicationConfiguration()
        configuration.useImmersiveMode = true // Recommended, but not required.
        initialize(DarkMatter(), configuration)
    }
}
