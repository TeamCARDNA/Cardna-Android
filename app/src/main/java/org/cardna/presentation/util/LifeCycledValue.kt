package org.cardna.presentation.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun <T: Any> LifecycleOwner.lifeCycled(initializer: () -> T): ReadOnlyProperty<LifecycleOwner, T> {
    return LifeCycledValue(initializer)
}

private class LifeCycledValue<T : Any>(
    private val initializer: () -> T
) : ReadOnlyProperty<LifecycleOwner, T>, LifecycleObserver {

    @Volatile
    private var value: T? = null

    override fun getValue(thisRef: LifecycleOwner, property: KProperty<*>): T {
        val currentValue = value
        if (currentValue != null) {
            return currentValue
        }
        return when (thisRef.lifecycle.currentState) {
            Lifecycle.State.DESTROYED -> error("value is already released cause lifecycle owner is destroyed")
            else -> synchronized(this) {
                thisRef.lifecycle.removeObserver(this)
                thisRef.lifecycle.addObserver(this)
                initializer().also { this.value = it }
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        value = null
    }
}