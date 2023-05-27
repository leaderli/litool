

package io.leaderli.litool.core.internal;

/**
 * 检查当前 JVM 主要 Java 版本的实用工具类。
 */
public final class JavaVersion {

    private static final int majorJavaVersion = determineMajorJavaVersion();

    private JavaVersion() {
    }

    /**
     * 确定当前 JVM 的主要 Java 版本。
     *
     * @return 主要 Java 版本
     */
    private static int determineMajorJavaVersion() {
        String javaVersion = System.getProperty("java.version");
        return getMajorJavaVersion(javaVersion);
    }


    /**
     * 解析 Java 版本号中的主要版本号。
     *
     * @param javaVersion Java 版本号
     * @return 主要 Java 版本号
     */
    static int getMajorJavaVersion(String javaVersion) {
        int version = parseDotted(javaVersion);
        if (version == -1) {
            version = extractBeginningInt(javaVersion);
        }
        if (version == -1) {
            return 6;  // Choose minimum supported JDK version as default
        }
        return version;
    }

    /**
     * 解析格式为 X.Y.Z 或 X_Y_Z 的版本号中的主要版本号。
     *
     * @param javaVersion 版本号
     * @return 主要版本号
     */
    private static int parseDotted(String javaVersion) {
        try {
            String[] parts = javaVersion.split("[._]");
            int firstVer = Integer.parseInt(parts[0]);
            if (firstVer == 1 && parts.length > 1) {
                return Integer.parseInt(parts[1]);
            } else {
                return firstVer;
            }
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * 从版本号的开头提取主要版本号。
     *
     * @param javaVersion 版本号
     * @return 主要版本号
     */
    private static int extractBeginningInt(String javaVersion) {
        try {
            StringBuilder num = new StringBuilder();
            for (int i = 0; i < javaVersion.length(); ++i) {
                char c = javaVersion.charAt(i);
                if (Character.isDigit(c)) {
                    num.append(c);
                } else {
                    break;
                }
            }
            return Integer.parseInt(num.toString());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * 获取当前 JVM 的主要 Java 版本。
     * i.e. '8' for Java 1.8, '9' for Java 9 etc.*
     *
     * @return 主要 Java 版本号
     */
    public static int getMajorJavaVersion() {
        return majorJavaVersion;
    }

    /**
     * 判断当前应用程序是否运行在 Java 9 或更高版本上。
     *
     * @return 如果是，则返回 true；否则，返回 false。
     */
    public static boolean isJava9OrLater() {
        return majorJavaVersion >= 9;
    }
}
