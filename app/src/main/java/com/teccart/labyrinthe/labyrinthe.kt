package com.teccart.labyrinthe

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import kotlin.random.Random

class labyrinthe(context: Context, mode : Boolean) : View(context) {
    private val startPageLogo: Bitmap
    private val screenWidth = context.resources.displayMetrics.widthPixels
    private val screenHeight = context.resources.displayMetrics.heightPixels
    private val ball: Ball = Ball(Pair(1040, 1700), 26)
    private val walls: ArrayList<Murs> = ArrayList()
    private val holes: ArrayList<Trou> = ArrayList()
    private val wallThickness = 7 // Thickness of the walls
    private val spaceMargin = 33 // Extra space between walls and holes
    private val holewin : Trou = Trou(Pair(40,50), 40 )

    init {
        startPageLogo = BitmapFactory.decodeResource(resources, R.drawable.th)

        // Generate walls with extra space margin
        while (walls.size < 28) {
            val wall = Murs(
                Pair(Random.nextInt(spaceMargin, screenWidth - spaceMargin),
                    Random.nextInt(spaceMargin, screenHeight - spaceMargin)),
                250,
                if (Random.nextBoolean()) Murs.VERTICAL else Murs.HORIZONTAL
            )

            if (!doesObjectCollideWithWallsOrHoles(wall.getRect(wallThickness).expand(spaceMargin))) {
                walls.add(wall)
            }
        }

        // Generate holes with extra space margin
        while (holes.size < 25) {
            val hole = Trou(
                Pair(Random.nextInt(spaceMargin, screenWidth - spaceMargin),
                    Random.nextInt(spaceMargin, screenHeight - spaceMargin)),
                30 // Random radius
            )


            if (!doesObjectCollideWithWallsOrHoles(hole.getRect().expand(spaceMargin))) {
                holes.add(hole)
            }
        }
    }

    private fun doesObjectCollideWithWallsOrHoles(rect: Rect): Boolean {
        // Check against the ball
        if (rect.isColliding(ball.getRect().expand(spaceMargin))) {
            return true
        }

        // Check against the winning hole
        if (rect.isColliding(holewin.getRect().expand(spaceMargin))) {
            return true
        }
        return walls.any { it.getRect(wallThickness).expand(spaceMargin).isColliding(rect) } ||
                holes.any { it.getRect().expand(spaceMargin).isColliding(rect) }
    }

    private fun Rect.expand(margin: Int): Rect {
        return Rect(this.left - margin, this.top - margin, this.right + margin, this.bottom + margin)
    }


    private fun handleWinCondition() {
        // Implement game logic for when the ball reaches the win hole
        Toast.makeText(context, "Congratulations! You've won!", Toast.LENGTH_LONG).show()
        // Optionally reset the game or navigate to another screen
        ball.position = Pair(1040, 1700)
        val gameIntent = Intent(context, EndActivity::class.java)
        context.startActivity(gameIntent)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val scaledBitmap = Bitmap.createScaledBitmap(startPageLogo, screenWidth, screenHeight, true)

        // Now draw the scaled bitmap
        canvas.drawBitmap(scaledBitmap, 0f, 0f, null)

        // Define paint objects for drawing
        val wallPaint = Paint().apply { color = Color.RED }
        val holePaint = Paint().apply { color = Color.BLACK }
        val holewinPaint = Paint().apply { color = Color.GREEN }
        val ballPaint = Paint().apply { color = Color.LTGRAY }
        val textPaint = Paint().apply {
            color = Color.BLACK // Text color
            textSize = 25f // Text size
            textAlign = Paint.Align.CENTER // Align text to the center horizontally
        }

        // Draw the walls
        for (wall in walls) {
            val wallRect = wall.getRect(wallThickness)
            canvas.drawRect(wallRect, wallPaint)
        }

        // Draw the holes
        for (hole in holes) {
            val holeRect = hole.getRect()
            canvas.drawOval(holeRect.left.toFloat(), holeRect.top.toFloat(), holeRect.right.toFloat(), holeRect.bottom.toFloat(), holePaint)
        }

        // Draw th holewin
        val holewin = holewin.getRect()
        canvas.drawOval(holewin.left.toFloat(), holewin.top.toFloat(), holewin.right.toFloat(), holewin.bottom.toFloat(), holewinPaint)
        // Draw the label on the winning hole
        // Calculate the center position for the text
        val holewintextX = (holewin.left + holewin.right) / 2f
        val holewintextY = (holewin.top + holewin.bottom) / 2f - ((textPaint.descent() + textPaint.ascent()) / 2f)
        canvas.drawText("Sortie", holewintextX, holewintextY, textPaint)


        // Draw the ball
        val ballRect = ball.getRect()
        canvas.drawOval(ballRect.left.toFloat(), ballRect.top.toFloat(), ballRect.right.toFloat(), ballRect.bottom.toFloat(), ballPaint)
        val ballRecttextX = (ballRect.left + ballRect.right) / 2f
        val ballRecttextY = (ballRect.top + ballRect.bottom) / 2f - ((textPaint.descent() + textPaint.ascent()) / 2f)
        canvas.drawText("Player", ballRecttextX, ballRecttextY, textPaint)


    }

