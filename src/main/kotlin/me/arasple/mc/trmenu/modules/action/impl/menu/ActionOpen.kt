package me.arasple.mc.trmenu.modules.action.impl.menu

import me.arasple.mc.trmenu.api.TrMenuAPI
import me.arasple.mc.trmenu.api.events.MenuOpenEvent
import me.arasple.mc.trmenu.data.MetaPlayer
import me.arasple.mc.trmenu.display.Menu
import me.arasple.mc.trmenu.modules.action.base.Action
import me.arasple.mc.trmenu.utils.Tasks
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/3/29 21:36
 */
class ActionOpen : Action("open(s)?|(open)?(-)?gui|(tr)?menu") {

    override fun onExecute(player: Player) {
        Tasks.runDelayTask(Runnable {
            val openingMenu = getOpeningMenu(player)
            MetaPlayer.setArguments(player, openingMenu.second)
            openingMenu.first?.open(player, 0, MenuOpenEvent.Reason.PLAYER_COMMAND)
        }, 1)
    }

    fun getOpeningMenu(player: Player): Pair<Menu?, Array<String>> {
        val args = getContent(player).split(" ")
        return Pair(TrMenuAPI.getMenuById(args[0]), if (args.size == 1) arrayOf() else args.toTypedArray().copyOfRange(1, args.size - 1))
    }

}