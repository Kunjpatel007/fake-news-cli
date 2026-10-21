package com.fakenews.detection;

import com.fakenews.model.NewsSearch;

public interface NewsDetector {
    NewsSearch detect(String newsText);
}
