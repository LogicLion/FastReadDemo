package com.example.fastreaddemo.utils

import com.example.fastreaddemo.data.FastReadType

/**
 * 匹配游戏类型
 * @author wzh
 * @date 2023/8/22
 */
fun matchGameType(type: Int): FastReadType? {
    return enumValues<FastReadType>().find { it.type == type }
}