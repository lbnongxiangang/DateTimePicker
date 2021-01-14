package com.loper7.date_time_picker

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import com.loper7.date_time_picker.DateTimeConfig.DAY
import com.loper7.date_time_picker.DateTimeConfig.HOUR
import com.loper7.date_time_picker.DateTimeConfig.MIN
import com.loper7.date_time_picker.DateTimeConfig.MONTH
import com.loper7.date_time_picker.DateTimeConfig.SECOND
import com.loper7.date_time_picker.DateTimeConfig.YEAR
import com.loper7.date_time_picker.common.DateTimeController
import com.loper7.date_time_picker.common.DateTimeInterface
import com.loper7.date_time_picker.number_picker.NumberPicker
import com.loper7.tab_expand.ext.dip2px
import com.loper7.tab_expand.ext.px2dip
import org.jetbrains.annotations.NotNull
import java.lang.Exception

class DateTimePicker : FrameLayout, DateTimeInterface {

    private var mYearSpinner: NumberPicker? = null
    private var mMonthSpinner: NumberPicker? = null
    private var mDaySpinner: NumberPicker? = null
    private var mHourSpinner: NumberPicker? = null
    private var mMinuteSpinner: NumberPicker? = null
    private var mSecondSpinner: NumberPicker? = null

    private var displayType = intArrayOf(YEAR, MONTH, DAY, HOUR, MIN, SECOND)

    private var showLabel = true
    private var themeColor = 0
    private var textSize = 0

    private var yearLabel = "年"
    private var monthLabel = "月"
    private var dayLabel = "日"
    private var hourLabel = "时"
    private var minLabel = "分"
    private var secondLabel = "秒"

    private var layoutResId = R.layout.layout_date_picker

    private lateinit var controller: DateTimeController


    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : this(context, attrs)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val attributesArray = context.obtainStyledAttributes(attrs, R.styleable.DateTimePicker)
        showLabel = attributesArray.getBoolean(R.styleable.DateTimePicker_showLabel, true)
        themeColor = attributesArray.getColor(
            R.styleable.DateTimePicker_themeColor,
            ContextCompat.getColor(context, R.color.colorAccent)
        )
        textSize =
            context.px2dip(
                attributesArray.getDimensionPixelSize(
                    R.styleable.DateTimePicker_textSize,
                    context.dip2px(15f)
                ).toFloat()
            )
        layoutResId = attributesArray.getResourceId(
            R.styleable.DateTimePicker_layout,
            R.layout.layout_date_picker
        )
        attributesArray.recycle()
        init()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    private fun init() {
        removeAllViews()
        try {
            View.inflate(context, layoutResId, this)
        }catch (e:Exception){
            throw Exception("layoutResId is it right or not?")
        }

        mYearSpinner = findViewById(R.id.np_datetime_year)
        if (mYearSpinner == null)
            mYearSpinner = findViewWithTag("np_datetime_year")
        mMonthSpinner = findViewById(R.id.np_datetime_month)
        if (mMonthSpinner == null)
            mMonthSpinner = findViewWithTag("np_datetime_month")
        mDaySpinner = findViewById(R.id.np_datetime_day)
        if (mDaySpinner == null)
            mDaySpinner = findViewWithTag("np_datetime_day")
        mHourSpinner = findViewById(R.id.np_datetime_hour)
        if (mHourSpinner == null)
            mHourSpinner = findViewWithTag("np_datetime_hour")
        mMinuteSpinner = findViewById(R.id.np_datetime_minute)
        if (mMinuteSpinner == null)
            mMinuteSpinner = findViewWithTag("np_datetime_minute")
        mSecondSpinner = findViewById(R.id.np_datetime_second)
        if (mSecondSpinner == null)
            mSecondSpinner = findViewWithTag("np_datetime_second")

        setThemeColor(themeColor)
        setTextSize(textSize)
        showLabel(showLabel)

        controller = DateTimeController().bindPicker(YEAR, mYearSpinner)
            .bindPicker(MONTH, mMonthSpinner)
            .bindPicker(DAY, mDaySpinner).bindPicker(HOUR, mHourSpinner)
            .bindPicker(MIN, mMinuteSpinner).bindPicker(SECOND, mSecondSpinner).build()
    }


    fun setLayout(@NotNull layout: Int) {
        if (layout == 0)
            return
        layoutResId = layout
        init()
    }

