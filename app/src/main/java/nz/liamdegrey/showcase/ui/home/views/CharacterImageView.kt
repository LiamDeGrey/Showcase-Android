package nz.liamdegrey.showcase.ui.home.views

import android.content.Context
import android.graphics.*
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.text.TextPaint
import android.util.AttributeSet
import android.widget.ImageView
import nz.liamdegrey.showcase.R

class CharacterImageView : ImageView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)


    inner class CharacterDrawable(context: Context, character: Char?) :
            LayerDrawable(arrayOf(ContextCompat.getDrawable(context, R.drawable.bg_semitransparent))) {
        private val textPaintSolid: TextPaint
        private val textPaintStroke: TextPaint

        private val character: String = character!!.toString()

        init {
            val textSize = context.resources.getDimensionPixelSize(R.dimen.characterImageView_characterSize)
            val textStrokeWidth = context.resources.getDimensionPixelSize(R.dimen.strokeWidth)
            val textColorSolid = ContextCompat.getColor(context, R.color.blue)
            val textColorStroke = ContextCompat.getColor(context, R.color.darkGrey)
            val typeface = ResourcesCompat.getFont(context, R.font.lato_regular)

            textPaintSolid = TextPaint()
            textPaintSolid.style = Paint.Style.FILL
            textPaintSolid.textSize = textSize.toFloat()
            textPaintSolid.color = textColorSolid
            textPaintSolid.textAlign = Paint.Align.CENTER
            textPaintSolid.typeface = Typeface.create(typeface, Typeface.BOLD)

            textPaintStroke = TextPaint()
            textPaintStroke.style = Paint.Style.STROKE
            textPaintStroke.textSize = textSize.toFloat()
            textPaintStroke.strokeWidth = textStrokeWidth.toFloat()
            textPaintStroke.color = textColorStroke
            textPaintStroke.textAlign = Paint.Align.CENTER
            textPaintStroke.typeface = Typeface.create(typeface, Typeface.BOLD)
        }

        override fun getIntrinsicWidth(): Int = width

        override fun getIntrinsicHeight(): Int = height

        override fun draw(canvas: Canvas) {
            super.draw(canvas)

            val x = (width / 2).toFloat()
            val y = height / 2 - (textPaintStroke.descent() + textPaintStroke.ascent()) / 2

            canvas.drawText(character, x, y, textPaintSolid)
            canvas.drawText(character, x, y, textPaintStroke)
        }

        override fun setAlpha(i: Int) {}

        override fun setColorFilter(colorFilter: ColorFilter?) {}

        override fun getOpacity(): Int = PixelFormat.OPAQUE
    }
}