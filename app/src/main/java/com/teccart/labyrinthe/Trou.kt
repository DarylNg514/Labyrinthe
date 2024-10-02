package com.teccart.labyrinthe

import android.graphics.Color
import android.graphics.Rect
import kotlin.random.Random

class Trou(var position: Pair<Int, Int>, var Rayon: Int) {

    fun getRect(): Rect {
        return Rect(position.first - Rayon, position.second - Rayon, position.first + Rayon, position.second + Rayon)
    }

    fun isColliding(other: Trou): Boolean {
        val thisRect = this.getRect()
        val otherRect = other.getRect()
        return Rect.intersects(thisRect, otherRect)
    }
}

/*
class Trou:Iobstacle {
    override val position: IntArray = IntArray(2)
    override var size: Int = 0
    var listobstacle:ArrayList<Iobstacle>
    var color:Int
    var Rayon: Int

    init {
        position[0]=  Random.nextInt(500)
        position[1]=Random.nextInt(500)
        listobstacle= ArrayList()
        color=Color.BLACK
        Rayon=30
    }
    /*override fun checkcollision(x:Int,y:Int):Boolean
    {
        return false
    }*/
    override fun checkcollision(x: Int, y: Int): Boolean {
        val dx = x - position[0]
        val dy = y - position[1]
        return Math.sqrt((dx * dx + dy * dy).toDouble()) < Rayon
    }

    fun getRect(): Rect {
        return Rect(position[0] - Rayon, position[1] - Rayon, position[0] + Rayon, position[1] + Rayon)
    }
    fun isColliding(other: Trou): Boolean {
        val dx = position[0] - other.position[0]
        val dy = position[1] - other.position[1]
        return Math.sqrt((dx * dx + dy * dy).toDouble()) < Rayon + other.Rayon
    }

    override fun Addobstacle(O:Iobstacle)
    {

    }
    override fun Addobstacles(Obs:ArrayList<Iobstacle>)
    {
    }
}
*/

/*

class Trou(): Iobstacle {
    override val position: IntArray = IntArray(2)
    override var size: Int = 0
    var color: Int = Color.BLACK
    var radius: Int = 30

    init {
        position[0] = Random.nextInt(500)
        position[1] = Random.nextInt(500)
        size = 2 * radius
    }

    override fun checkCollision(x: Int, y: Int): Boolean {
        val dx = x - position[0]
        val dy = y - position[1]
        return dx * dx + dy * dy <= radius * radius
    }

    override fun addObstacle(obstacle: Iobstacle) {
        // do nothing, as trous cannot be obstacles
    }

    override fun addObstacles(obstacles: ArrayList<Iobstacle>) {
        // do nothing, as trous cannot be obstacles
    }
}

 */