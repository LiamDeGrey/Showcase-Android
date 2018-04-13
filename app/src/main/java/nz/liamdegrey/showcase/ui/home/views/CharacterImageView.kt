package nz.liamdegrey.showcase.ui.home.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.AppCompatImageView
import android.text.TextPaint
import android.util.AttributeSet
import nz.liamdegrey.showcase.R
import kotlin.properties.Delegates

class CharacterImageView : AppCompatImageView {
    private val textPaintSolid: TextPaint
    private val textPaintStroke: TextPaint

    private var character by Delegates.observable<String?>(null) { _, _, _ ->
        postInvalidate()
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setupAttributes(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setupAttributes(attrs, defStyleAttr)
    }

    init {
        background = ContextCompat.getDrawable(context, R.drawable.bg_semitransparent)

        val textSize = context.resources.getDimensionPixelSize(R.dimen.characterImageView_characterSize)
        val textStrokeWidth = context.resources.getDimensionPixelSize(R.dimen.strokeWidth)
        val textColorSolid = ContextCompat.getColor(context, R.color.blue)
        val textColorStroke = ContextCompat.getColor(context, R.color.darkGrey)
        val typeface = ResourcesCompat.getFont(context, R.font.lato_regular)

        textPaintSolid = TextPaint().apply {
            style = Paint.Style.FILL
            color = textColorSolid
            textAlign = Paint.Align.CENTER
            setTextSize(textSize.toFloat())
            setTypeface(Typeface.create(typeface, Typeface.BOLD))
        }

        textPaintStroke = TextPaint().apply {
            style = Paint.Style.STROKE
            strokeWidth = textStrokeWidth.toFloat()
            color = textColorStroke
            textAlign = Paint.Align.CENTER
            setTextSize(textSize.toFloat())
            setTypeface(Typeface.create(typeface, Typeface.BOLD))
        }
    }

    //region: Public methods

    fun updateCharacter(character: Char) {
        updateCharacter(character.toString())
    }

    fun updateCharacter(character: Int) {
        updateCharacter(character.toString())
    }

    fun updateCharacter(character: String) {
        character.first()
                .takeUnless { it.isWhitespace() }
                ?.let { this.character = character }
    }

    //endregion

    private fun setupAttributes(attrs: AttributeSet, defStyleAttr: Int = 0, defStyleRes: Int = 0) {
        val attributes = context.theme.obtainStyledAttributes(attrs, R.styleable.CharacterImageView, defStyleAttr, defStyleRes)
        val character: String?
        val characterSize: Int?
        try {
            character = attributes.getString(R.styleable.CharacterImageView_character)
            characterSize = attributes.getDimensionPixelSize(R.styleable.CharacterImageView_characterSize, -1)
        } finally {
            attributes.recycle()
        }

        character?.let {
            updateCharacter(it)
        }

        characterSize?.takeIf { it != -1 }
                ?.let {
                    textPaintSolid.textSize = it.toFloat()
                    textPaintStroke.textSize = it.toFloat()
                }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        character?.let {
            val x = (width / 2).toFloat()
            val y = height / 2 - (textPaintStroke.descent() + textPaintStroke.ascent()) / 2

            canvas.drawText(it, x, y, textPaintSolid)
            canvas.drawText(it, x, y, textPaintStroke)
        }
    }
}