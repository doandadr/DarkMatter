package com.github.doandadr.darkmatter

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.github.doandadr.darkmatter.asset.*
import com.github.doandadr.darkmatter.audio.AudioService
import com.github.doandadr.darkmatter.audio.DefaultAudioService
import com.github.doandadr.darkmatter.ecs.system.*
import com.github.doandadr.darkmatter.event.GameEventManager
import com.github.doandadr.darkmatter.screen.LoadingScreen
import com.github.doandadr.darkmatter.ui.createSkin
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import ktx.collections.gdxArrayOf
import ktx.log.logger

const val UNIT_SCALE = 1 / 16f
const val V_WIDTH = 9
const val V_HEIGHT = 16
const val V_WIDTH_PIXELS = 135
const val V_HEIGHT_PIXELS = 240
private val LOG = logger<DarkMatter>()

class DarkMatter : KtxGame<KtxScreen>() {
    val uiViewport = FitViewport(V_WIDTH_PIXELS.toFloat(), V_HEIGHT_PIXELS.toFloat())
    val stage: Stage by lazy {
        val result = Stage(uiViewport, batch)
        Gdx.input.inputProcessor = result
        result
    }

    val gameViewport = FitViewport(V_WIDTH.toFloat(), V_HEIGHT.toFloat())
    val batch: Batch by lazy { SpriteBatch() }
    val assets: AssetStorage by lazy {
        KtxAsync.initiate()
        AssetStorage()
    }
    val gameEventManager = GameEventManager()
    val audioService: AudioService by lazy { DefaultAudioService(assets) }
    val preferences: Preferences by lazy { Gdx.app.getPreferences("dark-matter") }

    val engine: Engine by lazy {
        PooledEngine().apply {
            val graphicsAtlas = assets[TextureAtlasAsset.GAME_GRAPHICS.descriptor]
            val background = assets[TextureAsset.BACKGROUND.descriptor]


            addSystem(PlayerInputSystem(gameViewport))
            addSystem(MoveSystem())
            addSystem(PowerUpSystem(gameEventManager, audioService))
            addSystem(DamageSystem(gameEventManager))
            addSystem(CameraShakeSystem(gameViewport.camera, gameEventManager))
            addSystem(
                PlayerAnimationSystem(
                    graphicsAtlas.findRegion("ship_base"),
                    graphicsAtlas.findRegion("ship_left"),
                    graphicsAtlas.findRegion("ship_right")
                )
            )
            addSystem(AttachSystem())
            addSystem(AnimationSystem(graphicsAtlas))
            addSystem(
                RenderSystem(
                    batch,
                    gameViewport,
                    uiViewport,
                    background,
                    gameEventManager,
                    assets[ShaderProgramAsset.OUTLINE.descriptor]
                )
            )
            addSystem(RemoveSystem())
            addSystem(DebugSystem())
        }
    }

    override fun create() {
        Gdx.app.logLevel = LOG_DEBUG
        LOG.debug { "Create game instance" }

        val assetRefs = gdxArrayOf(
            TextureAtlasAsset.entries.filter { it.isSkinAtlas }.map { assets.loadAsync(it.descriptor) },
            BitmapFontAsset.entries.map { assets.loadAsync(it.descriptor) }
        ).flatten()
        KtxAsync.launch {
            assetRefs.joinAll()
            createSkin(assets)
            addScreen(LoadingScreen(this@DarkMatter))
            setScreen<LoadingScreen>()
        }
    }

    override fun dispose() {
        super.dispose()
        LOG.debug { "Sprites in batch: ${(batch as SpriteBatch).maxSpritesInBatch}" }
        MusicAsset.entries.forEach {
            LOG.debug { "Refcount $it: ${assets.getReferenceCount(it.descriptor)}" }
        }
        batch.dispose()
        assets.dispose()
        stage.dispose()
    }
}
