package com.github.doandadr.darkmatter.screen

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.doandadr.darkmatter.DarkMatter
import com.github.doandadr.darkmatter.event.GameEventListener
import com.github.doandadr.darkmatter.event.GameEventManager
import ktx.app.KtxScreen

abstract class DarkMatterScreen(
    val game: DarkMatter,
    val gameViewport: Viewport = game.gameViewport,
    val uiViewport: Viewport = game.uiViewport,
    val batch: Batch = game.batch,
    val engine: Engine = game.engine,
    val gameEventManager: GameEventManager = game.gameEventManager
) : KtxScreen {

    override fun resize(width: Int, height: Int) {
        gameViewport.update(width, height, true)
        uiViewport.update(width, height, true)
    }
}

