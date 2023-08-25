package com.example.fastreaddemo.data

/**
 * 快速阅读题型
 * @author wzh
 * @date 2023/5/29
 */
enum class FastReadType(val type: Int, val gameName: String) : java.io.Serializable {

    /**
     * 横向之字移动
     */
    HORIZONTAL_ZHI_ZI_PATH(1, "横向之字移动"),

    /**
     * 纵向之字移动
     */
    VERTICAL_ZHI_ZI_PATH(2, "纵向之字移动"),

    /**
     * 圆圈顺向移动
     */
    CIRCLE_CLOCKWISE(3, "圆圈顺向移动"),

    /**
     * 圆圈逆向移动
     */
    CIRCLE_ANTICLOCKWISE(4, "圆圈逆向移动"),


    /**
     * 凝视点训练
     */
    FOCAL_POINT(5, "凝视点训练"),


    /**
     * 对角线顺向移动
     */
    DIAGONAL_PATH(6, "对角线顺向移动"),

    /**
     * 对角线逆向移动
     */
    DIAGONAL_REVERSE_PATH(7, "对角线逆向移动"),


    /**
     * 对角线数字移动
     */
    DIAGONAL_FLASH(8, "对角线数字移动"),

    /**
     * 五角星移动
     */
    STAR_PATH(9, "五角星移动"),

    /**
     * 横向8字移动
     */
    HORIZONTAL_8_PATH(10, "横向8字移动"),

    /**
     * 纵向8字移动
     */
    VERTICAL_8_PATH(11, "纵向8字移动"),

    /**
     * 箭头扩散
     */
    ARROW_SPREAD(12, "箭头扩散"),


    /**
     * 圆环扩散
     */
    CIRCLE_SPREAD(13, "圆环扩散"),

    /**
     * 方框扩散
     */
    RECT_SPREAD(14, "方框扩散"),


    /**
     * 文字四周扩散
     */
    TEXT_SPREAD(15, "文字四周扩散"),


    /**
     * 圆周扩大
     */
    ENLARGE_CIRCLE(16, "圆周扩大"),

    /**
     * 矩形扩大
     */
    ENLARGE_RECT(17, "方框扩大"),


    /**
     * 视点止移
     */
    RECT_FLASH_MOVE(18, "视点止移"),


    /**
     * 数字舒尔特方格
     */
    NUMBER_GRID_GAME(19, "数字舒尔特方格"),


    /**
     * 跳读训练
     */
    SKIP_READ_TRAIN(20, "跳读训练"),

    /**
     * 字母舒尔特方格
     */
    LETTER_GRID_GAME(21, "字母舒尔特方格"),


    /**
     * 古诗舒尔特方格
     */
    ANCIENT_POETRY_GRID_GAME(22, "古诗舒尔特方格"),


    /**
     * 数字闪现
     */
    NUMBER_FLASH(23, "数字闪现"),

    /**
     * 字母闪现
     */
    ALPHABET_FLASH(24, "字母闪现"),


    /**
     * 一目十行
     */
    TEN_LINE_READ(25, "一目十行"),


    /**
     * 符号闪现
     */
    SYMBOL_FLASH(26, "符号闪现"),

    /**
     * 图片闪现
     */
    PICTURE_FLASH(27, "图片闪现"),

    /**
     * 句子闪现
     */
    SENTENCE_FLASH(28, "句子闪现"),


    /**
     * 文章分节阅读
     */
    ARTICLE_PART_READ(29, "文章分节阅读"),


    /**
     * 文章闪现阅读
     */
    ARTICLE_FLASH(30, "文章闪现阅读"),


    /**
     * 文章测试
     */
    ARTICLE_TEST(31, "文章测试"),


}