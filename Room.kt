

open class Room(val name: String) {
    protected open val dangerLevel =5
    var monster:Monster? =Goblin()

    fun description() = "Room: $name\n"+"Danger Level: $dangerLevel"+
            "Creature: ${monster?.description ?: "none."}"

    open fun load() = "Nothing much to see here..."
}

open class TownSquare: Room("Town Square"){
    override val dangerLevel = super.dangerLevel - 3
    private var bellSound = "GWONG"

    final override fun load() = "當你進村，村民聚集且歡呼!!"

    private fun ringBell() ="~~鐘樓響起鐘聲宣告您的到來~~ $bellSound"
}
