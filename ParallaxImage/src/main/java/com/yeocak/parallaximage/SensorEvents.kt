package com.yeocak.parallaximage

import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

class SensorEvents(context: Context) : SensorEventListener {
    private val sensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager
    private val sensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)

    private var defaultValues: Triple<Float, Float, Float>? = null

    private var onChangeListener: ((x: Float, y: Float, z: Float) -> Unit)? = null

    fun setOnChangeListener(listener: ((x: Float, y: Float, z: Float) -> Unit)) {
        onChangeListener = listener
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        p0 ?: return
        if (sensor?.type == Sensor.TYPE_GRAVITY) {
            defaultValues?.let {
                onChangeListener?.invoke(
                    p0.values[0] - it.first,
                    p0.values[1] - it.second,
                    p0.values[2] - it.third
                )
            } ?: run {
                defaultValues = Triple(p0.values[0], p0.values[1], p0.values[2])
            }
        }
    }

    fun start() {
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}
}