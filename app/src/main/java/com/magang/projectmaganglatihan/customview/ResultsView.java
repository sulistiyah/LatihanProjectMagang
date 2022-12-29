package com.magang.projectmaganglatihan.customview;

import com.magang.projectmaganglatihan.tflite.SimilarityClassifier.Recognition;

import java.util.List;

public interface ResultsView {
  public void setResults(final List<Recognition> results);
}
