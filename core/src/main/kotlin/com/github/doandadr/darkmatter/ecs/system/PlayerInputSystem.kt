package com.github.doandadr.darkmatter.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.doandadr.darkmatter.ecs.component.FacingComponent
import com.github.doandadr.darkmatter.ecs.component.FacingDirection
import com.github.doandadr.darkmatter.ecs.component.PlayerComponent
import com.github.doandadr.darkmatter.ecs.component.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get

private const val TOUCH_TOLERANCE_DISTANCE = 0.2f

class PlayerInputSystem(private val gameViewport: Viewport) :
    IteratingSystem(allOf(PlayerComponent::class, TransformComponent::class, FacingComponent::class).get()) {
    private val tempVec = Vector2()

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val facing = entity[FacingComponent.mapper]
        require(facing != null) {"Entity |entity| must have a FacingComponent. entity=$entity"}
        val transform = entity[TransformComponent.mapper]
        require(transform != null) {"Entity |entity| must have a TransformComponent. entity=$entity"}

        tempVec.x = Gdx.input.x.toFloat()
        gameViewport.unproject(tempVec)
        val diffX = tempVec.x - (transform.position.x + transform.size.x * 0.5f)
        facing.direction = when {
            diffX < -TOUCH_TOLERANCE_DISTANCE -> FacingDirection.LEFT
            diffX > TOUCH_TOLERANCE_DISTANCE -> FacingDirection.RIGHT
            else -> FacingDirection.DEFAULT
        }
    }
}
