package net.gegy1000.hgk.model

class StatusMessagesModel(val messages: Array<Status>) {
    class Status(val name: String, val options: Array<String>)
}
