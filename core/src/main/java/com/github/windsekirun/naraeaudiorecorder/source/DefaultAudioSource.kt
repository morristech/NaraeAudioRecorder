package com.github.windsekirun.naraeaudiorecorder.source

import android.media.AudioRecord
import android.media.MediaSyncEvent
import com.github.windsekirun.naraeaudiorecorder.config.AudioRecordConfig

/**
 * Default settings of [AudioSource]
 *
 * @param audioRecordConfig optional, [AudioRecordConfig] instance for configure [AudioRecord]
 */
open class DefaultAudioSource(private val audioRecordConfig: AudioRecordConfig = AudioRecordConfig.defaultConfig())
    : AudioSource {

    /**
     * backing property name for [getBufferSize]
     */
    private val _bufferSize: Int by lazy {
        AudioRecord.getMinBufferSize(
            audioRecordConfig.frequency,
            audioRecordConfig.channel, audioRecordConfig.audioEncoding
        )
    }

    /**
     * backing property name for [getAudioRecord]
     */
    private val _audioRecord: AudioRecord by lazy {
        AudioRecord(
            audioRecordConfig.audioSource,
            audioRecordConfig.frequency,
            audioRecordConfig.channel,
            audioRecordConfig.audioEncoding,
            getBufferSize()
        )
    }

    /**
     * see [AudioSource.getAudioRecord]
     */
    override fun getAudioRecord(): AudioRecord = _audioRecord

    /**
     * see [AudioSource.getAudioConfig]
     */
    override fun getAudioConfig(): AudioRecordConfig = audioRecordConfig

    /**
     * see [AudioSource.getBufferSize]
     */
    override fun getBufferSize(): Int = _bufferSize

    /**
     * Pre-process [AudioRecord] for start to recording.
     */
    @JvmOverloads
    open fun preProcessAudioRecord(mediaSyncEvent: MediaSyncEvent? = null): AudioRecord {
        return getAudioRecord().apply {
            if (mediaSyncEvent != null) {
                startRecording(mediaSyncEvent)
            } else {
                startRecording()
            }
        }
    }
}