    /**
     * 设置显示类型
     *
     * @param types
     */
    fun setDisplayType(types: IntArray?) {
        if (types == null || types.isEmpty()) return
        displayType = types

        if (!displayType.contains(YEAR)) {
            mYearSpinner?.visibility = View.GONE
        }

        if (!displayType.contains(MONTH)) {
            mMonthSpinner?.visibility = View.GONE
        }

        if (!displayType.contains(DAY)) {
            mDaySpinner?.visibility = View.GONE
        }

        if (!displayType.contains(HOUR)) {
            mHourSpinner?.visibility = View.GONE
        }

        if (!displayType.contains(MIN)) {
            mMinuteSpinner?.visibility = View.GONE
        }

        if (!displayType.contains(SECOND)) {
            mSecondSpinner?.visibility = View.GONE
        }
    }

    /**
     * 是否显示label
     *
     * @param b
     */
    fun showLabel(b: Boolean) {
        showLabel = b
        if (b) {
            mYearSpinner?.label = yearLabel
            mMonthSpinner?.label = monthLabel
            mDaySpinner?.label = dayLabel
            mHourSpinner?.label = hourLabel
            mMinuteSpinner?.label = minLabel
            mSecondSpinner?.label = secondLabel
        } else {
            mYearSpinner?.label = ""
            mMonthSpinner?.label = ""
            mDaySpinner?.label = ""
            mHourSpinner?.label = ""
            mMinuteSpinner?.label = ""
            mSecondSpinner?.label = ""
        }
    }

    /**
     * 主题颜色
     *
     * @param color
     */
    fun setThemeColor(@ColorInt color: Int) {
        if (color == 0) return
        themeColor = color
        mYearSpinner?.setTextColor(themeColor)
        mMonthSpinner?.setTextColor(themeColor)
        mDaySpinner?.setTextColor(themeColor)
        mHourSpinner?.setTextColor(themeColor)
        mMinuteSpinner?.setTextColor(themeColor)
        mSecondSpinner?.setTextColor(themeColor)

    }

    /**
     * 字体大小
     *
     * @param sp
     */
    fun setTextSize(@Dimension sp: Int) {
        if (sp == 0) return
        textSize = sp
        mYearSpinner?.setTextSize(textSize)
        mMonthSpinner?.setTextSize(textSize)
        mDaySpinner?.setTextSize(textSize)
        mHourSpinner?.setTextSize(textSize)
        mMinuteSpinner?.setTextSize(textSize)
        mSecondSpinner?.setTextSize(textSize)

    }

    /**
     * 设置标签文字
     * @param year 年标签
     * @param month 月标签
     * @param day 日标签
     * @param hour 时标签
     * @param min 分标签
     *  @param min 秒标签
     */
    fun setLabelText(
        year: String = yearLabel,
        month: String = monthLabel,
        day: String = dayLabel,
        hour: String = hourLabel,
        min: String = minLabel,
        second: String = secondLabel
    ) {
        this.yearLabel = year
        this.monthLabel = month
        this.dayLabel = day
        this.hourLabel = hour
        this.minLabel = min
        this.secondLabel = second
        showLabel(showLabel)
    }


    /**
     * 设置是否Picker循环滚动
     * @param types 需要设置的Picker类型（DateTimeConfig-> YEAR,MONTH,DAY,HOUR,MIN,SECOND）
     * @param wrapSelector 是否循环滚动
     */
    fun setWrapSelectorWheel(vararg types: Int, wrapSelector: Boolean) {
        setWrapSelectorWheel(types.toMutableList(), wrapSelector)
    }

    /**
     * 设置是否Picker循环滚动
     * @param wrapSelector 是否循环滚动
     */
    fun setWrapSelectorWheel(wrapSelector: Boolean) {
        setWrapSelectorWheel(null, wrapSelector)
    }


    override fun setDefaultMillisecond(time: Long) {
        controller.setDefaultMillisecond(time)
    }

    override fun setMinMillisecond(time: Long) {
        controller.setMinMillisecond(time)
    }

    override fun setMaxMillisecond(time: Long) {
        controller.setMaxMillisecond(time)
    }

    override fun setWrapSelectorWheel(types: MutableList<Int>?, wrapSelector: Boolean) {
        controller.setWrapSelectorWheel(types, wrapSelector)
    }

    override fun setOnDateTimeChangedListener(callback: ((Long) -> Unit)?) {
        controller.setOnDateTimeChangedListener(callback)
    }


}