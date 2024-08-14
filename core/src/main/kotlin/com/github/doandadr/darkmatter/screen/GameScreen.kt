package com.github.doandadr.darkmatter.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.utils.viewport.FitViewport
import com.github.doandadr.darkmatter.DarkMatter
import ktx.graphics.use
import ktx.log.logger

private val LOG = logger<GameScreen>()
/** First screen of the application. Displayed after the application is created.  */
class GameScreen(game: DarkMatter) : DarkMatterScreen(game) {
    private val viewport = FitViewport(9f, 16f)
    private val texture = Texture(Gdx.files.internal("graphics/ship_base.png"))
    private val sprite = Sprite(texture).apply {
        setSize(1f, 1f)
    }

    override fun show() {
        LOG.debug { "First Screen is shown" }
        sprite.setPosition(1f, 1f)
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        viewport.apply()
        game.batch.use(viewport.camera.combined) {
            sprite.draw(it)
        }
    }

    override fun dispose() {
        texture.dispose()
        game.batch.dispose()
    }
}
