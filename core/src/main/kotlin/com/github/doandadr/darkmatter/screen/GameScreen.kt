package com.github.doandadr.darkmatter.screen

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.github.doandadr.darkmatter.DarkMatter
import com.github.doandadr.darkmatter.UNIT_SCALE
import com.github.doandadr.darkmatter.V_WIDTH
import com.github.doandadr.darkmatter.asset.MusicAsset
import com.github.doandadr.darkmatter.ecs.component.*
import com.github.doandadr.darkmatter.ecs.system.DAMAGE_AREA_HEIGHT
import com.github.doandadr.darkmatter.event.GameEvent
import com.github.doandadr.darkmatter.event.GameEventListener
import ktx.ashley.entity
import ktx.ashley.with
import ktx.log.logger
import ktx.preferences.flush
import ktx.preferences.get
import ktx.preferences.set

private val LOG = logger<GameScreen>()

/** First screen of the application. Displayed after the application is created.  */
class GameScreen(game: DarkMatter, private val engine: Engine) : DarkMatterScreen(game), GameEventListener {

    override fun show() {
        LOG.debug { "First Screen is shown" }
        LOG.debug { "${preferences["highscore", 0f]}" }

        gameEventManager.addListener(GameEvent.PlayerDeath::class, this)

        audioService.play(MusicAsset.GAME)
        spawnPlayer()

        engine.entity {
            with<TransformComponent> {
                size.set(
                    V_WIDTH.toFloat(),
                    DAMAGE_AREA_HEIGHT
                )
            }
            with<AnimationComponent> {
                type = AnimationType.DARK_MATTER

            }
            with<GraphicComponent>()
        }
    }

    override fun hide() {
        super.hide()
        gameEventManager.removeListener(this)
    }

    private fun spawnPlayer() {
        val playerShip = engine.entity {
            with<TransformComponent> {
                setInitialPosition(4.5f, 8f, -1f)
            }
            with<MoveComponent>()
            with<GraphicComponent>()
            with<PlayerComponent>()
            with<FacingComponent>()
        }
        engine.entity {
            with<TransformComponent>()
            with<AttachComponent> {
                entity = playerShip
                offset.set(1f * UNIT_SCALE, -6f * UNIT_SCALE)
            }
            with<GraphicComponent>()
            with<AnimationComponent> {
                type = AnimationType.FIRE
            }
        }
    }

    override fun render(delta: Float) {
        (game.batch as SpriteBatch).renderCalls = 0
        engine.update(delta)
        audioService.update()
//         LOG.debug { "Rendercalls: ${(game.batch as SpriteBatch).renderCalls}" }
    }

    override fun onEvent(event: GameEvent) {
        when(event) {
            is GameEvent.PlayerDeath -> {
                LOG.debug { "Player died with a distrance of ${event.distance}" }
                preferences.flush{
                    this["highscore"] = event.distance
                }
                spawnPlayer()
            }
            GameEvent.CollectPowerUp -> TODO()
            GameEvent.PlayerHit -> TODO()
        }
    }
}
