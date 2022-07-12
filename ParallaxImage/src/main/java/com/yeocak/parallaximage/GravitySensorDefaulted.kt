package com.yeocak.parallaximage

import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class GravitySensorDefaulted(context: Context) : SensorEventListener {
    private val sensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager
    private val sensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)

    private var defaultValues: Triple<Float, Float, Float>? = null

    private var onChangeListeners = mutableListOf<(x: Float, y: Float, z: Float) -> Unit>()

    var defaultValuesOn = true

    fun addOnChangeListener(listener: ((x: Float, y: Float, z: Float) -> Unit)) {
        onChangeListeners.add(listener)
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        p0 ?: return
        if (!defaultValuesOn || defaultValues != null) {
            val invokeX = p0.values[0] - (defaultValues?.first ?: 0f)
            val invokeY = p0.values[1] - (defaultValues?.second ?: 0f)
            val invokeZ = p0.values[2] - (defaultValues?.third ?: 0f)
            onChangeListeners.forEach {
                it.invoke(invokeX, invokeY, invokeZ)
            }
        } else {
            defaultValues = Triple(p0.values[0], p0.values[1], p0.values[2])
        }
    }

    fun start() {
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
    }

    fun stop() {
        sensorManager.unregisterListener(this)
        defaultValues = null
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}
}