    private fun Rect.isColliding(other: Rect): Boolean {
        return Rect.intersects(this, other)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // Get the x and y coordinates of the touch event
        val x = event.x.toInt()
        val y = event.y.toInt()

        when (event.action) {
            /*MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                // Directly move the ball to the position of the touch
                moveBalla(x, y)
            }*/
            MotionEvent.ACTION_MOVE -> {
                // Directly move the ball to the position of the touch
                moveBall(x, y)
            }

        }
        return true
    }

     fun moveBall(x: Int, y: Int) {
        // Create a rectangle for the ball at the target position
        val ballNewRect = Rect(x - ball.radius, y - ball.radius, x + ball.radius, y + ball.radius)

        // Check for collision with walls
        val isCollisionWithWall = walls.any { wall ->
            val wallRect = wall.getRect(wallThickness)
            Rect.intersects(ballNewRect, wallRect)
        }

        // Check for collision with holes
        val isCollisionWithHole = holes.any { hole ->
            val holeRect = hole.getRect()
            Rect.intersects(ballNewRect, holeRect)
        }

        // If collision occurs, display a message and reset the ball's position
        if (isCollisionWithWall || isCollisionWithHole) {
            Toast.makeText(context, "Lost! Resetting position...", Toast.LENGTH_SHORT).show()
            // Reset the ball to its initial position
            ball.position = Pair(1040, 1700)
        } else {
            // Update the ball's position if no collision
            ball.position = Pair(x, y)
        }
        val isCollisionWithWinHole = Rect.intersects(ballNewRect, holewin.getRect())
        if (isCollisionWithWinHole) {
            // If ball reaches the win hole, perform the win logic
            handleWinCondition()
        }

        // Invalidate the view to redraw the ball at the new position
        invalidate()
    }

}

// Here, you should use the Murs, Trou, and Ball classes as defined in the previous response.



/*
class labyrinthe(context: Context?,var mode:Boolean)  : View(context) {
 var habitant:ArrayList<Iobstacle>
 init {
     habitant = ArrayList()
 }
    public  fun UpdateBall(X:Int,Y:Int)
    {

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        return true
    }
    public fun placeMur(nombre:Int)
    {

    }
    public fun placeTrou(nombre:Int)
    {

    }
    public fun placeBall()
    {

    }
    public fun placeExit()
    {

    }

}
*/



/*
class labyrinthe(context: Context) : View(context) {

    /*private val ball: Ball
    private val walls: ArrayList<Murs>
    private val holes:ArrayList<Trou>*/

    private val ball: Ball
    private val walls: MutableList<Murs>
    private val holes: MutableList<Trou>
    private val wallThickness = 10 // Épaisseur des murs



    init {
        ball = Ball()
        walls = mutableListOf()
        holes = mutableListOf()
        val screenWidth = context.resources.displayMetrics.widthPixels
        val screenHeight = context.resources.displayMetrics.heightPixels

         Générer des murs et des trous aléatoires
        for (i in 0 until 20) {
            val wall = Murs()
            val hole = Trou()

            do {
                wall.position[0] = Random.nextInt(screenWidth - wall.longueur)
                wall.position[1] = Random.nextInt(screenHeight - wall.D)
                hole.position[0] = Random.nextInt(screenWidth)
                hole.position[1] = Random.nextInt(screenHeight)

                // Définir la Rect pour le mur en fonction de son orientation et épaisseur
                val wallRect = if (wall.orientation == 0) {
                    Rect(wall.position[0], wall.position[1] - wall.longueur / 2, wall.position[0] + wallThickness, wall.position[1] + wall.longueur / 2)
                } else {
                    Rect(wall.position[0] - wall.longueur / 2, wall.position[1], wall.position[0] + wall.longueur / 2, wall.position[1] + wallThickness)
                }

                // Définir la Rect pour le trou
                val holeRect = Rect(hole.position[0] - hole.Rayon, hole.position[1] - hole.Rayon, hole.position[0] + hole.Rayon, hole.position[1] + hole.Rayon)

                // Vérifier si le mur ou le trou chevauche quelque chose déjà dans les listes
                val isWallColliding = walls.any { existingWall -> existingWall.getRect(wallThickness).isColliding(wallRect) }
                val isHoleColliding = holes.any { existingHole -> existingHole.getRect().isColliding(holeRect) }
            } while (isWallColliding || isHoleColliding)

            // Si aucune collision n'est détectée, ajouter le mur et le trou aux listes
            walls.add(wall)
            holes.add(hole)


        }
        }


   /* init {
        ball = Ball()
        walls = ArrayList()
        holes = ArrayList()

        // Generate random walls and holes
        for (i in 0..10) {

           /* if (!ball.checkcollision(wall.position[0], wall.position[1])) {
                walls.add(wall)
            }
            if (!ball.checkcollision(hole.position[0], hole.position[0])) {
                holes.add(hole)
            }*/

            val wall = Murs()
            val hole = Trou()


