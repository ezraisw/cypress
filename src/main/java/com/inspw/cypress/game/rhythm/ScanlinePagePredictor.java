package com.inspw.cypress.game.rhythm;

import com.inspw.cypress.game.misc.helper.MathHelper;

import java.util.List;

public class ScanlinePagePredictor {
    private List<ScanlinePage> pages;

    public ScanlinePagePredictor(List<ScanlinePage> pages) {
        this.pages = pages;
    }

    public List<ScanlinePage> getPages() {
        return pages;
    }

    public void setPages(List<ScanlinePage> pages) {
        this.pages = pages;
    }

    public ScanlinePage pageAt(float time) {
        for (ScanlinePage page : getPages()) {
            if (page.getStartTime() <= time && time < page.getEndTime()) {
                return page;
            }
        }
        return null;
    }

    public float positionAt(float time) {
        return positionAt(time, pageAt(time));
    }

    public float positionAt(float time, ScanlinePage sp) {
        if (sp == null) {
            return 0;
        }

        float spTimeDelta = sp.getEndTime() - sp.getStartTime();
        float timerToStartTime = time - sp.getStartTime();
        float spPosDelta = sp.getEndPosition() - sp.getStartPosition();
        float position = (float) (timerToStartTime / spTimeDelta * spPosDelta + sp.getStartPosition());

        return MathHelper.clamp(position, -100, 100);
    }
}
