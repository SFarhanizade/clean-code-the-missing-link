package filepath;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class PathNormalizer {

    private static final Pattern ABSOLUTE_PREFIX_PATTERN = Pattern.compile("^(\\\\\\\\|(?:[A-Za-z]:)?[\\\\/])[\\\\/]*");
    StringBuilder buf = new StringBuilder();

    public String normalize(String path) {
        path = extractAbsolutePrefix(path);
        List<String> segments = splitPathIntoNormalizedSegments(path);
        return buildNormalizedPath(segments);
    }

    private String extractAbsolutePrefix(String path) {
        Matcher matcher = ABSOLUTE_PREFIX_PATTERN.matcher(path);
        if (matcher.find()) {
            String absolutePrefix = matcher.group(1);
            buf.append(absolutePrefix);
            int afterAbsolutePrefix = matcher.end();
            path = path.substring(afterAbsolutePrefix);
        }
        return path;
    }

    private List<String> splitPathIntoNormalizedSegments(String path) {
        List<String> segments = splitPathSegments(path);
        collapseDotSegments(segments);
        return segments;
    }


    private static List<String> splitPathSegments(String path) {
        var context = new SplitPathSegmentsContext(path);
        return context.splitPathSegments();
    }


    private void collapseDotSegments(List<String> segments) {
        var context = new CollapsingDotSegmentsContext(buf, segments);
        context.collapseDotSegments();
    }


    private String buildNormalizedPath(List<String> segments) {
        for (String segment : segments)
            buf.append(segment);

        if (buf.isEmpty())
            buf.append('.');

        return buf.toString();
    }
}
