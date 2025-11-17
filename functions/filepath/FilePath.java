// This file originates from the Jenkins open-source project and is licensed under the MIT License.

package filepath;

import module java.base;

public class FilePath {
    public static String normalize(String path) {
        var normalizer = new PathNormalizer();
        return normalizer.normalize(path);
    }
}
