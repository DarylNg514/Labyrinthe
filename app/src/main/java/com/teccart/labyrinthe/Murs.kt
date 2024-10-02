package com.teccart.labyrinthe

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import kotlin.random.Random


class Murs(var position: Pair<Int, Int>, var longueur: Int, var orientation: Int) {
    companion object {
        const val HORIZONTAL = 0
        const val VERTICAL = 1
    }

    fun getRect(thickness: Int): Rect {
        return if (orientation == VERTICAL) {
            Rect(position.first, position.second - longueur / 2, position.first + thickness, position.second + longueur / 2)
        } else {
            Rect(position.first - longueur / 2, position.second, position.first + longueur / 2, position.second + thickness)
        }
    }

    fun isColliding(other: Murs, wallThickness: Int): Boolean {
        val thisRect = this.getRect(wallThickness)
        val otherRect = other.getRect(wallThickness)
        return Rect.intersects(thisRect, otherRect)
    }
}

/*
class Murs():Iobstacle {
    override val position: IntArray = IntArray(2)
    override var size: Int = 0
    val orientation: Int = Random.nextInt(2) // 0 pour vertical, 1 pour horizontal
    val longueur: Int = 250
    var D:Int=1
    var color: Int
    var listobstacle: ArrayList<Iobstacle>
    //var crayon: Paint

    init {
        position[0] = Random.nextInt(500)
        position[1] = Random.nextInt(500)
        //crayon = Paint()
        size = if (orientation == 1) longueur else D
        color = Color.BLACK
        listobstacle = ArrayList()
    }

    fun getRect(thickness: Int): Rect {
        return if (orientation == 1) { // Assuming 1 represents a vertical wall
            Rect(position[0], position[1] - longueur / 2, position[0] + thickness, position[1] + longueur / 2)
        } else { // Otherwise, it's a horizontal wall
            Rect(position[0] - longueur / 2, position[1], position[0] + longueur / 2, position[1] + thickness)
        }
    }

    fun isColliding(other: Murs): Boolean {
        if (orientation == 1 && other.orientation == 1) {
            return position[0] == other.position[0] && Math.abs(position[1] - other.position[1]) <= longueur
        } else if (orientation == 0 && other.orientation == 0) {
            return position[1] == other.position[1] && Math.abs(position[0] - other.position[0]) <= longueur
        } else {
            return false
        }
    }

    override fun checkcollision(x: Int, y: Int): Boolean {
        val wallThickness = 5 // Adjust the thickness as needed

        if (orientation == 1) {
            // Check if the point (x, y) is within the vertical range of the wall and within its thickness
            return y >= position[1] - wallThickness / 2 && y <= position[1] + wallThickness / 2 &&
                    x >= position[0] && x <= position[0] + longueur
        } else {
            // Check if the point (x, y) is within the horizontal range of the wall and within its thickness
            return x >= position[0] - wallThickness / 2 && x <= position[0] + wallThickness / 2 &&
                    y >= position[1] && y <= position[1] + D
        }
    }

    override fun Addobstacle(O:Iobstacle)
    {
        listobstacle.add(O)
    }
    override fun Addobstacles(Obs:ArrayList<Iobstacle>)
    {
        listobstacle.addAll(Obs)
    }

}*/


/*

class Murs(): Iobstacle {
    override val position: IntArray = IntArray(2)
    override var size: Int = 0
    var orientation: Int = 1 // 1=horizontal 0=vertical
    var longueur: Int = 10
    var D: Int = 1
    var color: Int = Color.BLACK
    var listObstacle: ArrayList<Iobstacle> = ArrayList()

    init {
        position[0] = Random.nextInt(500)
        position[1] = Random.nextInt(500)
        size = if (orientation == 1) longueur else D
    }

    override fun checkCollision(x: Int, y: Int): Boolean {
        if (orientation == 1) {
            return x >= position[0] && x <= position[0] + longueur && y == position[1]
        } else {
            return x == position[0] && y >= position[1] && y <= position[1] + D
        }
    }

    override fun addObstacle(obstacle: Iobstacle) {
        listObstacle.add(obstacle)
    }

    override fun addObstacles(obstacles: ArrayList<Iobstacle>) {
        listObstacle.addAll(obstacles)
    }
}

 */