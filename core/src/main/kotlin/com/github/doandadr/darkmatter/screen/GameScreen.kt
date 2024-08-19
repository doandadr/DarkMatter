package com.github.doandadr.darkmatter.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.viewport.FitViewport
import com.github.doandadr.darkmatter.DarkMatter
import com.github.doandadr.darkmatter.UNIT_SCALE
import com.github.doandadr.darkmatter.V_WIDTH
import com.github.doandadr.darkmatter.ecs.component.*
import com.github.doandadr.darkmatter.ecs.system.DAMAGE_AREA_HEIGHT
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
                setInitialPosition(4.5f, 8f, 0f)
            }
            with<MoveComponent>()
            with<GraphicComponent>()
            with<PlayerComponent>()
            with<FacingComponent>()
        }

        engine.entity{
            with<TransformComponent> {
                size.set(
                    V_WIDTH.toFloat(),
                    DAMAGE_AREA_HEIGHT
                )
            }
            with<AnimationComponent>{
                type = AnimationType.DARK_MATTER

            }
            with<GraphicComponent>()
        }
    }

    override fun render(delta: Float) {
        (game.batch as SpriteBatch).renderCalls = 0
        engine.update(delta)
        // LOG.debug { "Rendercalls: ${(game.batch as SpriteBatch).renderCalls}" }
    }
}
