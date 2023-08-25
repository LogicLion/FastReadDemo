package com.example.fastreaddemo.data

/**
 * 答题选项
 * @author wzh
 * @date 2023/8/25
 */
class FastReadOption(
    val title: String? = null,
    val img: String? = null,

    /**
     * 0:默认，1：正确，2：错误
     */
    var status: Int = 0
) {
}