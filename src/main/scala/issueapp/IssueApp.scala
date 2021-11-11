package issueapp

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import io.getquill.{PostgresZioJdbcContext, SnakeCase}
import io.getquill.context.ZioJdbc.DataSourceLayer
import zio.blocking.Blocking
import zio.clock.Clock
import zio.console.{Console, putStrLn}
import zio.duration.durationInt
import zio.{ExitCode, Has, RIO, Schedule, Task, URIO, ZLayer, ZManaged}

import java.io.Closeable
import java.sql.Connection
import javax.sql.DataSource

object IssueApp extends zio.App {

  val dataSourceLayer: ZLayer[Blocking, Throwable, Has[DataSource with Closeable]] = {
    val hikariConfig: HikariConfig = new HikariConfig()
    hikariConfig.setDriverClassName("org.postgresql.Driver")
    hikariConfig.setJdbcUrl("jdbc:postgresql://localhost:5432/test")
    hikariConfig.setUsername("test_user")
    hikariConfig.setPassword("test")
    val hikariDataSource: HikariDataSource = new HikariDataSource(hikariConfig)

    for {
      block <- ZManaged.environment[Blocking]
      ds <- ZManaged.fromAutoCloseable(Task(hikariDataSource: DataSource with Closeable))
    } yield Has(ds) ++ block
  }.toLayerMany

  val connectionLayer: ZLayer[Any, Throwable, Has[Connection]] = Blocking.live >>> dataSourceLayer >>> DataSourceLayer.live

  def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = issueAppLogic.provideSomeLayer[zio.ZEnv](connectionLayer).exitCode

  lazy val DbContext = new PostgresZioJdbcContext(SnakeCase)
  import DbContext.{ run => qrun, _ }

  val issueAppLogic: RIO[Console with Clock with Has[Connection], Long] =
    (qrun(query[TestTable]).tapError(e => putStrLn(s"error: $e")) >>=
      (l => putStrLn(l.mkString))).repeat(Schedule.spaced(1.second)).retry(Schedule.spaced(1.second))
}

case class TestTable(testId: Int)
