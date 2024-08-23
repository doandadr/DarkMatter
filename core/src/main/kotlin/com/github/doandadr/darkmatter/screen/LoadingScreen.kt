package com.github.doandadr.darkmatter.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.github.doandadr.darkmatter.DarkMatter
import com.github.doandadr.darkmatter.asset.ShaderProgramAsset
import com.github.doandadr.darkmatter.asset.SoundAsset
import com.github.doandadr.darkmatter.asset.TextureAsset
import com.github.doandadr.darkmatter.asset.TextureAtlasAsset
import com.github.doandadr.darkmatter.ui.LabelStyles
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ktx.actors.plus
import ktx.actors.plusAssign
import ktx.async.KtxAsync
import ktx.collections.gdxArrayOf
import ktx.log.logger
import ktx.scene2d.*

private val LOG = logger<LoadingScreen>()

class LoadingScreen(
    game: DarkMatter
) : DarkMatterScreen(game) {
    private lateinit var progressBar: Image
    private lateinit var touchToBeginLabel: Label

    override fun show() {
        val old = System.currentTimeMillis()

        // queue asset loading
        val assetRefs = gdxArrayOf(
            TextureAsset.entries.map { assets.loadAsync(it.descriptor) },
            TextureAtlasAsset.entries.map { assets.loadAsync(it.descriptor) },
            SoundAsset.entries.map { assets.loadAsync(it.descriptor) },
            ShaderProgramAsset.entries.map { assets.loadAsync(it.descriptor) }
        ).flatten()

        // once assets are loaded -> change to GameScreen
        KtxAsync.launch {
            assetRefs.joinAll()
            LOG.debug { "Time for assets to be loaded: ${System.currentTimeMillis() - old} ms" }
            assetsLoaded()
        }

        setupUI()
    }

    override fun hide() {
        stage.clear()

    }

    private fun setupUI() {
        stage.actors {
            table {
                defaults().fillX().expandX()

                label("Loading Screen", LabelStyles.GRADIENT.name) {
                    wrap = true
                    setAlignment(Align.center)
                }
                row()

                touchToBeginLabel = label("Touch To Begin", LabelStyles.DEFAULT.name) {
                    wrap = true
                    setAlignment(Align.center)
                    color.a = 0f
                }
                row()

                stack { cell ->
                    progressBar = image("life_bar").apply {
                        scaleX = 0f
                    }
                    label("Loading...", LabelStyles.DEFAULT.name) {
                        setAlignment(Align.center)
                    }
                    cell.padLeft(5f).padRight(5f)
                }

                setFillParent(true)
                pack()
            }
        }
        stage.isDebugAll = false
    }

    override fun render(delta: Float) {
        if (assets.progress.isFinished && Gdx.input.justTouched() && game.containsScreen<GameScreen>()) {
            game.setScreen<GameScreen>()
            game.removeScreen<LoadingScreen>()
            dispose()
        }

        progressBar.scaleX = assets.progress.percent
        stage.run {
            viewport.apply()
            act()
            draw()
        }
    }

    private fun assetsLoaded() {
        game.addScreen(GameScreen(game, game.engine))
        touchToBeginLabel += forever(sequence(fadeIn(0.5f) + fadeOut(0.5f)))
    }
}
