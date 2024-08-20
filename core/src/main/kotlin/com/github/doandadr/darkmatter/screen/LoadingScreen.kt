package com.github.doandadr.darkmatter.screen

import com.github.doandadr.darkmatter.DarkMatter
import com.github.doandadr.darkmatter.asset.SoundAsset
import com.github.doandadr.darkmatter.asset.TextureAsset
import com.github.doandadr.darkmatter.asset.TextureAtlasAsset
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ktx.async.KtxAsync
import ktx.collections.gdxArrayOf
import ktx.log.logger

private val LOG = logger<LoadingScreen>()

class LoadingScreen(
    game: DarkMatter
) : DarkMatterScreen(game) {
    override fun show() {
        val old = System.currentTimeMillis()

        // queue asset loading
        val assetRefs = gdxArrayOf(
            TextureAsset.entries.map { assets.loadAsync(it.descriptor) },
            TextureAtlasAsset.entries.map { assets.loadAsync(it.descriptor) },
            SoundAsset.entries.map { assets.loadAsync(it.descriptor) }
        ).flatten()

        // once assets are loaded -> change to GameScreen
        KtxAsync.launch {
            assetRefs.joinAll()
            LOG.debug { "Time for assets to be loaded: ${System.currentTimeMillis() - old} ms" }
            assetsLoaded()
        }

        //..
        // setup UI
    }

    private fun assetsLoaded() {
        game.addScreen(GameScreen(game, game.engine))
        game.setScreen<GameScreen>()
        game.removeScreen<LoadingScreen>()
        dispose()
    }
}
