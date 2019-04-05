package com.kuyuzhiqi.acrossdemo.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.kuyuzhiqi.testdemo.R
import com.kuyuzhiqi.testdemo.Status

class SwitchView : View {
    private var mWidth = 0f
    private var mHeight = 0f

    private val bigCirclePaint = Paint()
    private val smallCirclePaint = Paint()
    private var bigRadius = 0f
    private var smallRadius = 0f
    private lateinit var powerBmp: Bitmap
    private lateinit var loadBmp: Bitmap
    private val circleLinePaint = Paint()
    private val pathPaint = Paint()
    private var lineColor = Color.parseColor("#60C4A8")
    private val lineWidth = 5f

    private val loadBmpMatrix = Matrix()
    private lateinit var lineHeadBmp: Bitmap
    private var distance = 0f
    private val step = 2f
    private var status: Int = Status.NOT_CONNECTED

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {

        initPaint()
    }

    private fun initPaint() {
        bigCirclePaint.color = Color.WHITE
        bigCirclePaint.isAntiAlias = true

        smallCirclePaint.color = Color.GRAY
        smallCirclePaint.isAntiAlias = true

        pathPaint.isAntiAlias = true
        pathPaint.isAntiAlias = true
        pathPaint.color = lineColor
        pathPaint.style = Paint.Style.STROKE
        pathPaint.strokeWidth = lineWidth

        circleLinePaint.isAntiAlias = true
        circleLinePaint.color = lineColor
        circleLinePaint.style = Paint.Style.STROKE
        circleLinePaint.strokeWidth = lineWidth
    }

    private fun init() {
        bigRadius = Math.min(mWidth, mHeight) / 2
        smallRadius = bigRadius * 0.8f

        powerBmp = BitmapFactory.decodeResource(resources, R.mipmap.power)

        lineHeadBmp = BitmapFactory.decodeResource(resources, R.mipmap.vinyl)
        loadBmp = Bitmap.createBitmap(
            (smallRadius + lineHeadBmp.height / 2).toInt() * 2,
            (smallRadius + lineHeadBmp.height / 2).toInt() * 2,
            Bitmap.Config.ARGB_8888
        )
        val centerXY = loadBmp.width.toFloat() / 2
        val sweepMatrix = Matrix()
        sweepMatrix.setRotate(-90f, centerXY, centerXY)
        val sweepGradient = SweepGradient(centerXY, centerXY, intArrayOf(Color.WHITE, lineColor), null)
        sweepGradient.setLocalMatrix(sweepMatrix)
        circleLinePaint.shader = sweepGradient
        Canvas(loadBmp).apply {
            drawCircle(centerXY, centerXY, smallRadius, circleLinePaint)
            drawBitmap(lineHeadBmp, (loadBmp.width - lineHeadBmp.width) / 2.toFloat(), 0f, null)
        }
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.apply {
            drawCircle(mWidth / 2, mHeight / 2, bigRadius, bigCirclePaint)
            drawBitmap(powerBmp, mWidth / 2 - powerBmp.width / 2, mHeight / 2 - powerBmp.height / 2, null)
        }
        when (status) {
            Status.CONNECTING, Status.DISCONNECTING -> {
                if (distance <= 1800f) {
                    loadBmpMatrix.reset()
                    loadBmpMatrix.postTranslate(bigRadius - loadBmp.width / 2, bigRadius - loadBmp.height / 2)
                    loadBmpMatrix.postRotate(distance, mWidth / 2, mHeight / 2)
                    canvas.drawBitmap(loadBmp, loadBmpMatrix, null)
                    distance += step
                } else {
                    distance %= 360
                }
                invalidate()
            }
            else -> {
                canvas.drawCircle(bigRadius, bigRadius, smallRadius, pathPaint)
                distance = 0f
            }
        }
    }

    open fun setStatus(status: Int) {
        this.status = status
        invalidate()
    }

    open fun getStatus(): Int {
        return status
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        mWidth = (right - left).toFloat()
        mHeight = (bottom - top).toFloat()
        init()
    }
}