package com.github.doandadr.darkmatter.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.github.doandadr.darkmatter.ecs.component.PlayerComponent
import com.github.doandadr.darkmatter.ecs.component.RemoveComponent
import com.github.doandadr.darkmatter.ecs.component.TransformComponent
import com.github.doandadr.darkmatter.event.GameEvent
import com.github.doandadr.darkmatter.event.GameEventManager
import ktx.ashley.addComponent
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.get
import kotlin.math.max

const val DAMAGE_AREA_HEIGHT = 2f
private const val DAMAGE_PER_SECOND = 25f
private const val DEATH_EXPLOSION_DURATION = 0.9f

class DamageSystem(private val gameEventManager: GameEventManager) :
    IteratingSystem(allOf(PlayerComponent::class, TransformComponent::class).exclude(RemoveComponent::class).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = entity[TransformComponent.mapper]
        require(transform != null) { "Entity |entity| must have a TransformComponent. entity=$entity" }
        val player = entity[PlayerComponent.mapper]
        require(player != null) { "Entity |entity| must have a PlayerComponent. entity=$entity" }

        if (transform.position.y <= DAMAGE_AREA_HEIGHT) {
            var damage = DAMAGE_PER_SECOND * deltaTime
            if (player.shield > 0f) {
                val blockAmount = player.shield
                player.shield = max(0f, player.shield - damage)
                damage -= blockAmount

                if (damage <= 0f) {
                    return
                }
            }

            player.life -= damage
            gameEventManager.dispatchEvent(GameEvent.PlayerHit.apply {
                this.player = entity
                this.life = player.life
                this.maxLife = player.maxLife
            })

            if (player.life <= 0f) {
                // dispatch player death event
                gameEventManager.dispatchEvent(GameEvent.PlayerDeath.apply {
                    this.distance = player.distance
                })
                entity.addComponent<RemoveComponent>(engine) {
                    delay = DEATH_EXPLOSION_DURATION
                }
            }
        }
    }
}
