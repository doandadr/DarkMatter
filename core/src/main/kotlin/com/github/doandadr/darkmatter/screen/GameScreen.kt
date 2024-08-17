package com.github.doandadr.darkmatter.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.viewport.FitViewport
import com.github.doandadr.darkmatter.DarkMatter
import com.github.doandadr.darkmatter.UNIT_SCALE
import com.github.doandadr.darkmatter.ecs.component.FacingComponent
import com.github.doandadr.darkmatter.ecs.component.GraphicComponent
import com.github.doandadr.darkmatter.ecs.component.PlayerComponent
import com.github.doandadr.darkmatter.ecs.component.TransformComponent
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.with
import ktx.graphics.use
import ktx.log.logger

private val LOG = logger<GameScreen>()

/** First screen of the application. Displayed after the application is created.  */
class GameScreen(game: DarkMatter) : DarkMatterScreen(game) {
    override fun show() {
        LOG.debug { "First Screen is shown" }

        engine.entity {
            with<TransformComponent> {
                position.set(MathUtils.random(0f, 9f), MathUtils.random(0f, 16f), 0f)
            }
            with<GraphicComponent>()
            with<PlayerComponent>()
            with<FacingComponent>()
        }
    }

    override fun render(delta: Float) {
        engine.update(delta)
    }
}
