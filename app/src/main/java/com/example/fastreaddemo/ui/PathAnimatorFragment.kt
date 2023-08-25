package com.example.fastreaddemo.ui

import android.content.res.Configuration
import android.view.Gravity
import android.widget.FrameLayout
import com.example.fastreaddemo.R
import com.example.fastreaddemo.data.FastReadType
import com.example.fastreaddemo.data.RectFlashMoveData
import com.example.fastreaddemo.databinding.FastreadFragmentPathAnimatorBinding
import com.example.fastreaddemo.widget.*

/**
 * @author wzh
 * @date 2023/5/29
 */
class PathAnimatorFragment : BaseFastReadFragment<FastreadFragmentPathAnimatorBinding>() {
    override fun setupGameLayoutId() = R.layout.fastread_fragment_path_animator
    override fun initGameView() {
        mGameBinding.tvSecond.text = "0s"
        val viewContainer = mGameBinding.viewContainer

        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        val currentOrientation = resources.configuration.orientation
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutParams.width = FrameLayout.LayoutParams.MATCH_PARENT
            layoutParams.height = FrameLayout.LayoutParams.WRAP_CONTENT
        } else {
            layoutParams.width = FrameLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = FrameLayout.LayoutParams.MATCH_PARENT
        }

        val extra = gameConfig.extra
        val animatorView = when (gameConfig.gameType) {
            FastReadType.CIRCLE_CLOCKWISE -> CirclePathView(requireActivity())
            FastReadType.CIRCLE_ANTICLOCKWISE -> CirclePathView(requireActivity()).apply {
                isClockWise = false
            }

            FastReadType.STAR_PATH -> StarPathView(requireActivity())
            FastReadType.HORIZONTAL_8_PATH -> DoubleCirclePathView(requireActivity())
            FastReadType.VERTICAL_8_PATH -> DoubleCirclePathView(requireActivity()).apply {
                isHorizontal = false
            }

            FastReadType.HORIZONTAL_ZHI_ZI_PATH -> BallLinePathView(requireActivity()).apply {
                direct = false
                connectLineType = BallConnectLineType.ZHI_ZI_LINE
            }

            FastReadType.VERTICAL_ZHI_ZI_PATH -> BallLinePathView(requireActivity()).apply {
                direct = true
                connectLineType = BallConnectLineType.ZHI_ZI_LINE
            }

            FastReadType.DIAGONAL_PATH -> BallLinePathView(requireActivity()).apply {
                direct = false
                connectLineType = BallConnectLineType.DIAGONAL_LINE
            }

            FastReadType.DIAGONAL_REVERSE_PATH -> BallLinePathView(requireActivity()).apply {
                direct = true
                connectLineType = BallConnectLineType.DIAGONAL_LINE
            }

            FastReadType.ENLARGE_CIRCLE -> EnlargeCircleRectView(requireActivity())
            FastReadType.ENLARGE_RECT -> EnlargeCircleRectView(requireActivity()).apply {
                isRect = true
            }
            FastReadType.ARROW_SPREAD -> ArrowSpreadView(requireActivity())
            FastReadType.DIAGONAL_FLASH -> DiagonalFlashView(requireActivity())
            FastReadType.FOCAL_POINT -> FocalPointView(requireActivity())
            FastReadType.CIRCLE_SPREAD -> CircleRectSpreadView(requireActivity())
            FastReadType.RECT_SPREAD -> CircleRectSpreadView(requireActivity()).apply {
                isRect = true
            }
            FastReadType.RECT_FLASH_MOVE -> RectFlashMoveView(requireActivity()).apply {
                if (extra is RectFlashMoveData) {
                    lines = extra.line
                }
                layoutParams.width = FrameLayout.LayoutParams.MATCH_PARENT
                layoutParams.height = FrameLayout.LayoutParams.MATCH_PARENT
            }
            FastReadType.TEXT_SPREAD -> TextSpreadView(requireActivity()).apply {
                setText("恭喜发财")
            }
            else -> throw Exception("参数错误")
        }


        layoutParams.gravity = Gravity.CENTER_HORIZONTAL
        animatorView.layoutParams = layoutParams

        viewContainer.addView(animatorView)

        animatorView.setAnimationDuration(gameConfig.duration)
        animatorView.setAnimationRepeat(gameConfig.repeat)

        getTvPauseResume().setOnClickListener {
            if (animatorView.isAnimationRunning) {
                animatorView.pauseAnimation()
            } else {
                animatorView.startAnimation()
            }
        }
        animatorView.animatorListener = object : CustomAnimatorListener {
            override fun onAnimationStart() {
                getTvPauseResume().text = "暂停"
            }

            override fun onAnimationPause() {
                getTvPauseResume().text = "继续"
            }

            override fun onAnimationResume() {
                getTvPauseResume().text = "暂停"
            }

            override fun onAnimationEnd() {
                getTvPauseResume().text = "开始"
            }

            override fun onTimeCountDown(second: Int) {
                mGameBinding.tvSecond.text = "${second}s"
            }

        }

        mGameBinding.viewContainer.postDelayed({
            animatorView.startAnimation()
        }, 500)
    }

}