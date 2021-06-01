function onmessage(message, _, __) {
  if (message && message.topic) {
    PF('globalMessage').renderMessage({
      "summary": message.topic,
      "detail": message.username,
      "severity": "info"
    })
  }
}

function triggerButton1Click() {
  document.getElementById("buttonLogout").click();
}

