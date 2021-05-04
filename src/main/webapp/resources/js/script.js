function onmessage(message, _, __) {
  if (message && message.topic) {
    PF('globalMessage').renderMessage({
      "summary": message.topic,
      "detail": message.username,
      "severity": "info"
    })
  }
}
