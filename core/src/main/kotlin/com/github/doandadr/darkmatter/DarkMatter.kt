package com.github.doandadr.darkmatter

import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.github.doandadr.darkmatter.screen.FirstScreen
import com.github.doandadr.darkmatter.screen.SecondScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.log.logger

private val LOG = logger<DarkMatter>()

class DarkMatter : KtxGame<KtxScreen>() {
    override fun create() {
        Gdx.app.logLevel = LOG_DEBUG
        LOG.debug { "Creaete game instance" }
        addScreen(FirstScreen(this))
        addScreen(SecondScreen(this))
        setScreen<FirstScreen>()
    }
}
