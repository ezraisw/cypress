package com.inspw.cypress.game.rhythm;

public class RhythmTimer {
    private ScanlinePagePredictor predictor;
    private float time;
    private int spIndex;

    public RhythmTimer(ScanlinePagePredictor predictor) {
        this.predictor = predictor;
    }

    public ScanlinePagePredictor getPredictor() {
        return predictor;
    }

    public void setPredictor(ScanlinePagePredictor predictor) {
        this.predictor = predictor;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
        predictSpIndex();
    }

    public ScanlinePage currentPage() {
        if (spIndex < 0 || spIndex >= getPredictor().getPages().size()) {
            return null;
        }
        return predictor.getPages().get(spIndex);
    }

    public float currentPosition() {
        return predictor.positionAt(time, currentPage());
    }

    private void searchSpIndexBwd(int startIdx) {
        for (int i = startIdx; i >= 0; i--) {
            ScanlinePage sp = getPredictor().getPages().get(i);
            if (time >= sp.getStartTime() && time < sp.getEndTime()) {
                spIndex = i;
                return;
            }
        }
    }

    private void searchSpIndexFwd(int startIdx) {
        for (int i = startIdx; i < getPredictor().getPages().size(); i++) {
            ScanlinePage sp = getPredictor().getPages().get(i);
            if (time >= sp.getStartTime() && time < sp.getEndTime()) {
                spIndex = i;
                return;
            }
        }
    }

    private void predictSpIndex() {
        ScanlinePage sp = currentPage();

        if (sp == null) {
            searchSpIndexFwd(0);
        } else if (sp.getStartTime() > time) {
            searchSpIndexBwd(spIndex - 1);
        } else if (sp.getEndTime() <= time) {
            searchSpIndexFwd(spIndex + 1);
        }
    }
}