// Check for collisions between the new hole and existing holes
            // Check for collisions between the new wall and existing walls
            if (walls.none { it.checkcollision(wall.position[0], wall.position[1]) }) {
                // Check for collisions between the new wall and existing holes
                if (holes.none { it.checkcollision(wall.position[0], wall.position[1]) && !it.isOverlapping(hole) }) {
                    walls.add(wall)
                    holes.add(hole)
                }
            }
        }
    }*/

    fun moveBall(dx: Int, dy: Int) {
        // Update ball position
        ball.updateposition(ball.position[0] + dx, ball.position[1] + dy)

        // Check for collisions with walls
        for (wall in walls) {
            if (ball.checkcollision(wall.position[0], wall.position[1])) {
                // If there's a collision, move the ball back to its previous position
                /*ball.updateposition(ball.prevX, ball.prevY)
                break*/
            }
        }

        // Check for collisions with holes
        for (hole in holes) {
            if (ball.checkcollision(hole.position[0], hole.position[1])) {
                // If the ball falls into a hole, remove it from the screen
                ball.fall()
                invalidate()
                break
            }
        }

        // Invalidate the view to redraw the ball
        invalidate()
    }

    // Draw the ball, walls, and holes on the canvas
   /* override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //val ballPaint = Paint().apply { color = Color.RED }
        val wallPaint = Paint().apply { color = Color.RED }
        val holePaint = Paint().apply { color = Color.BLACK }
        // Constants for wall lengths
        val verticalWallLength = 100f  // Length of the vertical walls
        val horizontalWallLength = 100f  // Length of the horizontal walls

        //canvas.drawCircle(ball.X.toFloat(), ball.Y.toFloat(), ball.raduis.toFloat(), ballPaint)

        for (wall in walls) {

                val verticalWallStartY = Math.max(20f, wall.position[1] - verticalWallLength / 2) // Ensure the wall starts within the canvas bounds
                val verticalWallEndY = Math.min(height.toFloat(), wall.position[1] + verticalWallLength / 2) // Ensure the wall ends within the canvas bounds
                canvas.drawLine(wall.position[0].toFloat(), verticalWallStartY, wall.position[0].toFloat(), verticalWallEndY, wallPaint)

                // Draw horizontal wall with reduced length
                val horizontalWallStartX = Math.max(10f, wall.position[0] - horizontalWallLength / 2) // Ensure the wall starts within the canvas bounds
                val horizontalWallEndX = Math.min(width.toFloat(), wall.position[0] + horizontalWallLength / 2) // Ensure the wall ends within the canvas bounds

                canvas.drawLine(horizontalWallStartX, wall.position[1].toFloat(), horizontalWallEndX, wall.position[1].toFloat(), wallPaint)
            }



        for (hole in holes) {
            canvas.drawCircle(hole.position[0].toFloat(), hole.position[1].toFloat(), hole.Rayon.toFloat(), holePaint)
        }
    }*/

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val wallPaint = Paint().apply { color = Color.RED }
        val holePaint = Paint().apply { color = Color.BLACK }

        // Dessiner les murs
        for (wall in walls) {
            if (wall.orientation == 1) {
                val startX = wall.position[0].toFloat()
                val startY = (wall.position[1] - wall.longueur / 2).toFloat()
                val endY = (wall.position[1] + wall.longueur / 2).toFloat()
                canvas.drawRect(startX, startY, startX + wallThickness, endY, wallPaint)
            } else {
                val startX = (wall.position[0] - wall.longueur / 2).toFloat()
                val startY = wall.position[1].toFloat()
                val endX = (wall.position[0] + wall.longueur / 2).toFloat()
                canvas.drawRect(startX, startY, endX, startY + wallThickness, wallPaint)
            }
        }

        // Dessiner les trous
        for (hole in holes) {
            canvas.drawCircle(hole.position[0].toFloat(), hole.position[1].toFloat(), hole.Rayon.toFloat(), holePaint)
        }
    }
    private fun Rect.isColliding(other: Rect): Boolean {
        return Rect.intersects(this, other)
    }

}








