package com.github.doandadr.darkmatter.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.github.doandadr.darkmatter.DarkMatter
import ktx.graphics.use
import ktx.log.logger
import ktx.log.debug

private val LOG = logger<GameScreen>()
/** First screen of the application. Displayed after the application is created.  */
class GameScreen(game: DarkMatter) : DarkMatterScreen(game) {
    private val texture = Texture(Gdx.files.internal("graphics/ship_base.png"))
    private val sprite = Sprite(texture)

    override fun show() {
        LOG.debug { "First Screen is shown" }
        sprite.setPosition(1f, 1f)
    }

    override fun render(delta: Float) {
        game.batch.use {
            sprite.draw(it)
        }

    }

    override fun dispose() {
        texture.dispose()
        game.batch.dispose()
    }
}
