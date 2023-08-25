package com.example.fastreaddemo.data

/**
 * 游戏参数配置
 * @author wzh
 * @date 2023/5/29
 */
class GameConfig(
    val gameType: FastReadType,
    val duration: Int = 3,
    val repeat: Int = 1,
    var extra: Any? = null

) : java.io.Serializable {

}