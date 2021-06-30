import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.util.*
import kotlin.system.exitProcess

const val MAX_EXPERIENCE: Int =5000

fun main() {
    var townSquare=TownSquare()
    var className=when(townSquare){
        is Room -> "Room"
        is TownSquare -> "TownSquare"
        else -> throw IllegalArgumentException()
    }
    print(className)

    Game.play()
}

object Game {
    private val player = Player("Madrigal")
    private var currentRoom: Room =TownSquare()

    private var worldMap = listOf(
        listOf(currentRoom, Room("Tavern"), Room("Back Room")),
        listOf(Room("Long Corridor"), Room("Generic Room"))
    )

    init{
        println("~~~歡迎，冒險家~~~")
        player.castFireball()
    }
    fun play(){
        while(true){
            println(currentRoom.description())
            println(currentRoom.load())

            // player status
            printPlayerStatus(player)

            print(">>>> 請輸入您的指令: ")
            //println(">>>>您的指令是: ${readLine()}")
            println(GameInput(readLine()).processCommand())
            if(readLine() == "quit" || readLine()== "exit"){
                break
            }
            println("--------------------------------------------")
        }
    }
    private fun printPlayerStatus(player: Player){
        println("(光環: ${player.auraColor()})"+"(運勢: ${if (player.isBlessed) "走運" else "很背"})")
        println("${player.name} ${player.formatHealthStatus()}")
    }

    private class GameInput(arg: String?){
        private val input =arg ?: ""
        val command =input.split(" ")[0]
        val argument = input.split(" ").getOrElse(1, { "" })

        fun processCommand() = when(command.toLowerCase(Locale.getDefault())){
            "move" -> move(argument)
            "map" ->printMap(player)
            "ring" ->ringBell("Gaong~~")
            "quit" -> quit(player)
            "exit" -> quit(player)
            else->commandNotFound()
        }

        private fun commandNotFound() = "我不確定您要做甚麼!"
    }
    private fun move(directionInput: String) =
        try {
            val direction = Direction.valueOf(directionInput.toUpperCase())
            val newPosition = direction.updateCoordinate(player.currentPosition)
            if(!newPosition.isInBounds){
                throw IllegalStateException("$direction 越界了。")
            }

            val newRoom = worldMap[newPosition.y][newPosition.x]
            player.currentPosition = newPosition
            currentRoom =newRoom
            "OK, you move $direction to the ${newRoom.name}.\n${newRoom.load()}"
        }catch(e: Exception){
            "無效方向: $directionInput."
        }

    private fun fight() = currentRoom.monster?.let {
        while (player.healthPoints>0 && it.healthPoints>0){
            Thread.sleep(1000)
        }
        "打玩了"
    }?: "這兒沒有怪物可以打了~~~"

private fun slay(monster: Monster){
    println("${monster.name} 被 ${monster.attack(player)} 攻擊損傷!")
    println("${player.name} 被 ${player.attack(monster)} 攻擊損傷!")
    if(player.healthPoints<=0){
        println(">>>>嘿，真可惜，妳被打敗了! 感謝參與本遊戲 <<<<")
        exitProcess(0)
    }
    if (monster.healthPoints <=0){
        println(">>>> ${monster.name} 已被您擊敗! <<<<")
        currentRoom.monster = null
    }
}

    private fun quit(player: Player) = "~~~ 再見，${player.name}，歡迎再來玩 ~~~"

    private fun printMap(player: Player) {
        val x: Int = player.currentPosition.x
        val y: Int = player.currentPosition.y
        println("x=$x ,y=$y")
        for (h in 0..1) {
            for (i in 0..2) {
                if (i == x && h == y)
                    print("X ")
                else
                    print("O ")
                if (i == 3 && h == 1) {
                    break
                }
            }
            println()
        }
    }
    public fun ringBell(bellSound: String) = " $bellSound ~~~ 鐘樓響起鐘聲宣告您的到來!!"
}



/*private fun formatHealthStatus(healthPoints: Int, isBlessed: Boolean): String {
    val healthStatus = when (healthPoints) {
        100 -> "健康狀態極佳"
        in 90..99 -> "有一些小擦傷"
        in 75..89 -> if (isBlessed) {
            "雖有一些傷口，但恢復很快"
        } else {
            "有一些傷口"
        }
        in 15..74 -> "嚴重受傷"
        //顯示玩家狀態
        else -> "情況不妙"
    }
    return healthStatus
}
private fun castFireball(numFireball: Int = 2): Int{
    println("已喝下$numFireball 杯Fireball")
    return numFireball
}*/

/*private fun drink(glass: Int){
    val drunkenness = when(glass){
        0 -> "清醒"
        in 1..10 -> "微醺"
        in 11..20 -> "微醉"
        in 21..30 -> "醉了"
        in 31..40 -> "大醉"
        in 41..50 -> "爛醉如泥"
        else ->"沒意識"
    }
    println("酒醉狀態:$drunkenness")
}*/

