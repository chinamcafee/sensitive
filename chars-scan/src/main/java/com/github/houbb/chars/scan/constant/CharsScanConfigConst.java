package com.github.houbb.chars.scan.constant;

/**
 * 配置常量
 * @since 1.21.0
 */
public class CharsScanConfigConst {

    /**
     * 替换的字符
     */
    public static final int DEFAULT_START_INDEX = 0;

    /**
     * 默认前缀
     * @since 1.22.1
     */
    public static final String DEFAULT_PREFIX = ":：,，'\"‘“=| +-*/()（）【】[]{}><";

    /**
     * 默认前缀
     *
     * 在Java和许多其他编程语言中，有一些常见的转义序列，用于表示特殊字符或执行特殊的控制操作。
     *
     * 以下是一些常见的转义序列：
     *
     * 1. `\\`：反斜杠，用于表示单个反斜杠字符。
     * 2. `\'`：单引号，用于表示单引号字符。
     * 3. `\"`：双引号，用于表示双引号字符。
     * 这三个在单个字符的时候已经支持。
     *
     * 4. `\n`：换行符，表示新的一行。
     *
     * 5. `\r`：回车符，将光标移动到当前行的开头，而不换行。
     *
     * 6. `\t`：制表符，用于创建水平制表符。
     *
     * 7. `\b`：退格符，将光标向左移动一个位置。
     *
     * 8. `\f`：换页符，将光标移动到下一页。
     *
     * @since 1.30.1
     */
    public static final String DEFAULT_ESCAPE_PREFIX = "ntr";


}
