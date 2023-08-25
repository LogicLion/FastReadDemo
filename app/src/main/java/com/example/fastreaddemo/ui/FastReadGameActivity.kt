package com.example.fastreaddemo.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.fastreaddemo.R
import com.example.fastreaddemo.base.DataBindingActivity
import com.example.fastreaddemo.data.FastReadType
import com.example.fastreaddemo.data.GameConfig
import com.example.fastreaddemo.databinding.FastreadActivityGameBinding

/**
 * 快速阅读游戏界面
 * @author wzh
 * @date 2023/5/26
 */
class FastReadGameActivity : DataBindingActivity() {

    lateinit var gameTitle: String

    override fun setupLayoutId() = R.layout.fastread_activity_game

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = getViewBinding<FastreadActivityGameBinding>()
        binding.ivBack.setOnClickListener { finish() }

        val gameConfig = intent.extras?.getSerializable("gameConfig") as? GameConfig

        if (gameConfig != null) {

            val fragment = setGameFragment(gameConfig)
            val bundle = Bundle()
            bundle.putSerializable("gameConfig", gameConfig)
            fragment.arguments = bundle

            if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction()
                    .add(R.id.fl_content, fragment)
                    .commit()
            }

            gameTitle = gameConfig.gameType.gameName
            binding.toolbarTitle.text = gameTitle
        }
    }


    private fun setGameFragment(gameConfig: GameConfig): Fragment {

        return when (gameConfig.gameType) {

            FastReadType.HORIZONTAL_ZHI_ZI_PATH,
            FastReadType.VERTICAL_ZHI_ZI_PATH,
            FastReadType.CIRCLE_CLOCKWISE,
            FastReadType.CIRCLE_ANTICLOCKWISE,
            FastReadType.FOCAL_POINT,
            FastReadType.DIAGONAL_PATH,
            FastReadType.DIAGONAL_REVERSE_PATH,
            FastReadType.DIAGONAL_FLASH,
            FastReadType.STAR_PATH,
            FastReadType.HORIZONTAL_8_PATH,
            FastReadType.VERTICAL_8_PATH,
            FastReadType.ARROW_SPREAD,
            FastReadType.CIRCLE_SPREAD,
            FastReadType.RECT_SPREAD,
            FastReadType.TEXT_SPREAD,
            FastReadType.ENLARGE_CIRCLE,
            FastReadType.ENLARGE_RECT,
            FastReadType.RECT_FLASH_MOVE -> {
                PathAnimatorFragment()
            }
            FastReadType.NUMBER_GRID_GAME, FastReadType.LETTER_GRID_GAME,
            FastReadType.ANCIENT_POETRY_GRID_GAME -> GridGameFragment()
            FastReadType.ALPHABET_FLASH, FastReadType.SYMBOL_FLASH -> CharacterFlashFragment()
            FastReadType.NUMBER_FLASH -> NumberFlashFragment()
            FastReadType.SKIP_READ_TRAIN -> SkipReadTrainFragment()
            FastReadType.SENTENCE_FLASH -> SentenceFlashFragment()
            FastReadType.PICTURE_FLASH->PictureFlashFragment()
            else -> throw Exception("参数错误")
        }
    }

    companion object {
        fun actionStart(
            packageContext: Context,
            gameConfig: GameConfig
        ) {
            val intent = Intent(packageContext, FastReadGameActivity::class.java)
            intent.putExtra("gameConfig", gameConfig)
            packageContext.startActivity(intent)
        }
    }


}