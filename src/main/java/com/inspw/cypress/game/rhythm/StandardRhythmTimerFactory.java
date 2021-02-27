package com.inspw.cypress.game.rhythm;

import com.inspw.cypress.game.misc.helper.MathHelper;
import com.inspw.cypress.game.model.serialized.Chart;
import com.inspw.cypress.game.model.serialized.Note;
import com.inspw.cypress.game.model.serialized.ScanlineMovement;

import java.util.*;
import java.util.stream.Collectors;

public class StandardRhythmTimerFactory implements RhythmTimerFactory {
    @Override
    public RhythmTimer create(Chart chart) {
        List<ScanlinePage> pages = new ArrayList<>();

        checkForDuplicateNoteIds(chart.getNotes());

        float time = -chart.getDelay();
        float lastPosition = 0;
        ScanlinePage prev = null;
        for (ScanlineMovement sm : chart.getScanlineMovements()) {
            if (sm.getDuration() == 0) {
                lastPosition = sm.getPosition();
                continue;
            }

            int r = 1;
            for (int i = 0; i <= sm.getRepeat(); i++) {
                float startPosition = MathHelper.clamp(lastPosition, -100, 100);
                float endPosition = MathHelper.clamp(sm.getPosition() * r, -100, 100);
                float startTime = time;
                float endTime = time + sm.getDuration();

                ScanlinePage page = new ScanlinePage(
                        startPosition,
                        endPosition,
                        startTime,
                        endTime,
                        findNotesBetween(chart.getNotes(), startTime, endTime),
                        prev
                );
                pages.add(page);

                time = endTime;
                lastPosition = endPosition;
                r *= -1;
                prev = page;
            }
        }

        return new RhythmTimer(new ScanlinePagePredictor(pages));
    }

    private void checkForDuplicateNoteIds(List<Note> notes) {
        Set<String> ids = new HashSet<>(notes.size());
        for (Note note : notes) {
            if (ids.contains(note.getId())) {
                throw new RhythmDataException("Duplicate note ID: " + note.getId());
            }
            ids.add(note.getId());
        }
    }

    private List<Note> findNotesBetween(List<Note> notes, float startTime, float endTime) {
        return notes.stream()
                .filter(n -> startTime <= n.getTime() && n.getTime() < endTime)
                .sorted((n1, n2) -> Float.compare(n1.getTime(), n2.getTime()))
                .collect(Collectors.toList());
    }
}
