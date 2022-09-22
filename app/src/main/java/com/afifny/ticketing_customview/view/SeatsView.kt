package com.afifny.ticketing_customview.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import android.view.KeyEvent.ACTION_DOWN
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.afifny.ticketing_customview.R
import com.afifny.ticketing_customview.model.Seats

class SeatsView : View {
    var seat: Seats? = null;
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val seats: ArrayList<Seats> = arrayListOf(
        Seats(id = 1, name = "A1", isBooked = false),
        Seats(id = 2, name = "A2", isBooked = false),
        Seats(id = 3, name = "A3", isBooked = false),
        Seats(id = 4, name = "A4", isBooked = false),
        Seats(id = 5, name = "A5", isBooked = false),
        Seats(id = 6, name = "A6", isBooked = false),
        Seats(id = 7, name = "A7", isBooked = false),
        Seats(id = 8, name = "A8", isBooked = false)
    )

//    untuk menentukan posisi X dan Y dari masing-masing kursi yang kosong
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        val height = getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)

        val halfOfHeight = height / 2
        val halfOfWidth = width / 2
        var value = -600f

        for (i in 0..7) {
            if (i.mod(2) == 0) {
                seats[i].apply {
                    x = halfOfWidth - 300f
                    y = halfOfHeight + value
                }
            } else{
                seats[i].apply {
                    x = halfOfWidth + 100f
                    y = halfOfHeight + value
                }
                value += 300F
            }
        }
    }

    private val backgroundPaint = Paint()
    private val armrestPaint = Paint()
    private val bottomSeatsPaint = Paint()
    private val mBounds = Rect()
    private val numberSeatsPaint = Paint(Paint.FAKE_BOLD_TEXT_FLAG)
    private val titlePaint = Paint(Paint.FAKE_BOLD_TEXT_FLAG)

    /*Dalam metode onDraw, terdapat sebuah canvas yang bisa langsung kita gunakan untuk menggambar.
    Karena terdapat 8 kursi yang sama, kita cukup melakukan perulangan saja.
    Hal terpentingnya  adalah kita dapat mengetahui posisi x dan y dari masing-masing kursi.*/

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        for (seat in seats) {
            drawSeat(canvas, seat)
        }

        val text = "Silakan pilih kursi"
        titlePaint.apply {
            textSize = 50F
        }
        canvas?.drawText(text, (width/2) - 197F, 100F, titlePaint)
    }

    private fun drawSeat(canvas: Canvas?, seat: Seats) {
        // mengatur warna booked
        if (seat.isBooked) {
            backgroundPaint.color = ResourcesCompat.getColor(resources, R.color.grey_200, null)
            armrestPaint.color = ResourcesCompat.getColor(resources, R.color.grey_200, null)
            bottomSeatsPaint.color = ResourcesCompat.getColor(resources, R.color.grey_200, null)
            numberSeatsPaint.color = ResourcesCompat.getColor(resources, R.color.black, null)
        } else {
            backgroundPaint.color = ResourcesCompat.getColor(resources, R.color.blue_500, null)
            armrestPaint.color = ResourcesCompat.getColor(resources, R.color.blue_700, null)
            bottomSeatsPaint.color = ResourcesCompat.getColor(resources, R.color.blue_200, null)
            numberSeatsPaint.color = ResourcesCompat.getColor(resources, R.color.grey_200, null)
        }
        // menyimpan state
        canvas?.save()

        // background
        canvas?.translate(seat.x as Float, seat.y as Float)

        val backgroundPath = Path()
        backgroundPath.addRect(0F, 0F, 200F, 200F, Path.Direction.CCW)
        backgroundPath.addCircle(100F, 50F, 75F, Path.Direction.CCW)
        canvas?.drawPath(backgroundPath, backgroundPaint)

        // sandaran tangan
        val armrestPath = Path()
        armrestPath.addRect(0F, 0F, 50F, 200F, Path.Direction.CCW)
        canvas?.drawPath(armrestPath, armrestPaint)
        canvas?.translate(150F, 0F)
        armrestPath.addRect(0F, 0F, 50F, 200F, Path.Direction.CCW)
        canvas?.drawPath(armrestPath, armrestPaint)

        // bawah kursi
        canvas?.translate(-150F, 175F)
        val bottomSeatPath = Path()
        bottomSeatPath.addRect(0F, 0F, 200F, 25F, Path.Direction.CCW)
        canvas?.drawPath(bottomSeatPath, bottomSeatsPaint)

        // menulis nomor kursi
        canvas?.translate(0F, -175F)
        numberSeatsPaint.apply {
            textSize = 50F
            numberSeatsPaint.getTextBounds(seat.name, 0, seat.name.length, mBounds)
        }
        canvas?.drawText(seat.name, 100F - mBounds.centerX(), 100F, numberSeatsPaint)

        // mengembalikan pengaturan sebelumnya
        canvas?.restore()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val halfOfHeight = height / 2
        val halfOfWidth = width / 2

        val widthColumnOne = (halfOfWidth - 300F)..(halfOfWidth - 100F)
        val widthColumnTwo = (halfOfWidth + 100F)..(halfOfWidth + 300F)

        val heightRowOne = (halfOfHeight - 600F)..(halfOfHeight - 400F)
        val heightRowTwo = (halfOfHeight - 300F)..(halfOfHeight - 100F)
        val heightRowTree = (halfOfHeight + 0F)..(halfOfHeight + 200F)
        val heightRowFour =(halfOfHeight + 300F)..(halfOfHeight + 500F)

        if (event?.action == ACTION_DOWN) {
            when {
                event.x in widthColumnOne && event.y in heightRowOne -> booking(0)
                event.x in widthColumnTwo && event.y in heightRowOne -> booking(1)
                event.x in widthColumnOne && event.y in heightRowTwo -> booking(2)
                event.x in widthColumnTwo && event.y in heightRowTwo -> booking(3)
                event.x in widthColumnOne && event.y in heightRowTree -> booking(4)
                event.x in widthColumnTwo && event.y in heightRowTree -> booking(5)
                event.x in widthColumnOne && event.y in heightRowFour -> booking(6)
                event.x in widthColumnTwo && event.y in heightRowFour -> booking(7)
            }
        }
        return true
    }

    private fun booking(position: Int) {
        for (seat in seats) {
            seat.isBooked = false
        }
        seats[position].apply {
            seat = this
            isBooked = true
        }
        invalidate() // refresh metode onDraw dal seatsView
    }
}