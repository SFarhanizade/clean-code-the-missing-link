package filepath;

import java.util.List;

class CollapsingDotSegmentsContext {
    final StringBuilder buffer;
    final boolean isNotAbsolute;
    final List<String> segments;
    int index;
    String currentSegment;

    CollapsingDotSegmentsContext(StringBuilder buffer, List<String> segments) {
        this.buffer = buffer;
        this.isNotAbsolute = buffer.isEmpty();
        this.segments = segments;
    }


    public void collapseDotSegments() {
        while (index < segments.size()) {
            currentSegment = segments.get(index);
            tryToCollapse();
        }
    }

    private void tryToCollapse() {
        if (isDot()) removeDot();
        else if (isDotDot()) removeDotDot();
        else next();
    }

    private boolean isDot() {
        return currentSegment.equals(".");
    }

    private void removeDot() {
        segments.remove(index);
        if (!segments.isEmpty()) {
            segments.remove(index > 0 ? index - 1 : index);
        }
    }

    private boolean isDotDot() {
        return currentSegment.equals("..");
    }

    private void removeDotDot() {
        if (isLeading()) removeLeadingDotDot();
        else removeTrailingDotDot();
    }

    private boolean isLeading() {
        return index == 0;
    }

    private void removeLeadingDotDot() {
        removeDotDotPart();
        if (hasSeparator()) addSeparator();
        if (isNotAbsolute) addToBuffer();
    }

    private void removeDotDotPart() {
        segments.remove(0);
    }

    private boolean hasSeparator() {
        return !segments.isEmpty();
    }

    private void addSeparator() {
        currentSegment += removeAndGetSeparator();
    }

    private void addToBuffer() {
        buffer.append(currentSegment);
    }

    private void removeTrailingDotDot() {
        setIndexToPreviousPart();
        removeFromPreviousPart();

        // I did not understand usage of this if so I could not refactor it
        if (index > 0)
            segments.remove(index - 1);
        else if (hasSeparator())
            removeSeparator();
    }

    private void setIndexToPreviousPart() {
        index -= 2;
    }

    private void removeFromPreviousPart() {
        for (int j = 0; j < 3; j++)
            segments.remove(index);
    }

    private void removeSeparator() {
        removeAndGetSeparator();
    }

    private String removeAndGetSeparator() {
        return segments.remove(0);
    }

    private void next() {
        index += 2;
    }
}
