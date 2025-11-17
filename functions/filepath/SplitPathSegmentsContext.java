package filepath;

import java.util.ArrayList;
import java.util.List;

class SplitPathSegmentsContext {
    final String path;
    final int end;
    final List<String> segments;
    int startOfSegment;
    int currentIndex;
    char currentChar;

    SplitPathSegmentsContext(String path) {
        this.path = path;
        end = path.length();
        segments = new ArrayList<>();
    }

    public List<String> splitPathSegments() {
        while (currentIndex < end)
            extractSegments();
        return segments;
    }

    private void extractSegments() {
        currentChar = path.charAt(currentIndex);

        if (isSeparator()) {
            extractSegment();
            startOfSegment = currentIndex;
        }

        currentIndex++;
        addLastSegmentIfLeft();
    }

    private boolean isSeparator() {
        return isSeparator(currentChar);
    }

    private boolean isSeparator(char c) {
        return c == '/' || c == '\\';
    }

    private void extractSegment() {
        addSegment();
        setIndexAfterExtraSeparators();
        if (currentIndex < end)
            addSeparator();
    }

    private void addSegment() {
        String segment = path.substring(startOfSegment, currentIndex);
        segments.add(segment);
    }

    private void setIndexAfterExtraSeparators() {
        do {
            currentIndex++;
        } while (currentIndex < end && isSeparator(path.charAt(currentIndex)));
    }

    private void addSeparator() {
        String separator = Character.toString(currentChar);
        segments.add(separator);
    }

    private void addLastSegmentIfLeft() {
        if (startOfSegment < end && currentIndex >= end)
            segments.add(path.substring(startOfSegment));
    }
}
