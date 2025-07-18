package com.software.jetpack.compose.chan_xin_android.util

import net.sourceforge.pinyin4j.PinyinHelper
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination

object PinAYinUtil {
    fun toHanYuPinYin(hanZi:String):String {
        val chars = hanZi.trim().toCharArray()
        var hanYuPinYin = ""
        val defaultFormat = HanyuPinyinOutputFormat()
        defaultFormat.caseType = HanyuPinyinCaseType.LOWERCASE
        defaultFormat.toneType = HanyuPinyinToneType.WITHOUT_TONE
        defaultFormat.vCharType = HanyuPinyinVCharType.WITH_V
        val hanZiRegex = Regex("[\\u4E00-\\u9FA5]+")
        try {
            chars.forEach {char->

                // 判断为中文,则转换为汉语拼音
                if (char.toString().matches(hanZiRegex)) {
                    hanYuPinYin += PinyinHelper
                        .toHanyuPinyinStringArray(char, defaultFormat)[0];
                } else {
                    // 不为中文,则不转换
                    hanYuPinYin += char;
                }
            }
        } catch ( e: BadHanyuPinyinOutputFormatCombination) {
            throw BadHanyuPinyinOutputFormatCombination("字符不能转成汉语拼音")
        }

        return hanYuPinYin;

    }
    fun getFirstLetter(hanZi:String):String {
        val chars = hanZi.trim().toCharArray()
        val firstPinyin = StringBuilder()
        val defaultFormat = HanyuPinyinOutputFormat()
        defaultFormat.caseType = HanyuPinyinCaseType.LOWERCASE
        defaultFormat.toneType = HanyuPinyinToneType.WITHOUT_TONE
        try {
            chars.forEach {char->
                if (char.toString().matches(Regex("[\\u4E00-\\u9FA5]+"))) {
                    val pys = PinyinHelper.toHanyuPinyinStringArray(char, defaultFormat);
                    firstPinyin.append(pys[0][0]);
                } else {
                    firstPinyin.append(char)
                }
            }
        } catch (e:BadHanyuPinyinOutputFormatCombination) {
            throw BadHanyuPinyinOutputFormatCombination("字符不能转为汉语拼音")
        }
        return firstPinyin.toString()
    }

}