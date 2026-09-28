package com.araut.mosaicrelay.sdui.data

fun interface PageDocumentSource {
    suspend fun read(): String
}