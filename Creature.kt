

interface Fightable{
    var healthPoint: Int
    val diceCount: Int
    val diceSides: Int
    val damageRoll: Int

    fun attack(opponent: Fightable): Int
}