package com.github.doandadr.darkmatter.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.github.doandadr.darkmatter.DarkMatter
import ktx.log.logger

private val LOG = logger<FirstScreen>()
/** First screen of the application. Displayed after the application is created.  */
class FirstScreen(game: DarkMatter) : DarkMatterScreen(game) {
    override fun show() {
        super.show()
        LOG.debug { "First Screen is shown" }
    }

    override fun render(delta: Float) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            game.setScreen<SecondScreen>()
        }
    }
}
