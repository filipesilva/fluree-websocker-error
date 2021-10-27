# fluree-websocket-error

Repro for ___

Fails when writing a 1024+ byte event over websocket. The event arrives on the ledger though, and can be seen via http://localhost:8090/exploredb as having one of the predicates as `size-1024`.

```
docker-compose up fluree
# in another terminal
clj -X repro/-main
```

```
17:53:11.113 [AsyncHttpClient-3-1] ERROR fluree.db.util.log - "websocket error"
io.netty.handler.codec.http.websocketx.CorruptedWebSocketFrameException: Max frame length of 10240 has been exceeded.
	at io.netty.handler.codec.http.websocketx.WebSocket08FrameDecoder.protocolViolation(WebSocket08FrameDecoder.java:426)
	at io.netty.handler.codec.http.websocketx.WebSocket08FrameDecoder.decode(WebSocket08FrameDecoder.java:286)
	at io.netty.handler.codec.ByteToMessageDecoder.decodeRemovalReentryProtection(ByteToMessageDecoder.java:508)
	at io.netty.handler.codec.ByteToMessageDecoder.callDecode(ByteToMessageDecoder.java:447)
	at io.netty.handler.codec.ByteToMessageDecoder.channelRead(ByteToMessageDecoder.java:276)
```

