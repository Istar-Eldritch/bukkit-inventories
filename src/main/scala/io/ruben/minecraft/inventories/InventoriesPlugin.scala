package io.ruben.minecraft.inventories

import java.util.logging.Level._

import org.bukkit.plugin.java.JavaPlugin
import slick.driver.H2Driver.api._
import slick.jdbc.meta.MTable

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}
import DataAccess._
import scala.concurrent.ExecutionContext.Implicits.global

/**
* Created by istar on 13/09/15.
*/
class InventoriesPlugin extends JavaPlugin {

  def configPath = s"${getDataFolder.getAbsolutePath}/config"

  override def onEnable(): Unit = {

    getCommand("inventories").setExecutor(Commands)

    //Setup database

    db.run(MTable.getTables).onComplete {
      case Success(tables) => {
        if(tables.nonEmpty) {
          getLogger.info("Database already exist")
        }
        else {
          getLogger.info("Creating database for first time")

          val setup: DBIO[Unit] = DBIO.seq(
            (inventories.schema).create
          )
          val setupDb: Future[Unit] = db.run(setup)

          Await.result(setupDb, Duration.Inf)
          getLogger.info("Database created")
        }
      }
      case Failure(f) => {
        getLogger.log(SEVERE, "Couldn't read/write the database")
      }
    }
  }

}