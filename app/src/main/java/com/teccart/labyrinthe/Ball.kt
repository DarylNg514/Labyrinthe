package com.teccart.labyrinthe


import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import kotlin.random.Random


import android.graphics.Rect

class Ball(var position: Pair<Int, Int>, var radius: Int) {
    fun updatePosition(newPosition: Pair<Int, Int>) {
        position = newPosition
    }

    fun getRect(): Rect {
        return Rect(position.first - radius, position.second - radius, position.first + radius, position.second + radius)
    }

    fun isColliding(wall: Murs, wallThickness: Int): Boolean {
        val ballRect = this.getRect()
        val wallRect = wall.getRect(wallThickness)
        return Rect.intersects(ballRect, wallRect)
    }
}




/*
class Ball():Iobstacle {
    override val position: IntArray = IntArray(2)
    override var size: Int = 0
    var raduis: Int
    var color: Int
    var listobstacle: ArrayList<Iobstacle>
    //var crayon: Paint

    init {
        position[0] = Random.nextInt(500)
        position[1] = Random.nextInt(500)
        //crayon = Paint()
        raduis = 30
        color = Color.RED
        listobstacle = ArrayList()
    }

    public fun updateposition(x: Int, y: Int) {

    }

    public fun fall()
    {

    }

    override fun checkcollision(x:Int,y:Int):Boolean
    {
        return false
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

class Ball(): Iobstacle {
    override val position: IntArray = IntArray(2)
    override var size: Int = 0
    var radius: Int = 30
    var color: Int = Color.RED
    var listObstacle: ArrayList<Iobstacle> = ArrayList()

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
        listObstacle.add(obstacle)
    }

    override fun addObstacles(obstacles: ArrayList<Iobstacle>) {
        listObstacle.addAll(obstacles)
    }
}

 */