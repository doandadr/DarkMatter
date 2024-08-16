package com.github.doandadr.darkmatter.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.viewport.FitViewport
import com.github.doandadr.darkmatter.DarkMatter
import com.github.doandadr.darkmatter.UNIT_SCALE
import com.github.doandadr.darkmatter.ecs.component.GraphicComponent
import com.github.doandadr.darkmatter.ecs.component.TransformComponent
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.with
import ktx.graphics.use
import ktx.log.logger

private val LOG = logger<GameScreen>()

/** First screen of the application. Displayed after the application is created.  */
class GameScreen(game: DarkMatter) : DarkMatterScreen(game) {
    private val playerTexture = Texture(Gdx.files.internal("graphics/ship_base.png"))

    override fun show() {
        LOG.debug { "First Screen is shown" }

        repeat(10) {
            engine.entity {
                with<TransformComponent> {
                    position.set(MathUtils.random(0f, 9f), MathUtils.random(0f, 16f), 0f)
                }
                with<GraphicComponent> {
                    sprite.run {
                        setRegion(playerTexture) // required to set width and height
                        setSize(texture.width * UNIT_SCALE, texture.height * UNIT_SCALE)
                        setOriginCenter() // required to rotate entity
                    }
                }
            }
        }
    }

    override fun render(delta: Float) {
        engine.update(delta)
    }

    override fun dispose() {
        playerTexture.dispose()
        batch.dispose()
    }
}
