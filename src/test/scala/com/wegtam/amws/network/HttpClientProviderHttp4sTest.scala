/*
 * Copyright (c) 2017 Contributors as noted in the AUTHORS.md file
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.wegtam.amws.network

import java.net.{ ServerSocket, URI }

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.{ Directives, Route }
import akka.stream.Materializer
import cats.effect._
import org.http4s.client.blaze._

import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AsyncWordSpec

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.global
import scala.concurrent.duration._

class HttpClientProviderHttp4sTest extends AsyncWordSpec with Matchers with BeforeAndAfterAll {
  implicit val cs: ContextShift[IO] = IO.contextShift(global)
  implicit val timer: Timer[IO]     = IO.timer(global)
  // TODO Switch to clean http4s integration testing.
  implicit private val system: ActorSystem        = ActorSystem()
  implicit private val materializer: Materializer = Materializer(system)

  /**
    * Start a server socket and close it. The port number used by
    * the socket is considered free and returned.
    *
    * @param reuseAddress If set to `false` the returned port will not be useable for some time.
    * @return A port number.
    */
  private def findAvailablePort(reuseAddress: Boolean): Int = {
    val serverSocket = new ServerSocket(0)
    val freePort     = serverSocket.getLocalPort
    serverSocket.setReuseAddress(reuseAddress)
    serverSocket.close()
    freePort
  }

  override protected def afterAll(): Unit = {
    val _ = Await.result(system.terminate(), FiniteDuration(5, SECONDS))
  }

  "HttpClientProvider" when {
    "using Http4s" when {
      "using get" when {
        "endpoint is not found" must {
          "return an error response" in {
            val port = findAvailablePort(true)
            val route: Route = Directives.path("get-me") {
              Directives.get {
                Directives.complete("You should not reach me!")
              }
            }
            val _ = Http().bindAndHandle(route, "localhost", port)

            val uri = new URI(s"http://localhost:$port/get-me-wrong")
            val pld = AmwsRequestPayload(data = None)

            val test = BlazeClientBuilder[IO](global).stream.flatMap { http4sClient =>
              val client = new HttpClientProviderHttp4s[IO](http4sClient)
              val check = client.get(uri)(pld).value.map {
                case Left(e)  => e.code must be(404)
                case Right(d) => fail(s"The request must return an error instead of: $d")
              }
              fs2.Stream.eval(check)
            }
            test.compile.last.map(_.getOrElse(fail("Test not run properly!"))).unsafeToFuture
          }
        }

        "endpoint is valid" must {
          "return the correct response data" in {
            val port             = findAvailablePort(true)
            val expectedResponse = "I am the response!"
            val route: Route = Directives.path("get-me") {
              Directives.get {
                Directives.complete(expectedResponse)
              }
            }
            val _ = Http().bindAndHandle(route, "localhost", port)

            val uri = new URI(s"http://localhost:$port/get-me")
            val pld = AmwsRequestPayload(data = None)

            val test = BlazeClientBuilder[IO](global).stream.flatMap { http4sClient =>
              val client = new HttpClientProviderHttp4s[IO](http4sClient)
              val check = client.get(uri)(pld).value.map {
                case Left(e)  => fail(s"Http request returned an error: $e")
                case Right(d) => d.body must be(expectedResponse)
              }
              fs2.Stream.eval(check)
            }
            test.compile.last.map(_.getOrElse(fail("Test not run properly!"))).unsafeToFuture
          }
        }
      }

      "using post" when {
        "endpoint is not found" must {
          "return an error response" in {
            val port = findAvailablePort(true)
            val route: Route = Directives.path("get-me") {
              Directives.post {
                Directives.complete("You should not reach me!")
              }
            }
            val _ = Http().bindAndHandle(route, "localhost", port)

            val uri = new URI(s"http://localhost:$port/get-me-wrong")
            val pld = AmwsRequestPayload(data = None)

            val test = BlazeClientBuilder[IO](global).stream.flatMap { http4sClient =>
              val client = new HttpClientProviderHttp4s[IO](http4sClient)
              val check = client.post(uri)(pld).value.map {
                case Left(e)  => e.code must be(404)
                case Right(d) => fail(s"The request must return an error instead of: $d")
              }
              fs2.Stream.eval(check)
            }
            test.compile.last.map(_.getOrElse(fail("Test not run properly!"))).unsafeToFuture
          }
        }

        "endpoint is valid" must {
          "return the correct response data" in {
            val port             = findAvailablePort(true)
            val expectedResponse = "I am the response!"
            val route: Route = Directives.path("get-me") {
              Directives.post {
                Directives.complete(expectedResponse)
              }
            }
            val _ = Http().bindAndHandle(route, "localhost", port)

            val uri = new URI(s"http://localhost:$port/get-me")
            val pld = AmwsRequestPayload(data = None)

            val test = BlazeClientBuilder[IO](global).stream.flatMap { http4sClient =>
              val client = new HttpClientProviderHttp4s[IO](http4sClient)
              val check = client.post(uri)(pld).value.map {
                case Left(e)  => fail(s"Http request returned an error: $e")
                case Right(d) => d.body must be(expectedResponse)
              }
              fs2.Stream.eval(check)
            }
            test.compile.last.map(_.getOrElse(fail("Test not run properly!"))).unsafeToFuture
          }
        }
      }
    }
  }

}
