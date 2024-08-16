package com.github.doandadr.darkmatter.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool

class FacingComponent : Component, Pool.Poolable {
    var direction = FacingDirection.DEFAULT

    override fun reset() {
        direction = FacingDirection.DEFAULT
    }
}

enum class FacingDirection {
    LEFT, DEFAULT, RIGHT
}
