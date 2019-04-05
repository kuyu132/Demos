package com.kuyuzhiqi.acrossdemo.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.kuyuzhiqi.testdemo.R
import com.kuyuzhiqi.testdemo.STU
import com.kuyuzhiqi.testdemo.Status

class StatusView : View {
    private var mWidth = 0f
    private var mHeight = 0f
    private var status: Int = Status.NOT_CONNECTED
    private val statusPaint = Paint()
    private val textPaint = Paint()
    private val linePaint = Paint()
    private var text = ""
    private var currentStatusStr: String
    private var linePath = Path()
    private val topTextPadding = 20f
    private val textBounds = Rect()
    private val lineWidth = 5f
    private val step = 2f
    private var bmpMatrix = Matrix()
    private lateinit var bmp: Bitmap
    private var bmpOffsetX = 0
    private var bmpOffsetY = 0
    private var pathLength = 0f
    private lateinit var pathMeasure: PathMeasure
    private var distance = 0f
    private var tan = FloatArray(2)
    private var pos = FloatArray(2)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {

        currentStatusStr = context.getString(R.string.current_status)
        setStatus(Status.NOT_CONNECTED)
        initView()
        initBmp()
    }

    private fun init() {
        initPath()
        invalidate()
    }

    private fun initView() {
        statusPaint.isAntiAlias = true
        statusPaint.color = Color.WHITE
        statusPaint.strokeWidth = 8f
        statusPaint.textSize = 64f

        textPaint.isAntiAlias = true
        textPaint.color = Color.WHITE
        textPaint.strokeWidth = 5f
        textPaint.textSize = 40f

        linePaint.strokeWidth = lineWidth
        linePaint.style = Paint.Style.STROKE
        linePaint.color = Color.WHITE

        textPaint.getTextBounds(currentStatusStr, 0, currentStatusStr.length, textBounds)
    }

    private fun initBmp() {
        bmp = BitmapFactory.decodeResource(resources, R.mipmap.vinyl)
        bmpOffsetX = bmp.width / 2
        bmpOffsetY = bmp.height / 2
    }

    private fun initPath() {
        val vOffset = textBounds.height().toFloat() / 2
        val xOffset = lineWidth + bmpOffsetX
        val yOffset = lineWidth + bmpOffsetY

        val radius = (mHeight - vOffset - yOffset) / 2
        val tmpDiff = (mWidth - textBounds.width() - topTextPadding * 2 - xOffset * 2 - radius * 2) / 2
        val startX = xOffset + radius + tmpDiff + topTextPadding * 2 + textBounds.width()

        val centerX1 = radius + xOffset
        val centerY1 = radius + vOffset

        val centerX2 = mWidth - (radius + xOffset)
        val centerY2 = radius + vOffset

        val controlPoints = ArrayList<PointF>().apply {
            add(PointF(centerX2 + radius * STU, vOffset))
            add(PointF(centerX2 + radius, centerY2 - radius * STU))
            add(PointF(centerX2 + radius, centerY2))

            add(PointF(centerX2 + radius, centerY2 + radius * STU))
            add(PointF(centerX2 + radius * STU, centerY2 + radius))
            add(PointF(centerX2, centerY2 + radius))

            add(PointF(centerX1 - radius * STU, mHeight - yOffset))
            add(PointF(xOffset, centerY1 + radius * STU))
            add(PointF(xOffset, centerY1))

            add(PointF(xOffset, centerY1 - radius * STU))
            add(PointF(centerX1 - radius * STU, vOffset))
            add(PointF(centerX1, vOffset))
        }


        linePath.moveTo(startX, vOffset)
        linePath.lineTo(mWidth - xOffset - radius, vOffset)
        linePath.cubicTo(
            controlPoints[0].x,
            controlPoints[0].y,
            controlPoints[1].x,
            controlPoints[1].y,
            controlPoints[2].x,
            controlPoints[2].y
        )
        linePath.cubicTo(
            controlPoints[3].x,
            controlPoints[3].y,
            controlPoints[4].x,
            controlPoints[4].y,
            controlPoints[5].x,
            controlPoints[5].y
        )

        linePath.lineTo(radius + xOffset, mHeight - yOffset)
        linePath.cubicTo(
            controlPoints[6].x,
            controlPoints[6].y,
            controlPoints[7].x,
            controlPoints[7].y,
            controlPoints[8].x,
            controlPoints[8].y
        )
        linePath.cubicTo(
            controlPoints[9].x,
            controlPoints[9].y,
            controlPoints[10].x,
            controlPoints[10].y,
            controlPoints[11].x,
            controlPoints[11].y
        )
        linePath.lineTo(xOffset + radius + tmpDiff, vOffset)

        pathMeasure = PathMeasure(linePath, false)
        pathLength = pathMeasure.length
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val statusBounds = Rect()
        statusPaint.getTextBounds(text, 0, text.length, statusBounds)
        canvas.apply {
            drawText(
                text, (measuredWidth / 2 - statusBounds.width() / 2).toFloat(),
                (measuredHeight / 2 + statusBounds.height() / 2).toFloat(), statusPaint
            )
            drawText(
                currentStatusStr, (measuredWidth / 2 - textBounds.width() / 2).toFloat(),
                textBounds.height().toFloat(), textPaint
            )
            drawPath(linePath, linePaint)
        }

        when (status) {
            Status.CONNECTING, Status.DISCONNECTING -> {
                if (distance < pathLength) {
                    pathMeasure.getPosTan(distance, pos, tan)
                    bmpMatrix.reset()
                    bmpMatrix.postTranslate(pos[0] - bmpOffsetX, pos[1] - bmpOffsetY)
                    canvas.drawBitmap(bmp, bmpMatrix, null)
                    distance += step
                } else {
                    distance = 0f
                }
                invalidate()
            }
            else -> {
            }
        }
    }

    fun setStatus(state: Int) {
        status = state
        when (status) {
            Status.NOT_CONNECTED -> text = context.getString(R.string.status_not_conntect)
            Status.CONNECTING -> text = context.getString(R.string.status_conntecting)
            Status.CONNECTED -> text = context.getString(R.string.status_connected)
            Status.DISCONNECTING -> text = context.getString(R.string.status_disconntecting)
        }
        invalidate()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        mWidth = (right - left).toFloat()
        mHeight = (bottom - top).toFloat()
        init()
    }

}