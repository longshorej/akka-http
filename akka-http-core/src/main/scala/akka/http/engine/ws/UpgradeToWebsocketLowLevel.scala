/*
 * Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com>
 */

package akka.http.engine.ws

import akka.http.model.HttpResponse
import akka.http.model.ws.{ Message, UpgradeToWebsocket }
import akka.stream.FlowMaterializer
import akka.stream.scaladsl.Flow

/**
 * Currently internal API to handle FrameEvents directly.
 *
 * INTERNAL API
 */
private[http] abstract class UpgradeToWebsocketLowLevel extends InternalCustomHeader("UpgradeToWebsocket") with UpgradeToWebsocket {
  /**
   * The low-level interface to create Websocket server based on "frames".
   * The user needs to handle control frames manually in this case.
   *
   * Returns a response to return in a request handler that will signal the
   * low-level HTTP implementation to upgrade the connection to Websocket and
   * use the supplied handler to handle incoming Websocket frames.
   *
   * INTERNAL API (for now)
   */
  private[http] def handleFrames(handlerFlow: Flow[FrameEvent, FrameEvent, Any])(implicit mat: FlowMaterializer): HttpResponse

  override def handleMessages(handlerFlow: Flow[Message, Message, Any])(implicit mat: FlowMaterializer): HttpResponse =
    handleFrames(Websocket.handleMessages(handlerFlow))
}