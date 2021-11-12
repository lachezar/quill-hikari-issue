package issueapp

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import io.getquill.{PostgresZioJdbcContext, SnakeCase}
import io.getquill.context.ZioJdbc.{DataSourceLayer, QuillZioExt}
import zio.clock.Clock
import zio.console.{Console, putStrLn}
import zio.duration.durationInt
import zio.{ExitCode, Has, RIO, Schedule, TaskLayer, URIO}

import java.io.Closeable
import javax.sql.DataSource

object IssueApp extends zio.App {

  val dataSourceLayer: TaskLayer[Has[DataSource with Closeable]] = {
    val hikariConfig: HikariConfig = new HikariConfig()
    hikariConfig.setDriverClassName("org.postgresql.Driver")
    hikariConfig.setJdbcUrl("jdbc:postgresql://localhost:5432/test")
    hikariConfig.setUsername("test_user")
    hikariConfig.setPassword("test")
    val hikariDataSource: HikariDataSource = new HikariDataSource(hikariConfig)

    DataSourceLayer.fromDataSource(hikariDataSource)
  }

  def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    issueAppLogic.provideSomeLayer[zio.ZEnv](dataSourceLayer).exitCode

  lazy val DbContext = new PostgresZioJdbcContext(SnakeCase)
  import DbContext.{run => qrun, _}

  val issueAppLogic: RIO[Console with Clock with Has[DataSource with Closeable], Long] =
    qrun(query[TestTable]).onDataSource
      .tapBoth(e => putStrLn(s"error: $e"), l => putStrLn(l.mkString))
      .repeat(Schedule.spaced(1.second))
      .retry(Schedule.spaced(1.second))
}

case class TestTable(testId: Int)
