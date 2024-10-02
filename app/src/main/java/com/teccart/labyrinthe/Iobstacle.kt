package com.teccart.labyrinthe

interface Iobstacle {
    val position: IntArray
    val size: Int
    public fun checkcollision(x:Int,y:Int):Boolean
    public fun Addobstacle(O:Iobstacle)
    public fun Addobstacles(Obs:ArrayList<Iobstacle>)

}