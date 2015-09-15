package io.ruben.minecraft.inventories

import org.bukkit.command.{Command, CommandSender, CommandExecutor}
import org.bukkit.entity.Player
import org.bukkit.inventory.PlayerInventory

import slick.driver.H2Driver.api._
import DataAccess._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

/**
 * Created by istar on 16/09/15.
 */
object Commands extends CommandExecutor {
  override def onCommand(commandSender: CommandSender, command: Command, s: String,  arguments: Array[String]): Boolean = {
    commandSender match {
      case player: Player =>
        val playerId = player.getUniqueId.toString

        arguments.headOption match {
          case Some(cmd) =>

            cmd match {
              case "save" | "sv" =>
                db.run(inventories += Inventory.fromBukkit(player.getInventory)).onComplete {
                  case Success(response) => player.sendMessage("Inventory saved")
                  case Failure(err) => err.printStackTrace()
                }

              case "recover" | "rec" =>
                arguments.tail.headOption match {
                  case Some(arg) =>
                    db.run(inventories.filter(_.id === arg.toInt).result.headOption).onSuccess {
                      case Some(inv) =>
                        player.getInventory.setContents(Inventory.deserializeStacks(inv.contents))
                        player.getInventory.setArmorContents(Inventory.deserializeStacks(inv.armor.get))
                    }
                  case None => player.sendMessage("You have to specify an id")
                }

              case _ => player.sendMessage("The subcommand is not valid")
            }

          case None => player.sendMessage(s"${command.getUsage}\nYou have to specify a subcommand!")
        }
      case _ => commandSender.sendMessage("You are not a player!")
    }
    true
  }
}
