package com.magang.projectmaganglatihan.customview

import com.magang.projectmaganglatihan.tflite.SimilarityClassifier.Recognition

interface ResultsView {
    fun setResults(results: List<Recognition?>?)
}