/*


class Labyrinthe(context: Context?) :
    View(context),
    GestureDetector.OnGestureListener,
    GestureDetector.OnDoubleTapListener {

    private val TAG = Labyrinthe::class.java.simpleName
    private val GESTURE_THRESHOLD_DP = 15.dpToPx() // 15 dp
    private val MAX_BALLS = 1

    // Obstacles
    private var balls: MutableList<Ball> = mutableListOf()
    private var walls: MutableList<Murs> = mutableListOf()
    private var hole: Trou = Trou()
    private var exit: Trou = Trou()

    // Paint for drawing
    private var canvasPaint: Paint = Paint()
    private var ballPaint: Paint = Paint()
    private var wallPaint: Paint = Paint()

    // Drawing properties
    private var width: Int = 0
    private var height: Int = 0
    private var ballRadius: Int = 0

    // Gesture detector
    private var gestureDetector: GestureDetector? = null

    init {
        // Initialize paint
        canvasPaint.color = Color.WHITE
        ballPaint.color = Color.RED
        wallPaint.color = Color.BLACK

        // Initialize gesture detector
        gestureDetector = GestureDetector(context, this)
        gestureDetector!!.setOnDoubleTapListener(this)

        // Initialize ball properties
        ballRadius = dpToPx(30.0f)

        // Initialize obstacles
        createObstacles()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        width = w
        height = h
        super.onSizeChanged(w, h, oldw, oldh)
    }

    private fun createObstacles() {
        var numBalls = 0
        var numMurs = 0

        // Initialize balls
        while (numBalls < MAX_BALLS) {
            val ball = Ball()
            if (!isColliding(ball)) {
                addBall(ball)
                numBalls++
            }
        }

        // Initialize walls
        while (numMurs < 30) {
            val mur = Murs()
            if (!isColliding(mur)) {
                addWall(mur)
                numMurs++
            }
        }

        // Initialize hole
        val holePosition = IntArray(2)
        do {
            holePosition[0] = Random.nextInt(width)
            holePosition[1] = Random.nextInt(height)
        } while (isColliding(hole, holePosition))
        addTrou(hole, holePosition)

        // Initialize exit
        val exitPosition = IntArray(2)
        do {
            exitPosition[0] = Random.nextInt(width)
            exitPosition[1] = Random.nextInt(height)
        } while (isColliding(exit, exitPosition))
        addTrou(exit, exitPosition)
    }

    // Returns true if the given obstacle is colliding with any other obstacle
    private fun isColliding(obstacle: Iobstacle, position: IntArray = IntArray(2)): Boolean {
        if (obstacle is Ball) {
            balls.forEach { ball ->
                val dx = position[0] - ball.position[0]
                val dy = position[1] - ball.position[1]
                val distance = Math.sqrt(dx * dx + dy * dy.toDouble())
                if (distance <ballRadius * 2) {
                    return true
                }
            }
        } else if (obstacle is Murs) {
            walls.forEach { mur ->
                if (mur.position[0] == position[0] && (mur.position[1] <= position[1] && position[1] < mur.position[1] + mur.size)) {
                    return true
                } else if (mur.position[1] == position[1] && (mur.position[0] <= position[0] && position[0] < mur.position[0] + mur.size)) {
                    return true
                }
            }
        } else if (obstacle is Trou) {
            val dx = position[0] - obstacle.position[0]
            val dy = position[1] - obstacle.position[1]
            val distance = Math.sqrt(dx * dx + dy * dy)
            if (distance < obstacle.radius * 2) {
                return true
            }
        }
        return false
    }

    // Adds the given ball obstacle to the list of balls
    private fun addBall(ball: Ball) {
        balls.add(ball)
        addObstacle(ball)
    }

    // Adds the given wall obstacle to the list of walls
    private fun addWall(wall: Murs) {
        walls.add(wall)
        addObstacle(wall)
    }

    // Adds the given tr

 */