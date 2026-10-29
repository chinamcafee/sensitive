package com.github.houbb.chars.scan.util;

/**
 * https://www.cnblogs.com/cc11001100/p/9357177.html
 *
 * @author binbin.hou
 * @since 1.20.0
 */
public final class InnerBankLuhnHelper {

    private InnerBankLuhnHelper(){}

    /**
     * 是否符合 luna 算法
     *
     * 1. 从右到左给卡号字符串编号，最右边第一位是1，最右边第二位是2，最右边第三位是3….
     * 2. 从右向左遍历，对每一位字符t执行第三个步骤，并将每一位的计算结果相加得到一个数s。
     * 3. 对每一位的计算规则：如果这一位是奇数位，则返回t本身，
     * 如果是偶数位，则先将t乘以2得到一个数n，如果n是一位数（小于10），直接返回n，
     * 否则将n的个位数和十位数相加返回。
     * 4. 如果s能够整除10，则此号码有效，否则号码无效。
     *
     * 因为最终的结果会对10取余来判断是否能够整除10，所以又叫做模10算法。
     *
     * 5432123456788881 合法
     * @param cardNo 卡号
     * @return 结果
     */
    public static boolean isValidBankNo(StringBuilder cardNo) {
        int sum = calcSum(cardNo, 1);

        return sum % 10 == 0;
    }

    /**
     * 计算总和
     * @param cardNo 卡号
     * @param count 总数
     * @return 结果
     */
    private static int calcSum(StringBuilder cardNo, int count) {
        int length = cardNo.length();

        int sum = 0;
        for(int i = length; i > 0; i--) {
            int num = InnerCharUtil.getCharInt(cardNo.charAt(i-1));

            // 奇数位
            if(count % 2 != 0) {
                sum += num;
            } else {
                int doubleNum = num * 2;
                if(doubleNum < 10) {
                    sum += doubleNum;
                } else {
                    int doubleNumFirst = doubleNum / 10;
                    int doubleNumSecond = doubleNum % 10;
                    sum += doubleNumFirst;
                    sum += doubleNumSecond;
                }
            }

            // 位数变化
            count++;
        }

        return sum;
    }

}
